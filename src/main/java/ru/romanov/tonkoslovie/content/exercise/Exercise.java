package ru.romanov.tonkoslovie.content.exercise;


import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Exercise {

    @Id
    @GeneratedValue
    private long id;

    private ExerciseType type;

    private String original;

    @ElementCollection
    private List<String> answers;

    @Column(columnDefinition = "text")
    private String dictionary;

}
