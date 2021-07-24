package ru.romanov.tonkoslovie.content.word.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.romanov.tonkoslovie.content.word.Word;

import java.util.List;

@Mapper
public interface WordMapper {

    WordMapper INSTANCE = Mappers.getMapper(WordMapper.class);

    Word toEntity(WordDto wordDto);

    WordDto toDto(Word word);

    List<WordDto> toDtoList(List<Word> words);

}
