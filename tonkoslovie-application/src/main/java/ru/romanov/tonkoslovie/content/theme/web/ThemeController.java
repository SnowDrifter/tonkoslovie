package ru.romanov.tonkoslovie.content.theme.web;


import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.theme.ThemeService;
import ru.romanov.tonkoslovie.content.theme.dto.ThemeDto;
import ru.romanov.tonkoslovie.model.web.RestPage;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.Collections;

import static ru.romanov.tonkoslovie.user.entity.Role.ROLE_ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class ThemeController {

    private final ThemeService themeService;

    @GetMapping("/themes")
    public RestPage<ThemeDto> themes(@RequestParam(defaultValue = "0") @Min(0) int page,
                                     @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                     @RequestParam(defaultValue = "false") boolean unpublished,
                                     @RequestParam(defaultValue = "id") String sortField,
                                     @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc") String direction,
                                     HttpServletRequest request) {
        boolean includeUnpublished = unpublished && request.isUserInRole(ROLE_ADMIN.name());
        return themeService.getThemes(page, size, includeUnpublished, sortField, direction);
    }

    @PostMapping(value = "/theme")
    public ThemeDto saveTheme(@RequestBody ThemeDto themeDto) {
        return themeService.save(themeDto);
    }

    @GetMapping(value = "/theme")
    @Transactional
    public ResponseEntity<ThemeDto> getTheme(@RequestParam Long id,
                                             @RequestParam(defaultValue = "false") boolean shuffleExercises) {
        return themeService.getTheme(id)
                .map(theme -> {
                    if (shuffleExercises && theme.getExercises() != null) {
                        Collections.shuffle(theme.getExercises());
                    }
                    return ResponseEntity.ok(theme);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/theme")
    public void deleteTheme(@RequestParam Long id) {
        themeService.delete(id);
    }

}
