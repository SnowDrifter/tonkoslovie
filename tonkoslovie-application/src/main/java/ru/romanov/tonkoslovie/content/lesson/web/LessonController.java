package ru.romanov.tonkoslovie.content.lesson.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.lesson.Lesson;
import ru.romanov.tonkoslovie.content.lesson.LessonRepository;
import ru.romanov.tonkoslovie.content.lesson.dto.LessonDto;
import ru.romanov.tonkoslovie.content.lesson.dto.LessonMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

import static ru.romanov.tonkoslovie.user.entity.Role.ROLE_ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class LessonController {

    private final LessonRepository lessonRepository;

    @GetMapping("/lessons")
    public List<LessonDto> lessons(@RequestParam(required = false, defaultValue = "false") boolean unpublished, HttpServletRequest request) {
        List<Lesson> lessons;
        if (unpublished && request.isUserInRole(ROLE_ADMIN.name())) {
            lessons = lessonRepository.findAllByOrderByTitleAsc();
        } else {
            lessons = lessonRepository.findByPublishedTrueOrderByTitleAsc();
        }

        return LessonMapper.INSTANCE.toDtoList(lessons);
    }

    @PostMapping(value = "/lesson")
    public LessonDto saveLesson(@RequestBody LessonDto lessonDto) {
        Lesson lesson = LessonMapper.INSTANCE.toEntity(lessonDto);
        lesson = lessonRepository.save(lesson);
        return LessonMapper.INSTANCE.toDto(lesson);
    }

    @GetMapping(value = "/lesson")
    public ResponseEntity<LessonDto> getLesson(@RequestParam Long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);

        return lesson.map(LessonMapper.INSTANCE::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping(value = "/lesson")
    public void deleteLesson(@RequestParam Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
        }
    }
}
