package depth.main.ideac.domain.banner.repository;

import depth.main.ideac.domain.banner.Banner;
import depth.main.ideac.domain.banner.Type;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends JpaRepository<Banner, Long> {
    Page<Banner> findAllByTypeOrderByCreatedAtAsc(Type type, Pageable pageable);

    @Query("SELECT b FROM Banner b WHERE b.type = :type AND LOWER(b.title) LIKE %:searchWord%")
    Page<Banner> findByTypeAndTitleContainingIgnoreCase(Type type, String searchWord, Pageable pageable);

    List<Banner> findAllByType(Type type);
}
