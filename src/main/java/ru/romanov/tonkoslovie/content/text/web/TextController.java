package ru.romanov.tonkoslovie.content.text.web;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.text.Text;
import ru.romanov.tonkoslovie.content.text.TextRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/content")
public class TextController {

    private final TextRepository textRepository;

    @GetMapping("/texts")
    public List<Text> texts() {
        return new ArrayList<>(textRepository.findAllByOrderByIdAsc());
    }

    @PostMapping(value = "/text")
    public Text saveText(@RequestBody Text text) {
        return textRepository.save(text);
    }

    @GetMapping(value = "/text")
    public ResponseEntity<Text> getText(@RequestParam Long id) {
        Optional<Text> text = textRepository.findById(id);

        return text.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping(value = "/texts/findByTitle")
    public List<Text> findTextsByTitle(@RequestParam String title) {
        return textRepository.findByTitleContainingIgnoreCase(title);
    }

    @DeleteMapping(value = "/text")
    public void deleteText(@RequestParam Long id) {
        if (textRepository.existsById(id)) {
            textRepository.deleteById(id);
        }
    }

}
