package ru.romanov.tonkoslovie.content.theme;


import lombok.Data;
import ru.romanov.tonkoslovie.content.exercise.Exercise;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Theme {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Exercise> exercises;

    @Column(columnDefinition = "boolean default false")
    private boolean published;

}
