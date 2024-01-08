package depth.main.ideac.domain.club_post.application;

import depth.main.ideac.domain.club_post.ClubPost;
import depth.main.ideac.domain.club_post.ClubPostImage;
import depth.main.ideac.domain.club_post.dto.ClubPostReq;
import depth.main.ideac.domain.club_post.dto.ClubPostRes;
import depth.main.ideac.domain.club_post.repository.ClubPostImageRepository;
import depth.main.ideac.domain.club_post.repository.ClubPostRepository;
import depth.main.ideac.domain.club_post.dto.ClubPostDetailRes;
import depth.main.ideac.domain.club_post.dto.UpdateClubPostReq;
import depth.main.ideac.domain.user.domain.Role;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ErrorCode;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubPostService {

    private final UserRepository userRepository;
    private final ClubPostRepository clubPostRepository;
    private final ClubPostImageRepository clubPostImageRepository;
    private final FileService fileService;
    
    // 전체 조회
    public Page<ClubPostRes> getAllClubPosts(Pageable pageable) {
        Page<ClubPost> posts = clubPostRepository.findAll(pageable);

        List<ClubPostRes> clubPostResList = posts.getContent().stream()
                .map(clubPost -> ClubPostRes.builder()
                        .title(clubPost.getTitle())
                        .description(clubPost.getDetailedDescription())
                        .createdAt(clubPost.getCreatedAt())
                        .nickname(clubPost.getUser().getNickname())
                        .thumbnail(clubPost.getClubPostImages().stream()
                                .filter(ClubPostImage::isThumbnail)
                                .findFirst()
                                .map(ClubPostImage::getImagePath).orElse(null))
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(clubPostResList, pageable, posts.getTotalElements());
    }

    // 상세 조회
    public ClubPostDetailRes getDetailClubPosts(Long clubId) {
        ClubPost clubPost = clubPostRepository.findById(clubId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND));

        String thumbnailPath = clubPost.getClubPostImages().stream()
                .filter(ClubPostImage::isThumbnail)
                .findFirst()
                .map(ClubPostImage::getImagePath)
                .orElse(null);
        List<String> otherImagePaths = clubPost.getClubPostImages().stream()
                .filter(image -> !image.isThumbnail())
                .map(ClubPostImage::getImagePath)
                .toList();

        return ClubPostDetailRes.builder()
                .title(clubPost.getTitle())
                .description(clubPost.getDetailedDescription())
                .url1(clubPost.getUrl1())
                .url2(clubPost.getUrl2())
                .nickname(clubPost.getUser().getNickname())
                .thumbnail(thumbnailPath)
                .otherImages(otherImagePaths)
                .createdAt(clubPost.getCreatedAt())
                .build();
    }

    @Transactional
    public Long createClubPost(Long userId, ClubPostReq clubPostReq, List<MultipartFile> images) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));

        ClubPost clubPost = ClubPost.builder()
                .title(clubPostReq.getTitle())
                .detailedDescription(clubPostReq.getDescription())
                .url1(clubPostReq.getUrl1())
                .url2(clubPostReq.getUrl2())
                .user(user)
                .build();

        this.uploadFile(clubPost, images);

        clubPostRepository.save(clubPost);

        return clubPost.getId();
    }

    // 글 수정
    @Transactional
    public void updateClubPost(Long clubPostId, UpdateClubPostReq updateClubPostReq, List<MultipartFile> images) throws IOException {

        ClubPost clubPost = clubPostRepository.findById(clubPostId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND));

        clubPost.setTitle(updateClubPostReq.getTitle());
        clubPost.setDetailedDescription(updateClubPostReq.getDescription());
        clubPost.setUrl1(updateClubPostReq.getUrl1());
        clubPost.setUrl2(updateClubPostReq.getUrl2());
        
        this.deleteFile(clubPostId);
        this.uploadFile(clubPost, images);
    }

    // 글 삭제
    @Transactional
    public void deleteClubPost(Long clubPostId) {
        ClubPost clubPost = clubPostRepository.findById(clubPostId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND));
        this.deleteFile(clubPostId);
        clubPostRepository.delete(clubPost);
    }

    // 로그인한 사용자가 관리자인지 작성자 본인인지 확인하는 메소드
    public boolean isAdminOrWriter(Long clubPostId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.USER_NOT_FOUND));

        ClubPost clubPost = clubPostRepository.findById(clubPostId)
                .orElseThrow(() -> new DefaultException(ErrorCode.CONTENTS_NOT_FOUND));

        boolean isAdmin = user.getRole() == Role.ADMIN || user.getRole() == Role.OWNER ;
        boolean isWriter = clubPost.getUser().getId().equals(userId);

        return isAdmin || isWriter;
    }

    @Transactional
    public void uploadFile(ClubPost clubPost, List<MultipartFile> images) throws IOException {
        // 이미지 업로드 및 thumbnail 설정
        List<ClubPostImage> clubPostImages = new ArrayList<>();
        boolean isFirstImage = true;

        for (MultipartFile image : images) {
            String s3ImageKey = fileService.uploadFile(image, getClass().getSimpleName());

            // 첫 번째 이미지인 경우에만 thumbnail로 설정
            boolean isThumbnail = isFirstImage;
            isFirstImage = false;

            clubPostImages.add(ClubPostImage.builder()
                    .imagePath(fileService.getUrl(s3ImageKey))
                    .isThumbnail(isThumbnail)
                    .s3key(s3ImageKey)
                    .clubPost(clubPost)
                    .build());
        }
        clubPostImageRepository.saveAll(clubPostImages);
    }

    @Transactional
    public void deleteFile(Long clubId){
        List<ClubPostImage> images = clubPostImageRepository.findByClubPostId(clubId);
        for(ClubPostImage image : images) {
            fileService.deleteFile(image.getS3key()); //s3 삭제
            clubPostImageRepository.deleteById(image.getId()); //엔티티 삭제
        }
    }
}
