package depth.main.ideac.domain.project_post.application;

import depth.main.ideac.domain.project_post.ProjectPost;
import depth.main.ideac.domain.project_post.dto.request.PostProjectReq;
import depth.main.ideac.domain.project_post.dto.response.ProjectDetailRes;
import depth.main.ideac.domain.project_post.dto.response.ProjectRes;
import depth.main.ideac.domain.project_post.repository.ProjectPostRepository;
import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectPostService {

    private final ProjectPostRepository projectPostRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Long postProject(Long userId, PostProjectReq postProjectReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));
        if (!postProjectReq.isBooleanWeb() && !postProjectReq.isBooleanApp() && !postProjectReq.isBooleanAi()) {
                throw new DefaultException(ErrorCode.INVALID_PARAMETER, "키워드는 하나 이상 표시해야 합니다.");
        }
        ProjectPost projectPost = ProjectPost.builder()
                .title(postProjectReq.getTitle())
                .simpleDescription(postProjectReq.getSimpleDescription())
                .detailedDescription(postProjectReq.getDetailedDescription())
                .teamInformation(postProjectReq.getTeamInformation())
                .githubUrl(postProjectReq.getGithubUrl())
                .webUrl(postProjectReq.getWebUrl())
                .googlePlayUrl(postProjectReq.getGooglePlayUrl())
                .booleanWeb(postProjectReq.isBooleanWeb())
                .booleanApp(postProjectReq.isBooleanApp())
                .booleanAi(postProjectReq.isBooleanAi())
                .team(user.getOrganization())
                .hits(0L)
                .user(user)
//                .projectPostImages(postProjectReq.getProjectPostImages)
                .build();
        projectPostRepository.save(projectPost);
        return projectPost.getId();
    }

    public Page<ProjectRes> getAllProjects(Pageable pageable) {
        Page<ProjectPost> projectPosts = projectPostRepository.findAllByOrderByCreatedAtDesc(pageable);
        List<ProjectRes> projectResList = projectPosts.getContent()
                .stream()
                .map(projectPost -> ProjectRes.builder()
                        .booleanWeb(projectPost.isBooleanWeb())
                        .booleanApp(projectPost.isBooleanApp())
                        .booleanAi(projectPost.isBooleanAi())
                        .team(projectPost.getTeam())
                        .title(projectPost.getTitle())
                        .simpleDescription(projectPost.getSimpleDescription())
                        .build())
                .toList();

        return new PageImpl<>(projectResList, pageable, projectPosts.getTotalElements());
    }

    public Page<ProjectRes> getAllProjectsByHits(Pageable pageable) {
        Page<ProjectPost> projectPosts = projectPostRepository.findAll(pageable);
        List<ProjectRes> projectResList = projectPosts.getContent()
                .stream()
                .map(projectPost -> ProjectRes.builder()
                        .booleanWeb(projectPost.isBooleanWeb())
                        .booleanApp(projectPost.isBooleanApp())
                        .booleanAi(projectPost.isBooleanAi())
                        .team(projectPost.getTeam())
                        .title(projectPost.getTitle())
                        .simpleDescription(projectPost.getSimpleDescription())
                        .build())
                .toList();

        return new PageImpl<>(projectResList, pageable, projectPosts.getTotalElements());
    }

    public ProjectDetailRes getProjectDetail(Long projectId) {
        ProjectPost projectPost = projectPostRepository.findById(projectId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND, "프로젝트 내용을 찾을 수 없습니다."));
        return ProjectDetailRes.builder()
                .title(projectPost.getTitle())
                .simpleDescription(projectPost.getSimpleDescription())
                .detailedDescription(projectPost.getDetailedDescription())
                .teamInformation(projectPost.getTeamInformation())
                .githubUrl(projectPost.getGithubUrl())
                .webUrl(projectPost.getWebUrl())
                .googlePlayUrl(projectPost.getGooglePlayUrl())
                .booleanWeb(projectPost.isBooleanWeb())
                .booleanApp(projectPost.isBooleanApp())
                .booleanAi(projectPost.isBooleanAi())
                .build();
    }

    @Transactional
    public void updateProject(Long userId, Long projectId, PostProjectReq updateProjectReq) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));
        ProjectPost projectPost = projectPostRepository.findById(projectId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
        if (user.getRole() != Role.OWNER && user.getRole() != Role.ADMIN && !userId.equals(projectPost.getUser().getId())) {
            throw new DefaultException(ErrorCode.UNAUTHORIZED, "수정 권한이 없습니다.");
        }
        projectPost.setTitle(updateProjectReq.getTitle());
        projectPost.setSimpleDescription(updateProjectReq.getSimpleDescription());
        projectPost.setDetailedDescription(updateProjectReq.getDetailedDescription());
        projectPost.setTeamInformation(updateProjectReq.getTeamInformation());
        projectPost.setGithubUrl(updateProjectReq.getGithubUrl());
        projectPost.setWebUrl(updateProjectReq.getWebUrl());
        projectPost.setBooleanWeb(updateProjectReq.isBooleanWeb());
        projectPost.setBooleanApp(updateProjectReq.isBooleanApp());
        projectPost.setBooleanAi(updateProjectReq.isBooleanAi());
    }

    @Transactional
    public void deleteProject(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));
        ProjectPost projectPost = projectPostRepository.findById(projectId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
        if (user.getRole() != Role.OWNER && user.getRole() != Role.ADMIN && !userId.equals(projectPost.getUser().getId())) {
            throw new DefaultException(ErrorCode.UNAUTHORIZED, "삭제 권한이 없습니다.");
        }
        projectPostRepository.deleteById(projectId);
    }

    @Transactional
    public void addHitToRedis(Long id) {
        String key = "projectPostHitCnt::" + id;
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
        Set<String> keys = redisTemplate.keys("projectPostHitCnt*");
        for (String data : keys) {
            Long id = Long.parseLong(data.split("::")[1]);
            Long hits = Long.parseLong(Objects.requireNonNull(redisTemplate.opsForValue().get(data)));

            projectPostRepository.updateHits(id, hits);
            redisTemplate.delete(data);
        }

        System.out.println("projectPost hits update complete");
    }
}
