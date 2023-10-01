package ru.romanov.tonkoslovie.content.theme;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.content.theme.entity.Theme;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    Page<Theme> findAllByPublishedTrue(Pageable pageable);

}
