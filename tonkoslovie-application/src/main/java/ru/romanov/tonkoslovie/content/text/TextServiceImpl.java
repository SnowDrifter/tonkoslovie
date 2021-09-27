package ru.romanov.tonkoslovie.content.text;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.romanov.tonkoslovie.content.text.dto.TextDto;
import ru.romanov.tonkoslovie.content.text.dto.TextMapper;
import ru.romanov.tonkoslovie.content.text.entity.Text;
import ru.romanov.tonkoslovie.hibernate.RestPage;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TextServiceImpl implements TextService {

    private final TextRepository textRepository;

    @Override
    @Cacheable(cacheNames = "textList", key = "#page + '-' + #size + '-' + #sortField")
    public RestPage<TextDto> getTexts(int page, int size, String sortField) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortField).ascending());
        Page<Text> texts = textRepository.findAll(pageable);
        return RestPage.of(texts.map(TextMapper.INSTANCE::toDto));
    }

    @Override
    @Cacheable(value = "text", key = "#id", unless = "#result == null")
    public Optional<TextDto> getText(long id) {
        return textRepository.findById(id)
                .map(TextMapper.INSTANCE::toDto);
    }

    @Override
    public List<TextDto> findTexts(String title) {
        List<Text> texts = textRepository.findByTitleContainingIgnoreCase(title);
        return TextMapper.INSTANCE.toDtoList(texts);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "textList", allEntries = true),
            @CacheEvict(value = "text", key = "#textDto.id")
    })
    public TextDto save(TextDto textDto) {
        Text text = TextMapper.INSTANCE.toEntity(textDto);
        text = textRepository.save(text);
        return TextMapper.INSTANCE.toDto(text);
    }

    @Override
    @Caching(evict = {
            @CacheEvict(value = "textList", allEntries = true),
            @CacheEvict(value = "text", key = "#id")
    })
    public void delete(long id) {
        textRepository.deleteById(id);
    }
}
