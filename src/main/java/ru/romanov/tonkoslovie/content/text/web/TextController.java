package ru.romanov.tonkoslovie.content.text.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.text.Text;
import ru.romanov.tonkoslovie.content.text.TextRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/content")
public class TextController {

    private final TextRepository textRepository;

    @Autowired
    public TextController(TextRepository textRepository) {
        this.textRepository = textRepository;
    }

    @GetMapping("/texts")
    public List<Text> texts(){
        return new ArrayList<>(textRepository.findAll());
    }

    @PostMapping(value = "/text")
    public void saveText(@RequestBody Text text){
        textRepository.save(text);
    }

    @DeleteMapping(value = "/text")
    public void deleteText(@RequestParam Long id){
        if(textRepository.exists(id)){
            textRepository.delete(id);
        }
    }


}
