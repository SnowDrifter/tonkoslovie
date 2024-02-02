package ru.romanov.tonkoslovie.content.word.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "word_id_generator")
    @SequenceGenerator(name = "word_id_generator", sequenceName = "word_id_sequence", allocationSize = 1)
    private long id;

    private String russianText;

    private String polishText;

}
