package hello.repositories;

import hello.entities.City;
import hello.entities.Company;
import hello.entities.Country;
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
public class CountryRepositoryTest {

    @Autowired
    EntityManager entityManager;

    @Autowired
    CountryRepository countryRepository;

    @Test
    public void findByBusinessCompany() throws Exception {
        Country country = new Country();
        City city = new City();
        city.setCountry(country);
        Company company = new Company();
        company.setCity(city);
        company.setBusinessLicense("Edu");
        entityManager.persist(country);
        entityManager.persist(city);
        entityManager.persist(company);
        entityManager.flush();

        List<Country> found = countryRepository.findByBusinessCompany("Edu");
        assertEquals(1,found.size());
        assertEquals(country,found.get(0));
    }

}