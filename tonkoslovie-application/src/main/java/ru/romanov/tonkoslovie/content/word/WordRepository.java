package ru.romanov.tonkoslovie.content.word;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WordRepository extends JpaRepository<Word, Long> {

    @Query(value = "select * from word order by random() limit ?1", nativeQuery = true)
    List<Word> getRandomWords(int limit);

    List<Word> findAllByOrderByIdAsc();
}
