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

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(CityController.class)
public class CityControllerTest {

    @Autowired
    MockMvc mvc;
    @MockBean
    CityController cityController;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getListCity() throws Exception {
        Country country = new Country();
        City city = new City();
        city.setCountry(country);
        city.setName("Da Nang");
        List<City> cityList = Arrays.asList(city);
        Page<City> page = new PageImpl<City>(cityList);
        ResponseEntity responseEntity = new ResponseEntity(page, HttpStatus.OK);

        given(cityController.getListCity(1)).willReturn(responseEntity);

        mvc.perform(get("/city/all").param("pagenum",1+"").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalPages",is(1)))
                .andExpect(jsonPath("$.content[0].name",is("Da Nang")));
    }

    @Test
    public void addCity() throws Exception {
    }

    @Test
    public void deleteCity() throws Exception {
    }

    @Test
    public void editCity() throws Exception {
    }

    @Test
    public void findByBusinessCompany() throws Exception {
        City city = new City();
        city.setName("Da Nang");
        Company company = new Company();
        company.setCity(city);
        company.setBusinessLicense("Edu");

        List<City> cityList = Arrays.asList(city);
        ResponseEntity responseEntity = new ResponseEntity(cityList,HttpStatus.OK);

        given(cityController.findByBusinessCompany("Edu")).willReturn(responseEntity);

        mvc.perform(get("/city/findByBusiness").param("business","Edu").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name",is("Da Nang")));
    }

    @Test
    public void findByCountryNameAndCompanyBusiness() throws Exception {
        Country country = new Country();
        country.setName("Viet Nam");
        City city = new City();
        city.setName("Da Nang");
        city.setCountry(country);
        Company company = new Company();
        company.setBusinessLicense("Edu");
        company.setCity(city);

        List<City> cityList = Arrays.asList(city);
        ResponseEntity responseEntity = new ResponseEntity(cityList,HttpStatus.OK);

        given(cityController.findByCountryNameAndCompanyBusiness("Viet Nam","Edu")).willReturn(responseEntity);

        mvc.perform(get("/city/find1").param("name","Viet Nam").param("business","Edu")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$",hasSize(1)))
                .andExpect(jsonPath("$[0].name",is("Da Nang")));

    }

}