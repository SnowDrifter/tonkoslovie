package ru.romanov.tonkoslovie.content.lesson;

import ru.romanov.tonkoslovie.content.lesson.dto.LessonDto;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import java.util.Optional;

public interface LessonService {

    RestPage<LessonDto> getLessons(int page, int size, boolean includeUnpublished, String sortField);

    Optional<LessonDto> getLesson(long id);

    LessonDto save(LessonDto lesson);

    void delete(long id);

}
