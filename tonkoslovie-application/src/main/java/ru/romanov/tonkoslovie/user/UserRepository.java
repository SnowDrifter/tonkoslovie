package ru.romanov.tonkoslovie.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.user.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    User getById(long id);

    User findFirstByEmail(String email);

    @Query("select count(u)>0 from User u where u.email = ?1")
    boolean existsByEmail(String email);

}
