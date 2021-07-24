package ru.romanov.tonkoslovie.content.text.dto;

import lombok.Data;
import ru.romanov.tonkoslovie.content.text.TextPart;

import java.util.List;

@Data
public class TextDto {

    private long id;
    private String title;
    private List<TextPart> parts;
    private String soundFileName;

}
