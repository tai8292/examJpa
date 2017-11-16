package hello.repositories;

import hello.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long>{

    @Query("select c from Company c LEFT JOIN c.city ct where ct.id =:city_ ")
    public List<Company> findByCityId(@Param("city_id")Long city_id);id
}
