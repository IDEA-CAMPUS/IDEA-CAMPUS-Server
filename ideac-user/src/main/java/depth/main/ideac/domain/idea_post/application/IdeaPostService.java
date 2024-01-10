package depth.main.ideac.domain.idea_post.application;

import depth.main.ideac.domain.idea_post.IdeaPost;
import depth.main.ideac.domain.idea_post.dto.req.RegisterIdeaReq;
import depth.main.ideac.domain.idea_post.dto.req.UpdateIdeaReq;
import depth.main.ideac.domain.idea_post.dto.res.GetAllIdeasRes;
import depth.main.ideac.domain.idea_post.dto.res.GetDetailIdeaRes;
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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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

    //상세아디이어 조회
    @Transactional
    public ResponseEntity<?> getDetailIdea(Long id) {
        IdeaPost ideaPost = ideaPostRepository.findById(id).get();

        GetDetailIdeaRes getDetailIdeaRes = GetDetailIdeaRes.builder()
                .color(ideaPost.getUser().getColor())
                .nickName(ideaPost.getUser().getNickname())
                .title(ideaPost.getTitle())
                .simpleDescription(ideaPost.getSimpleDescription())
                .keyWord(ideaPost.getKeyword())
                .detailedDescription(ideaPost.getDetailedDescription())
                .url1(ideaPost.getUrl1())
                .url2(ideaPost.getUrl2())
                .hits(ideaPost.getHits())
                .build();

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(getDetailIdeaRes)
                .message("상세내용을 가져왔습니다.")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    //아이디어 수정
    @Transactional
    public ResponseEntity<?> updateIdea(Long id, UpdateIdeaReq updateIdeaReq) {
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
                .message("아이디어가 업데이트 되었습니다.!")
                .build();

        return ResponseEntity.ok(apiResponse);
    }
    //아이디어 삭제
    @Transactional
    public ResponseEntity<?> deleteIdea(Long id) {
        IdeaPost ideaPost = ideaPostRepository.findById(id).get();
        ideaPostRepository.delete(ideaPost);
        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(null)
                .message("아이디어가 삭제되었습니다.")
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
                .map(tmp -> new GetAllIdeasRes(
                        tmp.getUser().getColor(),
                        tmp.getUser().getNickname(),
                        tmp.getTitle(),
                        tmp.getSimpleDescription(),
                        tmp.getKeyword()))
                .collect(Collectors.toList());

        ApiResponse apiResponse = ApiResponse.builder()
                .check(true)
                .information(getAllIdeasRes)
                .message("조회목록들입니다.")
                .build();
        return ResponseEntity.ok(apiResponse);
    }

    //권한 검증
    public boolean isAdminOrWriter(Long ideaId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        IdeaPost ideaPost = ideaPostRepository.findById(ideaId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        boolean isAdmin = user.getRole() == Role.ADMIN || user.getRole() == Role.OWNER ;
        boolean isWriter = ideaPost.getUser().getId().equals(userId);

        return isAdmin || isWriter;
    }

    @Transactional
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

    }
}
