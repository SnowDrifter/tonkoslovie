package ru.romanov.tonkoslovie.content.word;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository  extends JpaRepository<Word, Long> {
}
