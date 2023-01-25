package md.liquibase.spring.repository;

import md.liquibase.spring.model.Geo;
import org.springframework.data.jpa.repository.JpaRepository;


public interface GeoRepository extends JpaRepository<Geo, Long> {

}
