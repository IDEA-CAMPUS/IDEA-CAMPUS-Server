package depth.main.ideac.domain.project_post.application;

import depth.main.ideac.domain.project_post.ProjectPost;
import depth.main.ideac.domain.project_post.ProjectPostImage;
import depth.main.ideac.domain.project_post.dto.request.PostProjectReq;
import depth.main.ideac.domain.project_post.dto.request.ProjectKeywordReq;
import depth.main.ideac.domain.project_post.dto.response.ProjectDetailRes;
import depth.main.ideac.domain.project_post.dto.response.ProjectRes;
import depth.main.ideac.domain.project_post.repository.ProjectPostImageRepository;
import depth.main.ideac.domain.project_post.repository.ProjectPostRepository;
import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ErrorCode;
import depth.main.ideac.global.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.*;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ProjectPostService {

    private final FileService fileService;
    private final ProjectPostRepository projectPostRepository;
    private final ProjectPostImageRepository projectPostImageRepository;
    private final UserRepository userRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public Long postProject(Long userId, PostProjectReq postProjectReq, List<MultipartFile> images) throws IOException {
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
                .build();
        this.uploadFile(projectPost, images);
        projectPostRepository.save(projectPost);
        return projectPost.getId();
    }

    public Page<ProjectRes> getAllProjects(int page, int size, String sortBy) {
        Pageable pageable;
        if (sortBy.equals("hits")) {
            pageable = PageRequest.of(page, size, Sort.by("hits").descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        }

        Page<ProjectPost> projectPosts = projectPostRepository.findAll(pageable);
        List<ProjectRes> projectResList = projectPosts.getContent()
                .stream()
                .map(projectPost -> {
                    String thumbnail = projectPost.getProjectPostImages().stream()
                            .filter(ProjectPostImage::isThumbnail)
                            .findFirst()
                            .map(ProjectPostImage::getImagePath)
                            .orElse(null);
                    return ProjectRes.builder()
                            .booleanWeb(projectPost.isBooleanWeb())
                            .booleanApp(projectPost.isBooleanApp())
                            .booleanAi(projectPost.isBooleanAi())
                            .team(projectPost.getTeam())
                            .title(projectPost.getTitle())
                            .simpleDescription(projectPost.getSimpleDescription())
                            .thumbnail(thumbnail)
                            .build();
                })
                .toList();
        return new PageImpl<>(projectResList, pageable, projectPosts.getTotalElements());
    }

    public Page<ProjectRes> getProjectsByKeyword(int page, int size, String sortBy, ProjectKeywordReq projectKeywordReq) {
        Pageable pageable;
        if (sortBy.equals("hits")) {
            pageable = PageRequest.of(page, size, Sort.by("hits").descending());
        } else {
            pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        }

        boolean booleanWeb = projectKeywordReq.isBooleanWeb();
        boolean booleanApp = projectKeywordReq.isBooleanApp();
        boolean booleanAi = projectKeywordReq.isBooleanAi();

        Page<ProjectPost> projectPosts = projectPostRepository
                .findByBooleanWebAndBooleanAppAndBooleanAi(booleanWeb, booleanApp, booleanAi, pageable);
        List<ProjectRes> projectResList = projectPosts.getContent()
                .stream()
                .map(projectPost -> {
                    String thumbnail = projectPost.getProjectPostImages().stream()
                            .filter(ProjectPostImage::isThumbnail)
                            .findFirst()
                            .map(ProjectPostImage::getImagePath)
                            .orElse(null);
                    return ProjectRes.builder()
                            .booleanWeb(projectPost.isBooleanWeb())
                            .booleanApp(projectPost.isBooleanApp())
                            .booleanAi(projectPost.isBooleanAi())
                            .team(projectPost.getTeam())
                            .title(projectPost.getTitle())
                            .simpleDescription(projectPost.getSimpleDescription())
                            .thumbnail(thumbnail)
                            .build();
                })
                .toList();
        return new PageImpl<>(projectResList, pageable, projectPosts.getTotalElements());
    }

    public ProjectDetailRes getProjectDetail(Long projectId) {
        ProjectPost projectPost = projectPostRepository.findById(projectId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND, "프로젝트 내용을 찾을 수 없습니다."));
        String thumbnailPath = projectPost.getProjectPostImages().stream()
                .filter(ProjectPostImage::isThumbnail)
                .findFirst()
                .map(ProjectPostImage::getImagePath)
                .orElse(null);
        List<String> otherImagePaths = projectPost.getProjectPostImages().stream()
                .filter(image -> !image.isThumbnail())
                .map(ProjectPostImage::getImagePath)
                .toList();
        return ProjectDetailRes.builder()
                .title(projectPost.getTitle())
                .simpleDescription(projectPost.getSimpleDescription())
                .detailedDescription(projectPost.getDetailedDescription())
                .teamInformation(projectPost.getTeamInformation())
                .githubUrl(projectPost.getGithubUrl())
                .webUrl(projectPost.getWebUrl())
                .googlePlayUrl(projectPost.getGooglePlayUrl())
                .hits(projectPost.getHits())
                .booleanWeb(projectPost.isBooleanWeb())
                .booleanApp(projectPost.isBooleanApp())
                .booleanAi(projectPost.isBooleanAi())
                .thumbnail(thumbnailPath)
                .otherImages(otherImagePaths)
                .build();
    }

    @Transactional
    public void updateProject(Long userId, Long projectId, PostProjectReq updateProjectReq, List<MultipartFile> images) throws IOException {
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
        this.deleteFile(projectId);
        this.uploadFile(projectPost, images);

    }

    @Transactional
    public void deleteProject(Long userId, Long projectId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));
        ProjectPost projectPost = projectPostRepository.findById(projectId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND, "프로젝트를 찾을 수 없습니다."));
        if (user.getRole() != Role.OWNER && user.getRole() != Role.ADMIN && !userId.equals(projectPost.getUser().getId())) {
            throw new DefaultException(ErrorCode.UNAUTHORIZED, "삭제 권한이 없습니다.");
        }
        this.deleteFile(projectId);
        projectPostRepository.deleteById(projectId);
    }

    @Transactional
    public void uploadFile(ProjectPost projectPost, List<MultipartFile> images) throws IOException {
        // 이미지 업로드 및 thumbnail 설정
        List<ProjectPostImage> projectPostImages = new ArrayList<>();
        boolean isFirstImage = true;

        for (MultipartFile image : images) {
            String s3ImageKey = fileService.uploadFile(image, getClass().getSimpleName());

            // 첫 번째 이미지인 경우에만 thumbnail로 설정
            boolean isThumbnail = isFirstImage;
            isFirstImage = false;

            projectPostImages.add(ProjectPostImage.builder()
                    .imagePath(fileService.getUrl(s3ImageKey))
                    .isThumbnail(isThumbnail)
                    .s3key(s3ImageKey)
                    .projectPost(projectPost)
                    .build());
        }
        projectPostImageRepository.saveAll(projectPostImages);
    }

    @Transactional
    public void deleteFile(Long projectId){
        List<ProjectPostImage> images = projectPostImageRepository.findByProjectPostId(projectId);
        for(ProjectPostImage image : images) {
            fileService.deleteFile(image.getS3key()); //s3 삭제
            projectPostImageRepository.deleteById(image.getId()); //엔티티 삭제
        }
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
