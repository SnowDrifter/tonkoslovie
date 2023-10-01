package ru.romanov.tonkoslovie.content.text.entity;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import org.hibernate.annotations.Type;

import jakarta.persistence.*;
import java.util.List;

@Data
@Entity
public class Text {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "text_id_generator")
    @SequenceGenerator(name = "text_id_generator", sequenceName = "text_id_sequence", allocationSize = 1)
    private long id;

    private String title;

    @Type(JsonBinaryType.class)
    private List<TextPart> parts;

    private String soundFileName;

}
