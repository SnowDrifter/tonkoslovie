package ru.romanov.tonkoslovie.content.text;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {

//    @Query("Select t from Text t where t.title like %?1%")
    List<Text> findByTitleContainingIgnoreCase(String title);

}
