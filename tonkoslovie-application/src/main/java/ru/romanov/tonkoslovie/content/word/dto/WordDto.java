package ru.romanov.tonkoslovie.content.word.dto;

import lombok.Data;

@Data
public class WordDto {

    private long id;
    private String russianText;
    private String polishText;

}
