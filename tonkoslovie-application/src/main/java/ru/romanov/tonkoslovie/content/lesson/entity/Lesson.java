package ru.romanov.tonkoslovie.content.lesson.entity;

import lombok.Getter;
import lombok.Setter;
import ru.romanov.tonkoslovie.content.text.entity.Text;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Getter
@Setter
public class Lesson {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "lesson_id_generator")
    @SequenceGenerator(name = "lesson_id_generator", sequenceName = "lesson_id_sequence", allocationSize = 1)
    private long id;

    private String title;

    private String annotation;

    private String content;

    @OrderBy(value = "title")
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Text> texts;

    private String previewImage;

    private boolean published;

}
