package hello.repositories;

import hello.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long>{

    @Query("select ct from City ct left join ct.country c where c.id =:country_id")
    public List<City> findByCountryId(@Param("country_id") Long country_id);
}
