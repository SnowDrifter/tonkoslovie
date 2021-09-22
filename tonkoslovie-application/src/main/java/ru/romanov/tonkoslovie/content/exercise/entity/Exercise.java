package ru.romanov.tonkoslovie.content.exercise.entity;


import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ru.romanov.tonkoslovie.hibernate.json.StringListJsonType;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@TypeDefs(value = {
        @TypeDef(name = "StringListJsonType", typeClass = StringListJsonType.class)
})
public class Exercise {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    private ExerciseType type;

    private String original;

    @Type(type = "StringListJsonType")
    private List<String> answers;

    private String answerRegex;

    @Column(columnDefinition = "text")
    private String dictionary;

    @Column(columnDefinition = "boolean default false")
    private boolean published;

}
