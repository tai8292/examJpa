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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@DataJpaTest
public class CompanyRepositoryTest {

    @Autowired
    EntityManager entityManager;
    @Autowired
    CompanyRepository companyRepository;
    ArrayList<Company> companyList;
    City city;

    @Before
    public void setUp() throws Exception {
        companyList = new ArrayList<>();
        Country country = new Country();
        country.setName("VN");
        city = new City();
        city.setCountry(country);
        Company company = new Company();
        company.setCity(city);
        company.setName("CGV");
        company.setBusinessLicense("movie");
        companyList.add(company);
        entityManager.persist(country);
        entityManager.persist(city);
        entityManager.persist(company);
        company = new Company();
        entityManager.persist(company);
        entityManager.flush();
    }

    @Test
    public void findByCityId() throws Exception {
        List<Company> companies = companyRepository.findByCityId(city.getId());
        assertEquals(1,companies.size());
        assertEquals(companyList.get(0),companies.get(0));
    }

    @Test
    public void findByNameContainsOrBusinessLicenseContains() throws Exception {
        List<Company> companies = companyRepository.findByNameContainsOrBusinessLicenseContains("CGV","movie");
        assertEquals(1,companies.size());
        assertEquals(companyList.get(0),companies.get(0));
    }

    @Test
    public void findByCountryName() throws Exception {
        List<Company> companies = companyRepository.findByCountryName("VN");
        assertEquals(1,companies.size());
        assertEquals(companyList.get(0),companies.get(0));
    }

}