package ru.romanov.tonkoslovie.content.exercise.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "exercise_id_generator")
    @SequenceGenerator(name = "exercise_id_generator", sequenceName = "exercise_id_sequence", allocationSize = 1)
    private long id;

    private String title;

    private ExerciseType type;

    private String original;

    @Type(type = "jsonb")
    private List<String> answers;

    private String answerRegex;

    private String dictionary;

    private boolean published;

}
