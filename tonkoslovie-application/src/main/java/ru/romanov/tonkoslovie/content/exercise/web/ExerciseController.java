package ru.romanov.tonkoslovie.content.exercise.web;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.exercise.Exercise;
import ru.romanov.tonkoslovie.content.exercise.ExerciseRepository;
import ru.romanov.tonkoslovie.content.exercise.dto.ExerciseDto;
import ru.romanov.tonkoslovie.content.exercise.dto.ExerciseMapper;

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
    public List<ExerciseDto> exercises() {
        List<Exercise> exercises = exerciseRepository.findAllByOrderByIdAsc();
        return ExerciseMapper.INSTANCE.toDtoList(exercises);
    }

    @PostMapping(value = "/exercise")
    public ExerciseDto saveExercise(@RequestBody ExerciseDto exerciseDto) {
        Exercise exercise = ExerciseMapper.INSTANCE.toEntity(exerciseDto);
        exercise = exerciseRepository.save(exercise);
        return ExerciseMapper.INSTANCE.toDto(exercise);
    }

    @GetMapping(value = "/exercise")
    public ResponseEntity<ExerciseDto> getExercise(@RequestParam Long id) {
        Optional<Exercise> exercise = exerciseRepository.findById(id);

        return exercise.map(ExerciseMapper.INSTANCE::toDto)
                .map(ResponseEntity::ok)
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
    public List<ExerciseDto> findExercises(@RequestParam String title) {
        List<Exercise> exercises = exerciseRepository.findByTitleContainingIgnoreCase(title);
        return ExerciseMapper.INSTANCE.toDtoList(exercises);
    }

    @DeleteMapping(value = "/exercise")
    public void deleteExercise(@RequestParam Long id) {
        if (exerciseRepository.existsById(id)) {
            exerciseRepository.deleteById(id);
        }
    }

}
