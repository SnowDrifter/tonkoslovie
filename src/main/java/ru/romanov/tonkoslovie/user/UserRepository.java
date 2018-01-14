package ru.romanov.tonkoslovie.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByUsername(String username);

    User findFirstByEmail(String email);

    Long countByUsername(String username);

    @Query("select count(u)>0 from User u where token = ?1")
    boolean existsByToken(String token);

    @Query("select count(u)>0 from User u where username = ?1")
    boolean existsByUsername(String username);

    @Query("select count(u)>0 from User u where email = ?1")
    boolean existsByEmail(String email);

}
