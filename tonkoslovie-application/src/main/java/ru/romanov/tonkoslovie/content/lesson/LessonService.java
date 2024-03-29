package ru.romanov.tonkoslovie.content.lesson;

import ru.romanov.tonkoslovie.content.lesson.dto.LessonDto;
import ru.romanov.tonkoslovie.model.web.RestPage;

import java.util.Optional;

public interface LessonService {

    RestPage<LessonDto> getLessons(int page, int size, boolean includeUnpublished, String sortField, String direction);

    Optional<LessonDto> getLesson(long id);

    LessonDto save(LessonDto lesson);

    void delete(long id);

}
