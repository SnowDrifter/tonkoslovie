package ru.romanov.tonkoslovie.content.text;

import ru.romanov.tonkoslovie.content.text.dto.TextDto;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import java.util.List;
import java.util.Optional;

public interface TextService {

    RestPage<TextDto> getTexts(int page, int size, String sortField, String direction);

    Optional<TextDto> getText(long id);

    List<TextDto> findTexts(String title);

    TextDto save(TextDto textDto);

    void delete(long id);
    
}
