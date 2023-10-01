package ru.romanov.tonkoslovie.audit.elastic;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;

@Data
public class AuditSource {

    private String table;
    @Field(name = "ts_ms", type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date createdAt;

}
