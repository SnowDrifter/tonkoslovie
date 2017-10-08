package ru.romanov.tonkoslovie.content.exercise.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.exercise.Exercise;
import ru.romanov.tonkoslovie.content.exercise.ExerciseRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
        Exercise exercise = exerciseRepository.findOne(id);

        if (exercise != null) {
            return ResponseEntity.ok(exercise);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/exercise/randomId")
    public Map<String, Long> getRandomExerciseId(@RequestParam(required = false) List<Long> excludeIds) {
        Long id;

        if(excludeIds == null || excludeIds.isEmpty()) {
            id = exerciseRepository.findRandomExerciseId();
        } else {
            id = exerciseRepository.findRandomExerciseIdExcludeIds(excludeIds);
        }

        return new HashMap<String, Long>() {{
            put("id", id);
        }};
    }

    @DeleteMapping(value = "/exercise")
    public void deleteExercise(@RequestParam Long id) {
        if (exerciseRepository.exists(id)) {
            exerciseRepository.delete(id);
        }
    }

}
