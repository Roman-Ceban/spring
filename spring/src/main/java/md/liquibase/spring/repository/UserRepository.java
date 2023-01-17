package md.liquibase.spring.repository;


import md.liquibase.spring.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<Users, Integer> {

}
