package ru.romanov.tonkoslovie.content.exercise.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.romanov.tonkoslovie.content.exercise.Exercise;
import ru.romanov.tonkoslovie.content.text.dto.TextMapper;

import java.util.List;

@Mapper(uses = TextMapper.class)
public interface ExerciseMapper {

    ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);

    Exercise toEntity(ExerciseDto exerciseDto);

    ExerciseDto toDto(Exercise exercise);

    List<ExerciseDto> toDtoList(List<Exercise> exercises);

}
