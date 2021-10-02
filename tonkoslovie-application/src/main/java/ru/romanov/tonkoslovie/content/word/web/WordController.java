package ru.romanov.tonkoslovie.content.word.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.word.WordService;
import ru.romanov.tonkoslovie.content.word.dto.WordDto;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class WordController {

    private final WordService wordService;

    @GetMapping("/words")
    public RestPage<WordDto> words(@RequestParam(defaultValue = "0") @Min(0) int page,
                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                   @RequestParam(required = false, defaultValue = "id") String sortField) {
        return wordService.getWords(page, size, sortField);
    }

    @PostMapping(value = "/word")
    public WordDto saveWord(@RequestBody WordDto wordDto) {
        return wordService.save(wordDto);
    }

    @DeleteMapping(value = "/word")
    public void deleteWord(@RequestParam Long id) {
        wordService.delete(id);
    }

    @GetMapping("/random")
    public List<WordDto> getRandomWords(@RequestParam @Min(1) @Max(100) Integer size) {
        return wordService.getRandomWords(size);
    }

}
