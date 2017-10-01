package ru.romanov.tonkoslovie.content.exercise.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.exercise.Exercise;
import ru.romanov.tonkoslovie.content.exercise.ExerciseRepository;

import java.util.ArrayList;
import java.util.List;

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
    public ResponseEntity<Exercise> getLesson(@RequestParam Long id) {
        Exercise exercise = exerciseRepository.findOne(id);

        if (exercise != null) {
            return ResponseEntity.ok(exercise);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/exercise")
    public void deleteLesson(@RequestParam Long id) {
        if (exerciseRepository.exists(id)) {
            exerciseRepository.delete(id);
        }
    }

}
