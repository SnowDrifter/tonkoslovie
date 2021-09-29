package ru.romanov.tonkoslovie.content.theme;

import ru.romanov.tonkoslovie.content.theme.dto.ThemeDto;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import java.util.Optional;

public interface ThemeService {

    RestPage<ThemeDto> getThemes(int page, int size, boolean includeUnpublished, String sortField);

    Optional<ThemeDto> getTheme(long id);

    ThemeDto save(ThemeDto themeDto);

    void delete(long id);
    
}
