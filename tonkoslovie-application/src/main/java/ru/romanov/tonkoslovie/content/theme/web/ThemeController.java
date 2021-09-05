package ru.romanov.tonkoslovie.content.theme.web;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.theme.Theme;
import ru.romanov.tonkoslovie.content.theme.ThemeRepository;
import ru.romanov.tonkoslovie.content.theme.dto.ThemeDto;
import ru.romanov.tonkoslovie.content.theme.dto.ThemeMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static ru.romanov.tonkoslovie.user.entity.Role.ROLE_ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class ThemeController {

    private final ThemeRepository themeRepository;

    @GetMapping("/themes")
    public List<ThemeDto> themes(@RequestParam(required = false, defaultValue = "false") boolean unpublished, HttpServletRequest request) {
        List<Theme> themes;
        if (unpublished && request.isUserInRole(ROLE_ADMIN.name())) {
            themes = themeRepository.findAllByOrderByTitleAsc();
        } else {
            themes = themeRepository.findByPublishedTrueOrderByTitleAsc();
        }

        return ThemeMapper.INSTANCE.toDtoList(themes);
    }

    @PostMapping(value = "/theme")
    public ThemeDto saveTheme(@RequestBody ThemeDto themeDto) {
        Theme theme = ThemeMapper.INSTANCE.toEntity(themeDto);
        theme = themeRepository.save(theme);
        return ThemeMapper.INSTANCE.toDto(theme);
    }

    @GetMapping(value = "/theme")
    @Transactional
    public ResponseEntity<ThemeDto> getTheme(@RequestParam Long id,
                                             @RequestParam(required = false, defaultValue = "false") boolean randomExercises) {
        Optional<Theme> themeOptional = themeRepository.findById(id);
        if (themeOptional.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Theme theme = themeOptional.get();

        if (randomExercises) {
            Collections.shuffle(theme.getExercises());
        }

        ThemeDto themeDto = ThemeMapper.INSTANCE.toDto(theme);
        return ResponseEntity.ok(themeDto);
    }

    @DeleteMapping(value = "/theme")
    public void deleteTheme(@RequestParam Long id) {
        if (themeRepository.existsById(id)) {
            themeRepository.deleteById(id);
        }
    }

}
