package hello.controllers;

import hello.dto.CompanyDto;
import hello.entities.City;
import hello.entities.Company;
import hello.repositories.CityRepository;
import hello.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "/company")
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CityRepository cityRepository;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Page<Company> getListCompany(@RequestParam int pagenum) {
        PageRequest pageRequest = new PageRequest(pagenum - 1, 10);
        return companyRepository.findAll(pageRequest);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity<Company> addCompany(@RequestBody CompanyDto companyDto) {
        City city = cityRepository.findOne(companyDto.getCityDto().getId());

        Company company = new Company();
        company.setName(companyDto.getName());
        company.setBusiness_license(companyDto.getBusiness_license());
        company.setCity(city);
        companyRepository.save(company);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String deleteCompany(@RequestParam Long id) {

        companyRepository.delete(id);
        return "detele";
    }

}
