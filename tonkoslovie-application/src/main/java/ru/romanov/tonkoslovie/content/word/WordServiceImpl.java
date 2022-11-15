package ru.romanov.tonkoslovie.content.word;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.content.word.dto.WordDto;
import ru.romanov.tonkoslovie.content.word.dto.WordMapper;
import ru.romanov.tonkoslovie.content.word.entity.Word;
import ru.romanov.tonkoslovie.model.web.RestPage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WordServiceImpl implements WordService {

    private final WordRepository wordRepository;

    @Override
    @Cacheable(cacheNames = "wordList", key = "#page + '-' + #size + '-' + #sortField + '-' + #direction")
    public RestPage<WordDto> getWords(int page, int size, String sortField, String direction) {
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Word> exercises = wordRepository.findAll(pageable);
        return RestPage.of(exercises.map(WordMapper.INSTANCE::toDto));
    }

    @Override
    @Cacheable(value = "word", key = "#id", unless = "#result == null")
    public Optional<WordDto> getWord(long id) {
        return wordRepository.findById(id)
                .map(WordMapper.INSTANCE::toDto);
    }

    @Override
    public List<WordDto> getRandomWords(int size) {
        List<Word> words = wordRepository.getRandomWords(size);
        return WordMapper.INSTANCE.toDtoList(words);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "wordList", allEntries = true),
            @CacheEvict(value = "word", key = "#wordDto.id")
    })
    public WordDto save(WordDto wordDto) {
        Word word = WordMapper.INSTANCE.toEntity(wordDto);
        word = wordRepository.save(word);
        return WordMapper.INSTANCE.toDto(word);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "wordList", allEntries = true),
            @CacheEvict(value = "word", key = "#id")
    })
    public void delete(long id) {
        wordRepository.deleteById(id);
    }
}
