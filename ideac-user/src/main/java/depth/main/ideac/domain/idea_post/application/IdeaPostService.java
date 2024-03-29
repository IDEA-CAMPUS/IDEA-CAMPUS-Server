package depth.main.ideac.domain.idea_post.application;

import depth.main.ideac.domain.idea_post.IdeaPost;
import depth.main.ideac.domain.idea_post.dto.request.RegisterIdeaReq;
import depth.main.ideac.domain.idea_post.dto.request.UpdateIdeaReq;
import depth.main.ideac.domain.idea_post.dto.response.GetAllIdeasRes;
import depth.main.ideac.domain.idea_post.dto.response.GetDetailIdeaRes;
import depth.main.ideac.domain.idea_post.repository.IdeaPostRepository;
import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.config.security.token.UserPrincipal;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ApiResponse;
import depth.main.ideac.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class IdeaPostService {

    private final IdeaPostRepository ideaPostRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    //아이디어 등록
    @Transactional
    public Long registerIdea(UserPrincipal userPrincipal, RegisterIdeaReq registerIdeaReq){
        User user = userRepository.findById(userPrincipal.getId()).get();

        IdeaPost ideapost = IdeaPost.builder()
                .title(registerIdeaReq.getTitle())
                .keyword(registerIdeaReq.getKeyword())
                .simpleDescription(registerIdeaReq.getSimpleDescription())
                .detailedDescription(registerIdeaReq.getDetailedDescription())
                .url1(registerIdeaReq.getUrl1())
                .url2(registerIdeaReq.getUrl2())
                .hits(0L)
                .user(user)
                .build();

        ideaPostRepository.save(ideapost);

        return ideapost.getId();
    }

    //상세 조회
    @Transactional
    public ResponseEntity<?> getDetailIdea(Long id) {
        IdeaPost ideaPost = ideaPostRepository.findById(id).get();

        String[] split = ideaPost.getKeyword().split(",");
        List<String> list = Arrays.asList(split);
        GetDetailIdeaRes getDetailIdeaRes = GetDetailIdeaRes.builder()
                .id(ideaPost.getId())
                .color(ideaPost.getUser().getColor())
                .nickName(ideaPost.getUser().getNickname())
                .title(ideaPost.getTitle())
                .simpleDescription(ideaPost.getSimpleDescription())
                .keyWord(list)
                .detailedDescription(ideaPost.getDetailedDescription())
                .url1(ideaPost.getUrl1())
                .url2(ideaPost.getUrl2())
                .hits(ideaPost.getHits())
                .createdAt(ideaPost.getCreatedAt())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(getDetailIdeaRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    //아이디어 수정
    @Transactional
    public ResponseEntity<?> updateIdea(Long id, Long userId,  UpdateIdeaReq updateIdeaReq) {
        if (!isAdminOrWriter(id, userId)) {
            throw new AccessDeniedException("해당 게시글에 대한 권한이 없습니다.");
        }
        IdeaPost ideaPost = ideaPostRepository.findById(id).get();

        ideaPost.setTitle(updateIdeaReq.getTitle());
        ideaPost.setKeyword(updateIdeaReq.getKeyWord());
        ideaPost.setSimpleDescription(updateIdeaReq.getSimpleDescription());
        ideaPost.setDetailedDescription(updateIdeaReq.getDetailedDescription());
        ideaPost.setUrl1(updateIdeaReq.getUrl1());
        ideaPost.setUrl2(updateIdeaReq.getUrl2());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(null)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    //아이디어 삭제
    @Transactional
    public ResponseEntity<?> deleteIdea(Long id, Long userId) {
        if (!isAdminOrWriter(id, userId)) {
            throw new AccessDeniedException("해당 게시글에 대한 권한이 없습니다.");
        }
        IdeaPost ideaPost = ideaPostRepository.findById(id).get();
        ideaPostRepository.delete(ideaPost);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(null)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    //모든 아이디어들 조회
    public ResponseEntity<?> getAllIdea(int page, int size, String sortBy) {
        Pageable pageable;
        if (sortBy.equals("hits")) {
            pageable = PageRequest.of(page, size, Sort.by("hits").descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        }
        Page<IdeaPost> pageResult = ideaPostRepository.findAll(pageable);
        List<GetAllIdeasRes> getAllIdeasRes = pageResult.getContent().stream()
                .map(tmp -> GetAllIdeasRes.builder()
                                .id(tmp.getId())
                                .color(tmp.getUser().getColor())
                                .nickName(tmp.getUser().getNickname())
                                .title(tmp.getTitle())
                                .simpleDescription(tmp.getSimpleDescription())
                                .keyword(Arrays.asList(tmp.getKeyword().split(",")))
                                .hits(tmp.getHits())
                                .createdAt(tmp.getCreatedAt()).build())
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(getAllIdeasRes)
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    //권한 검증
    public boolean isAdminOrWriter(Long ideaId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));

        IdeaPost ideaPost = ideaPostRepository.findById(ideaId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND));

        boolean isAdmin = user.getRole() == Role.ADMIN || user.getRole() == Role.OWNER ;
        boolean isWriter = ideaPost.getUser().getId().equals(userId);

        return isAdmin || isWriter;
    }

    @Transactional
    //@Cacheable(value = "Contents", key = "#contentId", cacheManager = "contentCacheManager")
    public void addHitToRedis(Long id) {
        String key = "ideaPostHitCnt::" + id;
        ValueOperations<String,String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get(key);
        if(value==null)
            valueOperations.set(key, "1");
        else {
            Long incrementedValue = Long.parseLong(value) + 1L;
            valueOperations.set(key, String.valueOf(incrementedValue));
        }
    }

    @Transactional
    @Scheduled(cron = "0 0/3 * * * ?")
    public void deleteHitFromRedis() {
        Set<String> keys = redisTemplate.keys("ideaPostHitCnt*");
        Iterator<String> it = keys.iterator();
        while (it.hasNext()) {
            String data = it.next();
            Long id = Long.parseLong(data.split("::")[1]);
            Long hits = Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get(data)));

            ideaPostRepository.updateHits(id, hits);
            redisTemplate.delete(data);
        }

        System.out.println("ideaPost hits update complete");
    }
}
