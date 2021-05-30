package ru.romanov.tonkoslovie.content.text;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ru.romanov.tonkoslovie.hibernate.json.TextPartListJsonType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
@TypeDefs(value = {
        @TypeDef(name = "TextPartListJsonType", typeClass = TextPartListJsonType.class)
})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Text {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    @Type(type = "TextPartListJsonType")
    private List<TextPart> parts;

    private String soundFileName;

}
