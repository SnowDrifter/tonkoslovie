package ru.romanov.tonkoslovie.content.word.web;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.word.WordService;
import ru.romanov.tonkoslovie.content.word.dto.WordDto;
import ru.romanov.tonkoslovie.model.web.RestPage;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.util.List;

@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class WordController {

    private final WordService wordService;

    @GetMapping("/words")
    public RestPage<WordDto> words(@RequestParam(defaultValue = "0") @Min(0) int page,
                                   @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                   @RequestParam(defaultValue = "id") String sortField,
                                   @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc") String direction) {
        return wordService.getWords(page, size, sortField, direction);
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
