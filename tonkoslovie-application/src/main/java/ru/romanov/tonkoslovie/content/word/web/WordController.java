package ru.romanov.tonkoslovie.content.word.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.word.Word;
import ru.romanov.tonkoslovie.content.word.WordRepository;
import ru.romanov.tonkoslovie.content.word.dto.WordDto;
import ru.romanov.tonkoslovie.content.word.dto.WordMapper;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class WordController {

    private final WordRepository wordRepository;

    @GetMapping("/words")
    public List<WordDto> words() {
        List<Word> words = wordRepository.findAllByOrderByIdAsc();
        return WordMapper.INSTANCE.toDtoList(words);
    }

    @PostMapping(value = "/word")
    public WordDto saveWord(@RequestBody WordDto wordDto) {
        Word word = WordMapper.INSTANCE.toEntity(wordDto);
        word = wordRepository.save(word);
        return WordMapper.INSTANCE.toDto(word);
    }

    @DeleteMapping(value = "/word")
    public void deleteWord(@RequestParam Long id) {
        if (wordRepository.existsById(id)) {
            wordRepository.deleteById(id);
        }
    }

    @GetMapping("/random")
    public List<WordDto> getRandomWords(@RequestParam Integer limit) {
        List<Word> words = wordRepository.getRandomWords(limit);
        return WordMapper.INSTANCE.toDtoList(words);
    }

}
