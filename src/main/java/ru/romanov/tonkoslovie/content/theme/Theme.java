package ru.romanov.tonkoslovie.content.theme;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Theme {

    @Id
    @GeneratedValue
    private long id;

    private String title;

}
