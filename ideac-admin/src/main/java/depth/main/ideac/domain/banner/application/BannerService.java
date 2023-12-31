package depth.main.ideac.domain.banner.application;

import depth.main.ideac.domain.banner.Banner;
import depth.main.ideac.domain.banner.Type;
import depth.main.ideac.domain.banner.dto.BannerListRes;
import depth.main.ideac.domain.banner.dto.BannerDetailRes;
import depth.main.ideac.domain.banner.repository.BannerRepository;
import depth.main.ideac.domain.user.domain.User;
import depth.main.ideac.domain.user.domain.repository.UserRepository;
import depth.main.ideac.global.error.DefaultException;
import depth.main.ideac.global.payload.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository bannerRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    // controller에서 type만 바꿔서 호출
    @Transactional
    public BannerDetailRes uploadBanner(MultipartFile file, String title, Type type, Long userId) throws IOException {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        // 서버에 파일 저장 & DB에 파일 정보 저장
        // - 동일 파일명을 피하기 위해 random값 사용
        String originalFileName = file.getOriginalFilename();
        String saveFileName = createSaveFileName(originalFileName);

        // 2-1.서버에 파일 저장
        file.transferTo(new File(getFullPath(saveFileName)));

        // 2-2. DB에 정보 저장
        String contentType = file.getContentType();

        Banner banner = Banner.builder()
                .title(title)
                .fileName(originalFileName)
                .saveFileName(saveFileName)
                .contentType(contentType)
                .type(type)
                .user(user)
                .build();
        bannerRepository.save(banner);

        return convertToBannerDetailRes(banner);
    }

    // 파일 저장 이름 만들기
    // - 사용자들이 올리는 파일 이름이 같을 수 있으므로, 자체적으로 랜덤 이름을 만들어 사용한다
    private String createSaveFileName(String originalFilename) {
        String ext = extractExt(originalFilename);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + ext;
    }

    // 확장자명 구하기
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }

    // fullPath 만들기
    private String getFullPath(String filename) {
        return uploadPath + filename;
    }

    private BannerDetailRes convertToBannerDetailRes(Banner banner) {
        return BannerDetailRes.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .originalFileName(banner.getFileName())
                .saveFileName(banner.getSaveFileName())
                .contentType(banner.getContentType())
                .type(banner.getType())
                .nickName(banner.getUser().getNickname())
                .createdAt(banner.getCreatedAt())
                .build();
    }

    // 배너 목록 가져오기
    public Page<BannerListRes> getAllBanners(Type type, Pageable pageable) {
        Page<Banner> banners = bannerRepository.findAllByTypeOrderByCreatedAtAsc(type, pageable);

        List<BannerListRes> bannerResList = IntStream.range(0, banners.getContent().size())
                .mapToObj(index -> {
                    Banner banner = banners.getContent().get(index);
                    BannerListRes bannerListRes = convertToBannerListRes(banner);
                    int number = pageable.getPageNumber() * pageable.getPageSize() + index + 1;
                    bannerListRes.setNumber(number);
                    return bannerListRes;
                })
                .toList();

        return new PageImpl<>(bannerResList, pageable, banners.getTotalElements());
    }

    private BannerListRes convertToBannerListRes(Banner banner) {
        return BannerListRes.builder()
                .id(banner.getId())
                .title(banner.getTitle())
                .nickName(banner.getUser().getNickname())
                .createdAt(banner.getCreatedAt())
                .build();
    }

    // 배너 상세 조회
    public BannerDetailRes getDetailBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        return convertToBannerDetailRes(banner);
    }

    // 배너 수정하기
    @Transactional
    public BannerDetailRes updateBanner(MultipartFile file, String title, Long bannerId) throws IOException {
        Banner banner = bannerRepository.findById(bannerId)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));

        String originalFileName = file.getOriginalFilename();
        String saveFileName = createSaveFileName(originalFileName);

        boolean isDelete = deleteFile(banner.getSaveFileName());

        if (!isDelete) {
            throw new RuntimeException("파일 삭제에 실패했습니다.");
        }
        file.transferTo(new File(getFullPath(saveFileName)));

        String contentType = file.getContentType();

        banner.updateBanner(title, originalFileName, saveFileName, contentType);

        return convertToBannerDetailRes(banner);
    }

    private boolean deleteFile(String fileName) {
        File fileToDelete = new File(getFullPath(fileName));

        if (fileToDelete.exists()) {
            return fileToDelete.delete();
        } else { return false; }
    }

    // 배너 삭제하기
    @Transactional
    public void deleteBanner(Long id) {
        Banner banner = bannerRepository.findById(id)
                .orElseThrow(() -> new DefaultException(ErrorCode.INVALID_PARAMETER));
        bannerRepository.delete(banner);
    }

    // 배너 검색
    public Page<BannerListRes> searchBanners(Type type, String word, Pageable pageable) {
        Page<Banner> banners = bannerRepository.findByTypeAndTitleContainingIgnoreCase(type, word, pageable);

        List<BannerListRes> bannerResList = IntStream.range(0, banners.getContent().size())
                .mapToObj(index -> {
                    Banner banner = banners.getContent().get(index);
                    BannerListRes bannerListRes = convertToBannerListRes(banner);
                    int number = pageable.getPageNumber() * pageable.getPageSize() + index + 1;
                    bannerListRes.setNumber(number);
                    return bannerListRes;
                })
                .toList();

        return new PageImpl<>(bannerResList, pageable, banners.getTotalElements());
    }


}
