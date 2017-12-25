package hello.controllers;

import hello.Application;
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
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class CountryControllerTest {

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
        this.companyRepository.deleteAll();
        this.cityRepository.deleteAll();
        this.countryRepository.deleteAll();

        country = new Country();
        country.setName("Viet Nam");
        country.setCode("VN");
        countryRepository.save(country);
    }

    @Test
    public void getListCountry() throws Exception {
        mvc.perform(get("/country/all").param("pagenum", 1 + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content.[*].name").value(hasItem(country.getName())))
                .andExpect(jsonPath("$.content.[*].code").value(hasItem(country.getCode())));
    }

    @Test
    public void addCountry() throws Exception {
        CountryDto countryDto = new CountryDto();
        countryDto.setName("Viet Nam 1");
        countryDto.setCode("VN1");
        mvc.perform(post("/country/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(countryDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addCountryNameConflict() throws Exception {
        CountryDto countryDto = new CountryDto();
        countryDto.setName("Viet Nam");
        countryDto.setCode("VN1");

        mvc.perform(post("/country/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(countryDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void addCountryCodeConflict() throws Exception {
        CountryDto countryDto = new CountryDto();
        countryDto.setName("Viet");
        countryDto.setCode("VN");

        mvc.perform(post("/country/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(countryDto)))
                .andExpect(status().isConflict());
    }

    @Test
    public void addCountryNull() throws Exception {
        CountryDto countryDto = new CountryDto();

        mvc.perform(post("/country/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(countryDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addCountryEmpty() throws Exception {
        CountryDto countryDto = new CountryDto();
        countryDto.setName("");
        countryDto.setCode("");

        mvc.perform(post("/country/add").contentType(MediaType.APPLICATION_JSON)
                .content(TestUtil.convertObjectToJsonBytes(countryDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void deleteCountryOk() throws Exception {
        mvc.perform(delete("/country/delete").param("id", country.getId() + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCountryNotFound() throws Exception {
        mvc.perform(delete("/country/delete").param("id", (country.getId() + 1) + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCountryConflict() throws Exception {
        city = new City();
        city.setCountry(country);
        cityRepository.save(city);
        mvc.perform(delete("/country/delete").param("id", country.getId() + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isConflict());
    }

    @Test
    public void findByIdOk() throws Exception {
        mvc.perform(get("/country").param("id", country.getId() + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(country.getName())));
    }

    @Test
    public void findByIdNotFound() throws Exception {
        mvc.perform(get("/country").param("id", (country.getId() + 1) + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByBusinessCompanyOk() throws Exception {
        city = new City();
        city.setCountry(country);
        Company company = new Company();
        company.setBusinessLicense("Edu");
        company.setCity(city);
        cityRepository.save(city);
        companyRepository.save(company);

        mvc.perform(get("/country/find/business").param("business", "Edu")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is(country.getName())));
    }

    @Test
    public void findByBusinessCompanyNotFound() throws Exception {
        mvc.perform(get("/country/find/business").param("business", "Edu")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }
}
