package ru.romanov.tonkoslovie.content.lesson.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.lesson.LessonService;
import ru.romanov.tonkoslovie.content.lesson.dto.LessonDto;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import static ru.romanov.tonkoslovie.user.entity.Role.ROLE_ADMIN;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class LessonController {

    private final LessonService lessonService;

    @GetMapping("/lessons")
    public RestPage<LessonDto> lessons(@RequestParam(defaultValue = "0") @Min(0) int page,
                                       @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                       @RequestParam(required = false, defaultValue = "false") boolean unpublished,
                                       @RequestParam(required = false, defaultValue = "title") String sortField,
                                       HttpServletRequest request) {
        boolean includeUnpublished = unpublished && request.isUserInRole(ROLE_ADMIN.name());
        return lessonService.getLessons(page, size, includeUnpublished, sortField);
    }

    @PostMapping(value = "/lesson")
    public LessonDto saveLesson(@RequestBody LessonDto lessonDto) {
        return lessonService.save(lessonDto);
    }

    @GetMapping(value = "/lesson")
    public ResponseEntity<LessonDto> getLesson(@RequestParam Long id) {
        return lessonService.getLesson(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping(value = "/lesson")
    public void deleteLesson(@RequestParam Long id) {
        lessonService.delete(id);
    }
}
