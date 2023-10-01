package ru.romanov.tonkoslovie.audit.elastic;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.Map;

@Data
@Document(indexName = "debezium.audit")
public class Audit {

    @Id
    private String id;
    private Map<String, String> before;
    private Map<String, String> after;
    private String op;
    @Field(type = FieldType.Object)
    private AuditSource source;

    public String findEntityId() {
        if (before != null && before.containsKey("id")) {
            return before.get("id");
        } else if (after != null && after.containsKey("id")) {
            return after.get("id");
        }

        return null;
    }

}
