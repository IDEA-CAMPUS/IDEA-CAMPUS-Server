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

    public List<BannerRes> getBanners(Type type) {
        List<Banner> banners = bannerRepository.findAllByTypeOrderByCreatedAtAsc(type);

        return banners.stream()
                .map(banner -> BannerRes.builder()
                        .title(banner.getTitle())
                        .saveFileUrl(banner.getSaveFileUrl())
                        .build())
                .collect(Collectors.toList());
    }
}
