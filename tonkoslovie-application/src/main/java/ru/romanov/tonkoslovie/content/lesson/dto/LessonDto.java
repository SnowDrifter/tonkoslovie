package ru.romanov.tonkoslovie.content.lesson.dto;

import lombok.Data;
import ru.romanov.tonkoslovie.content.text.dto.TextDto;

import java.io.Serializable;
import java.util.List;

@Data
public class LessonDto implements Serializable {

    private long id;
    private String title;
    private String annotation;
    private String content;
    private List<TextDto> texts;
    private String previewImage;
    private boolean published;

}
