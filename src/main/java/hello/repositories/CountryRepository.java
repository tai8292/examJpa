package hello.repositories;

import hello.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryRepository extends JpaRepository<Country, Long> {

    @Query("select distinct c from Company cp join cp.city ct join ct.country c " +
            "where cp.businessLicense like %:business% order by c.id")
    List<Country> findByBusinessCompany(@Param("business") String business);
}
