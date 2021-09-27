package ru.romanov.tonkoslovie.content.text.dto;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import ru.romanov.tonkoslovie.content.text.entity.Text;

import java.util.List;

@Mapper
public interface TextMapper {

    TextMapper INSTANCE = Mappers.getMapper(TextMapper.class);

    Text toEntity(TextDto textDto);

    TextDto toDto(Text text);

    List<TextDto> toDtoList(List<Text> texts);

}
