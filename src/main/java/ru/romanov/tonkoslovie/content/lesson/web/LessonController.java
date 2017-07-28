package ru.romanov.tonkoslovie.content.lesson.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.lesson.Lesson;
import ru.romanov.tonkoslovie.content.lesson.LessonRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class LessonController {

    private final LessonRepository lessonRepository;

    @Autowired
    public LessonController(LessonRepository lessonRepository) {
        this.lessonRepository = lessonRepository;
    }

    @GetMapping("/lessons")
    public List<Lesson> lessons() {
        return new ArrayList<>(lessonRepository.findAll());
    }

    @PostMapping(value = "/lesson")
    public Lesson saveLesson(@RequestBody Lesson lesson) {
        return lessonRepository.save(lesson);
    }

    @GetMapping(value = "/lesson")
    public Lesson getLesson(@RequestParam Long id) {
        return lessonRepository.getOne(id);
    }

    @DeleteMapping(value = "/lesson")
    public void deleteLesson(@RequestParam Long id) {
        if (lessonRepository.exists(id)) {
            lessonRepository.delete(id);
        }
    }
}
