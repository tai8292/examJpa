package hello.repositories;

import hello.entities.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, Long>{

    @Query("select c from Company c where c.city.id=:city_id ")
    public List<Company> findByCityId(@Param("city_id")Long city_id);

    @Query("select new Company(c.id,c.name,c.city.id,c.city.name) from Company c left join c.city")
    public List<Company> findByCityId1();

    @Query("select c from Company c join fetch c.city")
    public List<Company> findByCityId2();

}
