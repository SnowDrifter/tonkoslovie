package ru.romanov.tonkoslovie.content.exercise.web;

import com.google.common.collect.ImmutableMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.exercise.Exercise;
import ru.romanov.tonkoslovie.content.exercise.ExerciseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/content")
public class ExerciseController {

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public ExerciseController(ExerciseRepository exerciseRepository) {
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping("/exercises")
    public List<Exercise> exercises() {
        return new ArrayList<>(exerciseRepository.findAllByOrderByIdAsc());
    }

    @PostMapping(value = "/exercise")
    public Exercise saveExercise(@RequestBody Exercise exercise) {
        return exerciseRepository.save(exercise);
    }


    @GetMapping(value = "/exercise")
    public ResponseEntity<Exercise> getExercise(@RequestParam Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);

        if (exercise.isPresent()) {
            return ResponseEntity.ok(exercise.get());
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/exercise/randomId")
    public Map<String, Object> getRandomExerciseId(@RequestParam(required = false) List<Long> excludeIds) {
        Long id;

        if(excludeIds == null || excludeIds.isEmpty()) {
            id = exerciseRepository.findRandomExerciseId();
        } else {
            id = exerciseRepository.findRandomExerciseIdExcludeIds(excludeIds);
        }

        return ImmutableMap.of("id", id);
    }

    @GetMapping(value = "/exercises/findByTitle")
    public List<Exercise> findTextsByTitle(@RequestParam String title) {
        return exerciseRepository.findByTitleContainingIgnoreCase(title);
    }

    @DeleteMapping(value = "/exercise")
    public void deleteExercise(@RequestParam Long id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
        }
    }

}
