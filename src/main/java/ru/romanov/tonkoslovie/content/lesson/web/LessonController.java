package ru.romanov.tonkoslovie.content.lesson.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.lesson.Lesson;
import ru.romanov.tonkoslovie.content.lesson.LessonRepository;

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
    public List<Lesson> lessons(@RequestParam(required = false, defaultValue = "false") boolean unpublished, HttpServletRequest request) {
        if (unpublished && request.isUserInRole(ROLE_ADMIN.name())) {
            return lessonRepository.findAllByOrderByTitleAsc();
        } else {
            return lessonRepository.findByPublishedTrueOrderByTitleAsc();
        }
    }

    @PostMapping(value = "/lesson")
    public Lesson saveLesson(@RequestBody Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @GetMapping(value = "/lesson")
    public ResponseEntity<Lesson> getLesson(@RequestParam Long id) {
        Optional<Lesson> lesson = lessonRepository.findById(id);

        return lesson.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @DeleteMapping(value = "/lesson")
    public void deleteLesson(@RequestParam Long id) {
        if (lessonRepository.existsById(id)) {
            lessonRepository.deleteById(id);
        }
    }
}
