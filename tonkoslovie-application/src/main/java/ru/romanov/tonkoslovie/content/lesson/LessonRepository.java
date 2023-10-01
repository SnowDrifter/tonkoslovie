package ru.romanov.tonkoslovie.content.lesson;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.content.lesson.entity.Lesson;


@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {

    Page<Lesson> findAllByPublishedTrue(Pageable pageable);

}
