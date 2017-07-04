package ru.romanov.tonkoslovie.content.text;

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
public class Text {

    @Id
    @GeneratedValue
    private long id;
    @Type(type = "TextPartJsonType")
    private List<TextPart> parts;

}
