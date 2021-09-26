package ru.romanov.tonkoslovie.content.exercise.entity;


import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
@TypeDefs(value = {
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Exercise {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    private ExerciseType type;

    private String original;

    @Type(type = "jsonb")
    private List<String> answers;

    private String answerRegex;

    @Column(columnDefinition = "text")
    private String dictionary;

    @Column(columnDefinition = "boolean default false")
    private boolean published;

}
