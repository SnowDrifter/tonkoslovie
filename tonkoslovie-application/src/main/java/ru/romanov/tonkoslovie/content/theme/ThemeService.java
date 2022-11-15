package ru.romanov.tonkoslovie.content.theme;

import ru.romanov.tonkoslovie.content.theme.dto.ThemeDto;
import ru.romanov.tonkoslovie.model.web.RestPage;

import java.util.Optional;

public interface ThemeService {

    RestPage<ThemeDto> getThemes(int page, int size, boolean includeUnpublished, String sortField, String direction);

    Optional<ThemeDto> getTheme(long id);

    ThemeDto save(ThemeDto themeDto);

    void delete(long id);
    
}
