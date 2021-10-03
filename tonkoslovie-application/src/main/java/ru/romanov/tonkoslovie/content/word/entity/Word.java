package ru.romanov.tonkoslovie.content.word.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class Word {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "word_id_generator")
    @SequenceGenerator(name = "word_id_generator", sequenceName = "word_id_sequence", allocationSize = 1)
    private long id;

    private String russianText;

    private String polishText;

}
