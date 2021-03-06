package ru.romanov.tonkoslovie.content.text;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import ru.romanov.tonkoslovie.utils.TextPartJsonType;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

@Data
@Entity
@TypeDefs(value = {
        @TypeDef(name = "TextPartJsonType", typeClass = TextPartJsonType.class)
})
@JsonIgnoreProperties(value = {"hibernateLazyInitializer", "handler"})
public class Text {

    @Id
    @GeneratedValue
    private long id;

    private String title;

    @Type(type = "TextPartJsonType")
    private List<TextPart> parts;

    private String soundFileName;

}
