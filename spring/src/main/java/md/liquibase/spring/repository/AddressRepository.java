package md.liquibase.spring.repository;


import md.liquibase.spring.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AddressRepository extends JpaRepository<Address, Long> {

}
