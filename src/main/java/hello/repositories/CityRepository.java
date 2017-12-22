package hello.repositories;

import hello.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {

    @Query("select ct from City ct where ct.country.id =:country_id")
    List<City> findByCountryId(@Param("country_id") Long country_id);

    @Query("select distinct c.city from Company c left join c.city where c.businessLicense like %:business%")
    List<City> findByCompanyBusiness(@Param("business") String business);

    @Query(value = "select distinct ct from Company cp left join cp.city ct left join ct.country c" +
            " where cp.businessLicense like %:business% and c.name like %:countryName%")
    List<City> findByCountryNameAndBusinessCompany(@Param("countryName") String countryName,
                                                   @Param("business") String business);

    List<City> findByName(String name);

    List<City> findByCode(String code);
}
