package ru.romanov.tonkoslovie.content.lesson;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.content.lesson.dto.LessonDto;
import ru.romanov.tonkoslovie.content.lesson.dto.LessonMapper;
import ru.romanov.tonkoslovie.content.lesson.entity.Lesson;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;

    @Override
    @Cacheable(cacheNames = "lessonList", key = "#page + '-' + #size + '-' + #sortField", condition = "!#includeUnpublished")
    public RestPage<LessonDto> getLessons(int page, int size, boolean includeUnpublished, String sortField, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Lesson> lessons;
        if (includeUnpublished) {
            lessons = lessonRepository.findAll(pageable);
        } else {
            lessons = lessonRepository.findAllByPublishedTrue(pageable);
        }

        return RestPage.of(lessons.map(LessonMapper.INSTANCE::toDto));
    }

    @Override
    @Cacheable(value = "lesson", key = "#id", unless = "#result == null")
    public Optional<LessonDto> getLesson(long id) {
        return lessonRepository.findById(id)
                .map(LessonMapper.INSTANCE::toDto);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "lessonList", allEntries = true),
            @CacheEvict(value = "lesson", key = "#lessonDto.id")
    })
    public LessonDto save(LessonDto lessonDto) {
        Lesson lesson = LessonMapper.INSTANCE.toEntity(lessonDto);
        lesson = lessonRepository.save(lesson);
        return LessonMapper.INSTANCE.toDto(lesson);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "lessonList", allEntries = true),
            @CacheEvict(value = "lesson", key = "#id")
    })
    public void delete(long id) {
        lessonRepository.deleteById(id);
    }
}
