package md.liquibase.spring.repository;

import md.liquibase.spring.model.Contacts;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContactRepository extends JpaRepository<Contacts, Integer> {
}
