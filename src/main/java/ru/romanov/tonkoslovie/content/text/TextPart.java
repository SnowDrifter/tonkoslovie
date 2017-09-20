package ru.romanov.tonkoslovie.content.text;

import lombok.Data;
import ru.romanov.tonkoslovie.content.text.web.ChoiceVariant;

import java.util.List;

@Data
public class TextPart {

    private TextPartType type;
    private String data;
    private String placeholder;
    private List<ChoiceVariant> choiceVariants;

}
