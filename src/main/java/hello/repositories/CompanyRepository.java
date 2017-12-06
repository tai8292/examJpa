package hello.repositories;

import hello.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long>{

    @Query("select c from Company c where c.city.id=:city_id ")
    List<Company> findByCityId(@Param("city_id")Long city_id);

    List<Company> findByNameContainsOrBusinessLicenseContains(String name, String businessLicense);

    @Query("select c from Company c join c.city ct join ct.country ctr where ctr.name =:countryName")
    List<Company> findByCountryName(@Param("countryName") String countryName);
}
