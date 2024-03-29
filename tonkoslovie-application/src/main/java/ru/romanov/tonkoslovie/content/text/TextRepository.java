package ru.romanov.tonkoslovie.content.text;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.content.text.entity.Text;

import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {

    List<Text> findByTitleContainingIgnoreCase(String title);

}
