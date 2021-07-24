package ru.romanov.tonkoslovie.content.text.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.text.Text;
import ru.romanov.tonkoslovie.content.text.TextRepository;
import ru.romanov.tonkoslovie.content.text.dto.TextDto;
import ru.romanov.tonkoslovie.content.text.dto.TextMapper;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class TextController {

    private final TextRepository textRepository;

    @GetMapping("/texts")
    public List<TextDto> texts() {
        return TextMapper.INSTANCE.toDtoList(textRepository.findAllByOrderByTitleAsc());
    }

    @PostMapping(value = "/text")
    public TextDto saveText(@RequestBody TextDto textDto) {
        Text text = TextMapper.INSTANCE.toEntity(textDto);
        text = textRepository.save(text);
        return TextMapper.INSTANCE.toDto(text);
    }

    @GetMapping(value = "/text")
    public ResponseEntity<TextDto> getText(@RequestParam Long id) {
        Optional<Text> text = textRepository.findById(id);

        return text.map(TextMapper.INSTANCE::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/texts/find")
    public List<TextDto> findTexts(@RequestParam String title) {
        List<Text> texts = textRepository.findByTitleContainingIgnoreCase(title);
        return TextMapper.INSTANCE.toDtoList(texts);
    }

    @DeleteMapping(value = "/text")
    public void deleteText(@RequestParam Long id) {
        if (textRepository.existsById(id)) {
            textRepository.deleteById(id);
        }
    }

}
