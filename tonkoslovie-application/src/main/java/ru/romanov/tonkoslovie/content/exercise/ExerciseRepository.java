package ru.romanov.tonkoslovie.content.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.content.exercise.entity.Exercise;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query(value = "select id from exercise where ((?1) is null or id not in (?1)) order by random() limit 1", nativeQuery = true)
    Long findRandomExerciseIdExcludeIds(List<Long> excludeIds);

    List<Exercise> findByTitleContainingIgnoreCase(String title);

}
