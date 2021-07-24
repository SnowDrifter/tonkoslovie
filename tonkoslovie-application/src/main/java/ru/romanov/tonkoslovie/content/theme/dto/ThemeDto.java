package ru.romanov.tonkoslovie.content.theme.dto;

import lombok.Data;
import ru.romanov.tonkoslovie.content.exercise.dto.ExerciseDto;

import java.util.List;

@Data
public class ThemeDto {

    private long id;
    private String title;
    private List<ExerciseDto> exercises;
    private boolean published;

}
