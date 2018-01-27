package ru.romanov.tonkoslovie.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.user.entity.User;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByOrderByIdAsc();

    User findFirstByEmail(String email);

    @Query("select count(u)>0 from User u where token = ?1")
    boolean existsByToken(String token);

    @Query("select count(u)>0 from User u where email = ?1")
    boolean existsByEmail(String email);

}
