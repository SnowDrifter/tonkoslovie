package ru.romanov.tonkoslovie.content.text.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@TypeDefs(value = {
        @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
})
public class Text {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "text_id_generator")
    @SequenceGenerator(name = "text_id_generator", sequenceName = "text_id_sequence", allocationSize = 1)
    private long id;

    private String title;

    @Type(type = "jsonb")
    private List<TextPart> parts;

    private String soundFileName;

}
