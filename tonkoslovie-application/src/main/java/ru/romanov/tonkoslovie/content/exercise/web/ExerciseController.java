package ru.romanov.tonkoslovie.content.exercise.web;

import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.exercise.ExerciseService;
import ru.romanov.tonkoslovie.content.exercise.dto.ExerciseDto;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping("/exercises")
    public RestPage<ExerciseDto> exercises(@RequestParam(defaultValue = "0") @Min(0) int page,
                                           @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                           @RequestParam(defaultValue = "id") String sortField,
                                           @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc") String direction) {
        return exerciseService.getExercises(page, size, sortField, direction);
    }

    @GetMapping(value = "/exercise")
    public ResponseEntity<ExerciseDto> getExercise(@RequestParam Long id) {
        return exerciseService.getExercise(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/exercises/find")
    public List<ExerciseDto> find(@RequestParam String title) {
        return exerciseService.findByTitle(title);
    }

    @GetMapping(value = "/exercise/random_id")
    public Map<String, Object> getRandomExerciseId(@RequestParam(required = false) List<Long> excludeIds) {
        Long id = exerciseService.findRandomId(excludeIds);
        return ImmutableMap.of("id", id);
    }

    @PostMapping(value = "/exercise")
    public ExerciseDto saveExercise(@RequestBody ExerciseDto exerciseDto) {
        return exerciseService.save(exerciseDto);
    }

    @DeleteMapping(value = "/exercise")
    public void deleteExercise(@RequestParam Long id) {
        exerciseService.delete(id);
    }

}
