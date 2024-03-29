package ru.romanov.tonkoslovie.content.word;

import ru.romanov.tonkoslovie.content.word.dto.WordDto;
import ru.romanov.tonkoslovie.model.web.RestPage;

import java.util.List;
import java.util.Optional;

public interface WordService {

    RestPage<WordDto> getWords(int page, int size, String sortField, String direction);

    Optional<WordDto> getWord(long id);

    List<WordDto> getRandomWords(int size);

    WordDto save(WordDto wordDto);

    void delete(long id);
    
}
