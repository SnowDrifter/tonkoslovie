package ru.romanov.tonkoslovie.content.theme.entity;


import lombok.Data;
import ru.romanov.tonkoslovie.content.exercise.entity.Exercise;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
public class Theme {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "theme_id_generator")
    @SequenceGenerator(name = "theme_id_generator", sequenceName = "theme_id_sequence", allocationSize = 1)
    private long id;

    private String title;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Exercise> exercises;

    private boolean published;

}
