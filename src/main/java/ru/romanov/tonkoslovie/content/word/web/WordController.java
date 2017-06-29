package ru.romanov.tonkoslovie.content.word.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.romanov.tonkoslovie.content.word.Word;
import ru.romanov.tonkoslovie.content.word.WordRepository;

import java.util.List;

@RestController
@RequestMapping("/api/content")
public class WordController {

    private final WordRepository wordRepository;

    @Autowired
    public WordController(WordRepository wordRepository) {
        this.wordRepository = wordRepository;
    }

    @RequestMapping("/words")
    public List<Word> words(){
        return wordRepository.findAll();
    }

    @RequestMapping(value = "/word", method = RequestMethod.POST)
    public void saveWord(@RequestBody Word word){
        wordRepository.save(word);
    }

    @RequestMapping(value = "/word", method = RequestMethod.DELETE)
    public void deleteWord(@RequestParam Long id){
        if(wordRepository.exists(id)){
            wordRepository.delete(id);
        }
    }

}
