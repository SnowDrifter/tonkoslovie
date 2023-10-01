package ru.romanov.tonkoslovie.audit.dto;


import org.mapstruct.*;
import org.mapstruct.factory.Mappers;
import ru.romanov.tonkoslovie.audit.elastic.Audit;

@Mapper
public interface AuditMapper {

    AuditMapper INSTANCE = Mappers.getMapper(AuditMapper.class);

    @Mappings({
            @Mapping(target = "operation", expression = "java(AuditOperation.fromCode(audit.getOp()))"),
            @Mapping(target = "table", expression = "java(audit.getSource().getTable())"),
            @Mapping(target = "createdAt", expression = "java(audit.getSource().getCreatedAt())"),
            @Mapping(target = "entityId", expression = "java(audit.findEntityId())"),
    })
    AuditDto toDto(Audit audit);

}
