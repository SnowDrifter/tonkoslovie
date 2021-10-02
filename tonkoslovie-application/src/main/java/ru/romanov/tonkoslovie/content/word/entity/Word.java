package ru.romanov.tonkoslovie.content.word.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
public class Word {

    @Id
    @GeneratedValue
    private long id;

    private String russianText;

    private String polishText;

}
