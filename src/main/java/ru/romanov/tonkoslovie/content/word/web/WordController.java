package ru.romanov.tonkoslovie.content.word.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.word.Word;
import ru.romanov.tonkoslovie.content.word.WordRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class WordController {

    private final WordRepository wordRepository;

    @GetMapping("/words")
    public List<Word> words() {
        return new ArrayList<>(wordRepository.findAllByOrderByIdAsc());
    }

    @PostMapping(value = "/word")
    public Word saveWord(@RequestBody Word word) {
        return wordRepository.save(word);
    }

    @DeleteMapping(value = "/word")
    public void deleteWord(@RequestParam Long id) {
        if (wordRepository.existsById(id)) {
            wordRepository.deleteById(id);
        }
    }

    @GetMapping("/random")
    public List<Word> getRandomWords(@RequestParam Integer limit) {
        return wordRepository.getRandomWords(limit);
    }

}
