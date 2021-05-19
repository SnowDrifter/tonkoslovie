package ru.romanov.tonkoslovie.content.lesson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.romanov.tonkoslovie.content.text.Text;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Lesson {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    @Column(columnDefinition = "text")
    private String annotation;

    @Column(columnDefinition = "text")
    private String content;

    @OrderBy(value = "title")
    @ManyToMany(fetch = FetchType.EAGER)
    private List<Text> texts;

    private String previewImage;

    @Column(columnDefinition = "boolean default false")
    private boolean published;

}
