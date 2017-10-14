package ru.romanov.tonkoslovie.content.theme.web;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.theme.Theme;
import ru.romanov.tonkoslovie.content.theme.ThemeRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class ThemeController {

    private final ThemeRepository themeRepository;

    @Autowired
    public ThemeController(ThemeRepository themeRepository) {
        this.themeRepository = themeRepository;
    }

    @GetMapping("/themes")
    public List<Theme> themes() {
        return new ArrayList<>(themeRepository.findAllByOrderByIdAsc());
    }

    @PostMapping(value = "/theme")
    public Theme saveTheme(@RequestBody Theme theme) {
        return themeRepository.save(theme);
    }

    @GetMapping(value = "/theme")
    public ResponseEntity<Theme> getTheme(@RequestParam Long id) {
        Theme theme = themeRepository.findOne(id);

        if (theme != null) {
            return ResponseEntity.ok(theme);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/theme")
    public void deleteTheme(@RequestParam Long id) {
        if (themeRepository.exists(id)) {
            themeRepository.delete(id);
        }
    }

}
