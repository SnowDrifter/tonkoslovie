package ru.romanov.tonkoslovie.content.exercise.web;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.exercise.Exercise;
import ru.romanov.tonkoslovie.content.exercise.ExerciseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;

    @GetMapping("/exercises")
    public List<Exercise> exercises() {
        return exerciseRepository.findAllByOrderByIdAsc();
    }

    @PostMapping(value = "/exercise")
    public Exercise saveExercise(@RequestBody Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @GetMapping(value = "/exercise")
    public ResponseEntity<Exercise> getExercise(@RequestParam Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);

        return exercise.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/exercise/random_id")
    public Map<String, Object> getRandomExerciseId(@RequestParam(required = false) List<Long> excludeIds) {
        if (excludeIds == null) {
            excludeIds = new ArrayList<>();
        }

        Long id = exerciseRepository.findRandomExerciseIdExcludeIds(excludeIds);
        return ImmutableMap.of("id", id);
    }

    @GetMapping(value = "/exercises/find")
    public List<Exercise> findExercises(@RequestParam String title) {
        return exerciseRepository.findByTitleContainingIgnoreCase(title);
    }

    @DeleteMapping(value = "/exercise")
    public void deleteExercise(@RequestParam Long id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
        }
    }

}
