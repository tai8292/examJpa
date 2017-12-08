package hello.controllers;

import hello.dto.CountryDto;
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

import java.util.*;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RunWith(SpringRunner.class)
@WebMvcTest(CountryController.class)
public class CountryControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private CountryController countryController;

    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void getListCountry() throws Exception {
        Country country = new Country();
        country.setName("VN");
        List<Country> countryList = Arrays.asList(country);
        Page<Country> page = new PageImpl<Country>(countryList);
        ResponseEntity responseEntity = new ResponseEntity(page,HttpStatus.OK);

        given(countryController.getListCountry(1)).willReturn(responseEntity);

        mvc.perform(get("/country/all").param("pagenum",1+"")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages",is(1)))
                .andExpect(jsonPath("$.totalElements",is(1)))
                .andExpect(jsonPath("$.content[0].name",is("VN")));
    }

    @Test
    public void addCountry() throws Exception {
        Country country = new Country();


        CountryDto countryDto = new CountryDto();
        countryDto.setId(1L);
        countryDto.setName("abc");
        ResponseEntity responseEntity = new ResponseEntity(country,HttpStatus.CREATED);


        given(countryController.addCountry(countryDto)).willReturn(responseEntity);

        mvc.perform(post("/country/add").contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(countryDto)))
                .andExpect(status().isCreated());

    }


    @Test
    public void deleteCountry() throws Exception {

    }

    @Test
    public void findById() throws Exception {
        Country country = new Country();
        country.setId(1L);
        country.setName("VN");
        ResponseEntity responseEntity = new ResponseEntity(country,HttpStatus.OK);

        given(countryController.findById(country.getId())).willReturn(responseEntity);

        mvc.perform(get("/country/"+country.getId()).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("VN")));
    }

    @Test
    public void findByBusinessCompanyOk() throws Exception {
        Country country = new Country();
        country.setName("Viet Nam");
        City city = new City();
        city.setCountry(country);
        Company company = new Company();
        company.setCity(city);
        company.setBusinessLicense("Edu");

        ResponseEntity responseEntity = new ResponseEntity(country,HttpStatus.OK);

        given(countryController.findByBusinessCompany("Edu")).willReturn(responseEntity);

        mvc.perform(get("/country/find/business").param("business","Edu")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name",is("Viet Nam")));
    }

}