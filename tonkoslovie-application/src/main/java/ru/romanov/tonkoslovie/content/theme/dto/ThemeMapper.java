package ru.romanov.tonkoslovie.content.theme.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.romanov.tonkoslovie.content.exercise.dto.ExerciseMapper;
import ru.romanov.tonkoslovie.content.theme.entity.Theme;

import java.util.List;

@Mapper(uses = ExerciseMapper.class)
public interface ThemeMapper {

    ThemeMapper INSTANCE = Mappers.getMapper(ThemeMapper.class);

    Theme toEntity(ThemeDto themeDto);

    ThemeDto toDto(Theme theme);

    List<ThemeDto> toDtoList(List<Theme> themes);

}
