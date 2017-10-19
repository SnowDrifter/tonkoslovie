package ru.romanov.tonkoslovie.content.theme;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import ru.romanov.tonkoslovie.content.exercise.Exercise;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Theme {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Exercise> exercises;

}
