package ru.romanov.tonkoslovie.content.text.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.text.TextService;
import ru.romanov.tonkoslovie.content.text.dto.TextDto;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class TextController {

    private final TextService textService;

    @GetMapping("/texts")
    public RestPage<TextDto> texts(@RequestParam(defaultValue = "0") @Min(0) int page,
                                   @RequestParam(defaultValue = "10") @Min(1) @Max(100) int size,
                                   @RequestParam(required = false, defaultValue = "title") String sortField) {
        return textService.getTexts(page, size, sortField);
    }

    @GetMapping(value = "/text")
    public ResponseEntity<TextDto> getText(@RequestParam Long id) {
        return textService.getText(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping(value = "/text")
    public TextDto saveText(@RequestBody TextDto textDto) {
        return textService.save(textDto);
    }

    @GetMapping(value = "/texts/find")
    public List<TextDto> findTexts(@RequestParam String title) {
        return textService.findTexts(title);
    }

    @DeleteMapping(value = "/text")
    public void deleteText(@RequestParam Long id) {
        textService.delete(id);
    }

}
