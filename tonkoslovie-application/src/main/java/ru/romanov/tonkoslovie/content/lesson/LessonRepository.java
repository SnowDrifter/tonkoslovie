package ru.romanov.tonkoslovie.content.lesson;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LessonRepository extends PagingAndSortingRepository<Lesson, Long> {

    Page<Lesson> findAllByPublishedTrue(Pageable pageable);

}
