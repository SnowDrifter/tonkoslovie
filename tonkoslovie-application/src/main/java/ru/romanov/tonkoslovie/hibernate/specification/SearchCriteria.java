package ru.romanov.tonkoslovie.hibernate.specification;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCriteria {

    private String key;
    private String operation;
    private String value;

}
