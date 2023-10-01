package ru.romanov.tonkoslovie.content.exercise.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_id_generator")
    @SequenceGenerator(name = "exercise_id_generator", sequenceName = "exercise_id_sequence", allocationSize = 1)
    private long id;

    private String title;

    @Column(columnDefinition = "int4")
    private ExerciseType type;

    private String original;

    @Type(JsonBinaryType.class)
    private List<String> answers;

    private String answerRegex;

    private String dictionary;

    private boolean published;

}
