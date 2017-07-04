package ru.romanov.tonkoslovie.content.text;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextRepository extends JpaRepository<Text, Long> {

}
