package ru.romanov.tonkoslovie.content.text;

import lombok.Data;

import java.util.List;

@Data
public class TextPart {

    private TextPartType type;
    private String data;
    private String placeholder;
    private List<ChoiceVariant> choiceVariants;

}
