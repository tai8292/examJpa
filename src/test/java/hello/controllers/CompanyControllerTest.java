package hello.controllers;

import hello.Application;
import hello.dto.CityDto;
import hello.dto.CompanyDto;
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
import static org.hamcrest.CoreMatchers.is;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class CompanyControllerTest {

    @Autowired
    private MockMvc mvc;
    private City city;
    private Company company;
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

        Country country = new Country();
        country.setName("Viet Nam");
        city = new City();
        city.setName("Da Nang");
        city.setCountry(country);
        company = new Company();
        company.setCity(city);
        company.setBusinessLicense("Edu");
        company.setName("DTU");

        countryRepository.save(country);
        cityRepository.save(city);
        companyRepository.save(company);
    }

    @Test
    public void getListCompany() throws Exception {
        mvc.perform(get("/company/all").contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("pagenum", "" + 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages", is(1)))
                .andExpect(jsonPath("$.content[*].name").value(hasItem(company.getName())))
                .andExpect(jsonPath("$.content[*].businessLicense").value(hasItem(company.getBusinessLicense())));
    }

    @Test
    public void addCompanyOk() throws Exception {
        CityDto cityDto = new CityDto();
        cityDto.setId(city.getId());
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCityDto(cityDto);

        mvc.perform(post("/company/add").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyDto)))
                .andExpect(status().isCreated());
    }

    @Test
    public void addCompanyNotFound() throws Exception {
        CityDto cityDto = new CityDto();
        cityDto.setId(city.getId() + 1);
        CompanyDto companyDto = new CompanyDto();
        companyDto.setCityDto(cityDto);

        mvc.perform(post("/company/add").contentType(MediaType.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(companyDto)))
                .andExpect(status().isNotFound());
    }

    @Test
    public void deleteCompanyOk() throws Exception {
        mvc.perform(delete("/company/delete").param("id", company.getId() + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

    @Test
    public void deleteCompanyNotFound() throws Exception {
        mvc.perform(delete("/company/delete").param("id", (company.getId() + 1) + "")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findCompanyOk() throws Exception {
        mvc.perform(get("/company/find").param("value", "Edu")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("DTU")));
    }

    @Test
    public void findCompanyNotFound() throws Exception {
        mvc.perform(get("/company/find").param("value", "Education")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

    @Test
    public void findByCountryNameOk() throws Exception {
        mvc.perform(get("/company/find/country").param("countryName", "Viet Nam")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[*].name").value(hasItem(company.getName())))
                .andExpect(jsonPath("$[*].businessLicense").value(hasItem(company.getBusinessLicense())));
    }

    @Test
    public void findByCountryNameNotFound() throws Exception {
        mvc.perform(get("/company/find/country").param("countryName", "Viet Nam a")
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isNotFound());
    }

}