package hello.controllers;

import hello.Application;
import hello.dto.CityDto;
import hello.dto.CountryDto;
import hello.entities.City;
import hello.entities.Company;
import hello.entities.Country;
import hello.repositories.CityRepository;
import hello.repositories.CompanyRepository;
import hello.repositories.CountryRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class CityControllerTest {

    @Autowired
    private MockMvc mvc;
    private Country country;
    private City city;

    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private CompanyRepository companyRepository;

    @Before
    public void setUp() throws Exception {
        companyRepository.deleteAll();
        cityRepository.deleteAll();
        countryRepository.deleteAll();

        country = new Country();
        country.setName("Viet Nam");
        city = new City();
        city.setName("Da Nang");
        city.setCode("DN");
        city.setCountry(country);

        countryRepository.save(country);
        cityRepository.save(city);
    }

    @Test
    public void getListCity() throws Exception {
        mvc.perform(get("/city/all").param("pagenum",1+"")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[*].name").value(hasItem(city.getName())))
                .andExpect(jsonPath("$.content.[*].code").value(hasItem(city.getCode())))
                .andExpect(jsonPath("$.content.[*].country.name").value(hasItem(city.getCountry().getName())));
    }

    @Test
    public void addCityCreate() throws Exception {
        CityDto cityDto = new CityDto();
        CountryDto countryDto = new CountryDto();
        countryDto.setId(country.getId());
        cityDto.setCountryDto(countryDto);

        mvc.perform(post("/city/add").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cityDto)))
                .andExpect(status().isCreated());

    }

    @Test
    public void addCityCreateNotFound() throws Exception {
        CityDto cityDto = new CityDto();
        CountryDto countryDto = new CountryDto();
        countryDto.setId(country.getId()+1);
        cityDto.setCountryDto(countryDto);

        mvc.perform(post("/city/add").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(cityDto)))
                .andExpect(status().isNotFound());

    }


    @Test
    public void deleteCityOk() throws Exception {
        mvc.perform(delete("/city/delete").param("id",city.getId()+""))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCityNotFound() throws Exception {
        mvc.perform(delete("/city/delete").param("id",(city.getId()+1)+""))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCityConFlict() throws Exception {
        Company company = new Company();
        company.setCity(city);
        companyRepository.save(company);

        mvc.perform(delete("/city/delete").param("id",city.getId()+""))
                .andExpect(status().isConflict());
    }

    @Test
    public void findByBusinessCompanyOk() throws Exception {
        Company company = new Company();
        company.setBusinessLicense("Edu");
        company.setCity(city);
        companyRepository.save(company);

        mvc.perform(get("/city/findByBusiness").param("business","Edu")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[*].name").value(hasItem(city.getName())))
                .andExpect(jsonPath("$[*].code").value(hasItem(city.getCode())));
    }

    @Test
    public void findByBusinessCompanyNotFound() throws Exception {
        mvc.perform(get("/city/findByBusiness").param("business","Edu")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByCountryNameAndCompanyBusinessOk() throws Exception {
        Company company = new Company();
        company.setBusinessLicense("Edu");
        company.setCity(city);
        companyRepository.save(company);

        mvc.perform(get("/city/find1").param("name","Viet Nam")
                .param("business","Edu")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[*].name").value(hasItem(city.getName())))
                .andExpect(jsonPath("$[*].code").value(hasItem(city.getCode())));
    }

    @Test
    public void findByCountryNameAndCompanyBusinessNotFound() throws Exception {
        mvc.perform(get("/city/find1").param("name","Viet Nam")
                .param("business","Edu")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

}