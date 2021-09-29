package ru.romanov.tonkoslovie.content.theme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.content.theme.entity.Theme;

@Repository
public interface ThemeRepository extends PagingAndSortingRepository<Theme, Long> {

    Page<Theme> findAllByPublishedTrue(Pageable pageable);

}
