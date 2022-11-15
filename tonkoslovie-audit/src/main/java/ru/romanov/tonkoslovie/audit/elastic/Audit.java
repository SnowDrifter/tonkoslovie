package ru.romanov.tonkoslovie.audit.elastic;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Date;
import java.util.Map;

@Data
@Document(indexName = "debezium.audit")
public class Audit {

    @Id
    private String id;
    @Field("source.table")
    private String table;
    private Map<String, String> before;
    private Map<String, String> after;
    private String op;
    @Field(name = "source.ts_ms", type = FieldType.Date, format = DateFormat.epoch_millis)
    private Date createdAt;

    public String findEntityId() {
        if (before != null && before.containsKey("id")) {
            return before.get("id");
        } else if (after != null && after.containsKey("id")) {
            return after.get("id");
        }

        return null;
    }

}
