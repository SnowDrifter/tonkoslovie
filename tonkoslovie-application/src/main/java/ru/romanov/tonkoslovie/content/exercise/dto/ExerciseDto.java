package ru.romanov.tonkoslovie.content.exercise.dto;


import lombok.Data;
import ru.romanov.tonkoslovie.content.exercise.ExerciseType;

import java.util.List;

@Data
public class ExerciseDto {

    private long id;
    private String title;
    private ExerciseType type;
    private String original;
    private List<String> answers;
    private String answerRegex;
    private String dictionary;
    private boolean published;

}
