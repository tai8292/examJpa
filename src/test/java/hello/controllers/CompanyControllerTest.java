package hello.controllers;

import hello.entities.City;
import hello.entities.Company;
import hello.entities.Country;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import static org.hamcrest.CoreMatchers.is;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CompanyController.class)
public class CompanyControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    CompanyController companyController;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getListCompany() throws Exception {
        Company company = new Company();
        company.setName("DTU");
        Company company1 = new Company();
        company1.setName("BK");

        List<Company> companyList = Arrays.asList(company,company1);
        Page<Company> page = new PageImpl<Company>(companyList);
        ResponseEntity responseEntity = new ResponseEntity(page, HttpStatus.OK);

        given(companyController.getListCompany(1)).willReturn(responseEntity);

        mvc.perform(get("/company/all").param("pagenum",1+"").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages",is(1)))
                .andExpect(jsonPath("$.content[0].name",is("DTU")))
                .andExpect(jsonPath("$.content[1].name",is("BK")));
    }

    @Test
    public void addCompany() throws Exception {
    }

    @Test
    public void deleteCompany() throws Exception {
    }

    @Test
    public void findCompany() throws Exception {

    }

    @Test
    public void findByCountryName() throws Exception {
        Country country = new Country();
        country.setName("Viet Nam");
        City city = new City();
        city.setCountry(country);
        Company company = new Company();
        company.setName("DTU");
        company.setCity(city);

        List<Company> companyList = Arrays.asList(company);
        ResponseEntity responseEntity = new ResponseEntity(companyList,HttpStatus.OK);

        given(companyController.findByCountryName("Viet Nam")).willReturn(responseEntity);

        mvc.perform(get("/company/find/country").param("countryName","Viet Nam")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].name",is("DTU")));

    }

}