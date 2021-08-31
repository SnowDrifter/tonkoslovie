package ru.romanov.tonkoslovie.user;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.romanov.tonkoslovie.user.entity.User;

@Repository
public interface UserRepository extends PagingAndSortingRepository<User, Long>, JpaSpecificationExecutor<User> {

    User getById(long id);

    User findFirstByEmail(String email);

    @Query("select count(u)>0 from User u where token = ?1")
    boolean existsByToken(String token);

    @Query("select count(u)>0 from User u where email = ?1")
    boolean existsByEmail(String email);

}
