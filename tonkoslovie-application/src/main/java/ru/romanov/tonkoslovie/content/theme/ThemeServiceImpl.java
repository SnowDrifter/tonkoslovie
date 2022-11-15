package ru.romanov.tonkoslovie.content.theme;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.content.theme.dto.ThemeDto;
import ru.romanov.tonkoslovie.content.theme.dto.ThemeMapper;
import ru.romanov.tonkoslovie.content.theme.entity.Theme;
import ru.romanov.tonkoslovie.model.web.RestPage;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ThemeServiceImpl implements ThemeService {

    private final ThemeRepository themeRepository;

    @Override
    @Cacheable(cacheNames = "themeList", key = "#page + '-' + #size + '-' + #sortField + '-' + #direction", condition = "!#includeUnpublished")
    public RestPage<ThemeDto> getThemes(int page, int size, boolean includeUnpublished, String sortField, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Theme> themes;
        if (includeUnpublished) {
            themes = themeRepository.findAll(pageable);
        } else {
            themes = themeRepository.findAllByPublishedTrue(pageable);
        }

        return RestPage.of(themes.map(ThemeMapper.INSTANCE::toDto));
    }

    @Override
    @Cacheable(value = "theme", key = "#id", unless = "#result == null")
    public Optional<ThemeDto> getTheme(long id) {
        return themeRepository.findById(id)
                .map(ThemeMapper.INSTANCE::toDto);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "themeList", allEntries = true),
            @CacheEvict(value = "theme", key = "#themeDto.id")
    })
    public ThemeDto save(ThemeDto themeDto) {
        Theme theme = ThemeMapper.INSTANCE.toEntity(themeDto);
        theme = themeRepository.save(theme);
        return ThemeMapper.INSTANCE.toDto(theme);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "themeList", allEntries = true),
            @CacheEvict(value = "theme", key = "#id")
    })
    public void delete(long id) {
        themeRepository.deleteById(id);
    }
}
