package hello.repositories;

import hello.entities.City;
import hello.entities.Company;
import hello.entities.Country;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CityRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    CityRepository cityRepository;
    private Country country;
    private City city;

    @Before
    public void setUp() throws Exception {
        country = new Country();
        country.setName("Viet Nam");
        city = new City();
        city.setCountry(country);
        Company company = new Company();
        company.setBusinessLicense("Edu");
        company.setCity(city);
        entityManager.persist(country);
        entityManager.persist(city);
        entityManager.persist(company);
        entityManager.flush();
    }

    @Test
    public void findByCountryId() throws Exception {
        List<City> cities = cityRepository.findByCountryId(country.getId());
        assertEquals(1,cities.size());
        assertEquals(city,cities.get(0));

    }

    @Test
    public void findByCompanyBusiness() throws Exception {
        List<City> cities = cityRepository.findByCompanyBusiness("Edu");
        assertEquals(1,cities.size());
        assertEquals(city,cities.get(0));
    }

    @Test
    public void findByCountryNameAndBusinessCompany() throws Exception {
        List<City> cities = cityRepository.findByCountryNameAndBusinessCompany("Viet Nam","Edu");
        assertEquals(1,cities.size());
        assertEquals(city,cities.get(0));
    }

}