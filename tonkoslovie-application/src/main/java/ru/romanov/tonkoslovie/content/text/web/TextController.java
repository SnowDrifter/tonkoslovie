package ru.romanov.tonkoslovie.content.text.web;

import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.text.TextService;
import ru.romanov.tonkoslovie.content.text.dto.TextDto;
import ru.romanov.tonkoslovie.model.web.RestPage;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class TextController {

    private final TextService textService;

    @GetMapping("/texts")
    public RestPage<TextDto> texts(@RequestParam(defaultValue = "0") @Min(0) int page,
                                   @RequestParam(defaultValue = "10") @Range(min = 1, max = 100) int size,
                                   @RequestParam(defaultValue = "id") String sortField,
                                   @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc") String direction) {
        return textService.getTexts(page, size, sortField, direction);
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
