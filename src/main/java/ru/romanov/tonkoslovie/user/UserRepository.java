package ru.romanov.tonkoslovie.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByUsername(String username);

    Long countByUsername(String username);

}
