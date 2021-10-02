package ru.romanov.tonkoslovie.content.exercise;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.content.exercise.dto.ExerciseDto;
import ru.romanov.tonkoslovie.content.exercise.dto.ExerciseMapper;
import ru.romanov.tonkoslovie.content.exercise.entity.Exercise;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Override
    @Cacheable(cacheNames = "exerciseList", key = "#page + '-' + #size + '-' + #direction")
    public RestPage<ExerciseDto> getExercises(int page, int size, String sortField, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Exercise> exercises = exerciseRepository.findAll(pageable);
        return RestPage.of(exercises.map(ExerciseMapper.INSTANCE::toDto));
    }

    @Override
    @Cacheable(value = "exercise", key = "#id", unless = "#result == null")
    public Optional<ExerciseDto> getExercise(long id) {
        return exerciseRepository.findById(id)
                .map(ExerciseMapper.INSTANCE::toDto);
    }

    @Override
    public List<ExerciseDto> findByTitle(String title) {
        List<Exercise> exercises = exerciseRepository.findByTitleContainingIgnoreCase(title);
        return ExerciseMapper.INSTANCE.toDtoList(exercises);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "exerciseList", allEntries = true),
            @CacheEvict(value = "exercise", key = "#exerciseDto.id")
    })
    public ExerciseDto save(ExerciseDto exerciseDto) {
        Exercise exercise = ExerciseMapper.INSTANCE.toEntity(exerciseDto);
        exercise = exerciseRepository.save(exercise);
        return ExerciseMapper.INSTANCE.toDto(exercise);
    }

    @Override
    public Long findRandomId(List<Long> excludeIds) {
        if (excludeIds == null) {
            excludeIds = new ArrayList<>();
        }

        return exerciseRepository.findRandomExerciseIdExcludeIds(excludeIds);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "exerciseList", allEntries = true),
            @CacheEvict(value = "exercise", key = "#id")
    })
    public void delete(long id) {
        exerciseRepository.deleteById(id);
    }
}
