package depth.main.ideac.domain.banner.application;

import depth.main.ideac.domain.banner.Banner;
import depth.main.ideac.domain.banner.Type;
import depth.main.ideac.domain.banner.dto.BannerRes;
import depth.main.ideac.domain.banner.repository.BannerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BannerService {

    private final BannerRepository bannerRepository;

    // 배너
    public List<BannerRes> viewBanners(Type type) {
        List<Banner> banners = bannerRepository.findAllByType(type);

        return banners.stream()
                .map(this::convertToBannerRes)
                .collect(Collectors.toList());
    }

    private BannerRes convertToBannerRes(Banner banner) {
        return BannerRes.builder()
                .title(banner.getTitle())
                .originalFileName(banner.getFileName())
                .saveFileUrl(banner.getSaveFileUrl())
                .createdAt(banner.getCreatedAt())
                .build();
    }
}
