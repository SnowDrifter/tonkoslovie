package ru.romanov.tonkoslovie.content.theme.web;


import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.exercise.Exercise;
import ru.romanov.tonkoslovie.content.theme.Theme;
import ru.romanov.tonkoslovie.content.theme.ThemeRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class ThemeController {

    private final ThemeRepository themeRepository;

    @GetMapping("/themes")
    public List<Theme> themes(@RequestParam(required = false, defaultValue = "true") Boolean onlyPublished) {
        if (onlyPublished) {
            return new ArrayList<>(themeRepository.findByPublishedTrueOrderByIdAsc());
        } else {
            return new ArrayList<>(themeRepository.findAllByOrderByIdAsc());
        }
    }

    @PostMapping(value = "/theme")
    public Theme saveTheme(@RequestBody Theme theme) {
        return themeRepository.save(theme);
    }

    @GetMapping(value = "/theme")
    @Transactional
    public ResponseEntity<Theme> getTheme(@RequestParam Long id,
                                          @RequestParam(required = false, defaultValue = "false") boolean randomExercises,
                                          @RequestParam(required = false, defaultValue = "5") int exercisesCount) {
        Optional<Theme> themeOptional = themeRepository.findById(id);

        if (!themeOptional.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Theme theme = themeOptional.get();

        List<Exercise> exercises = theme.getExercises();
        if (randomExercises) {
            Collections.shuffle(exercises);
        }

        exercises = exercises.subList(0, Math.min(exercises.size(), exercisesCount));
        theme.setExercises(exercises);

        return ResponseEntity.ok(theme);
    }

    @DeleteMapping(value = "/theme")
    public void deleteTheme(@RequestParam Long id) {
        if (themeRepository.existsById(id)) {
            themeRepository.deleteById(id);
        }
    }

}
