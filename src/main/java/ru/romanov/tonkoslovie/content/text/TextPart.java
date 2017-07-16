package ru.romanov.tonkoslovie.content.text;

import lombok.Data;

@Data
public class TextPart {

    private TextPartType type;
    private String data;
    private String placeholder;

}
