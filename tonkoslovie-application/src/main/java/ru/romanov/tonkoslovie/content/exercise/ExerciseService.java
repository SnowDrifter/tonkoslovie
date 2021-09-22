package ru.romanov.tonkoslovie.content.exercise;

import ru.romanov.tonkoslovie.content.exercise.dto.ExerciseDto;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import java.util.List;
import java.util.Optional;

public interface ExerciseService {

    RestPage<ExerciseDto> getExercises(int page, int size);

    Optional<ExerciseDto> getExercise(long id);

    List<ExerciseDto> findByTitle(String title);

    ExerciseDto save(ExerciseDto lesson);

    Long findRandomId(List<Long> excludeIds);

    void delete(long id);

}
