package ru.romanov.tonkoslovie.hibernate;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
public class RestPage<T> {

    private List<T> content;
    private int size;
    private long number;
    private int totalPages;
    private long totalElements;
    private boolean last;

    public RestPage(Page<T> page) {
        this.content = page.getContent();
        this.size = page.getSize();
        this.number = page.getNumber();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.last = page.isLast();
    }

    public static <T> RestPage<T> of(Page<T> page) {
        return new RestPage<>(page);
    }

}
