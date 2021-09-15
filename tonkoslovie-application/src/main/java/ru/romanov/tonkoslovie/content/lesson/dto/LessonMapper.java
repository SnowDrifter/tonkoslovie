package ru.romanov.tonkoslovie.content.lesson.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.romanov.tonkoslovie.content.lesson.entity.Lesson;
import ru.romanov.tonkoslovie.content.text.dto.TextMapper;

import java.util.List;

@Mapper(uses = TextMapper.class)
public interface LessonMapper {

    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    Lesson toEntity(LessonDto lessonDto);

    LessonDto toDto(Lesson lesson);

    List<LessonDto> toDtoList(List<Lesson> lessons);

}
