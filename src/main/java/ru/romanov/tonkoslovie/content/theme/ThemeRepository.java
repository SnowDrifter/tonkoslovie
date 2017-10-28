package ru.romanov.tonkoslovie.content.theme;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ThemeRepository extends JpaRepository<Theme, Long> {

    List<Theme> findAllByOrderByIdAsc();

    List<Theme> findByPublishedTrueOrderByIdAsc();

}
