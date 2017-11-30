package hello.controllers;

import hello.dto.CompanyDto;
import hello.entities.City;
import hello.entities.Company;
import hello.repositories.CityRepository;
import hello.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/company")
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CityRepository cityRepository;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Page<Company> getListCompany(@RequestParam int pagenum, @RequestParam int size) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "businessLicense"),
                new Sort.Order(Sort.Direction.ASC, "id"));
        PageRequest pageRequest1 = new PageRequest(pagenum - 1, size, sort);
        return companyRepository.findAll(pageRequest1);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity<Company> addCompany(@RequestBody CompanyDto companyDto) {
        City city = cityRepository.findOne(companyDto.getCityDto().getId());

        Company company = new Company();
        company.setName(companyDto.getName());
        company.setBusinessLicense(companyDto.getBusiness_license());
        company.setCity(city);
        company.setCreate_date(new Date());
        company.setModified_date(new Date());
        companyRepository.save(company);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String deleteCompany(@RequestParam Long id) {
        if (companyRepository.exists(id)) {
            companyRepository.delete(id);
            return "delete";
        }
        return "the company id is not exist";
    }

    @RequestMapping(path = "/path", method = RequestMethod.GET)
    public List<Company> getByCity(@RequestParam Long cityId) {
        return companyRepository.findByCityId(cityId);
    }

    @RequestMapping(path = "/path1", method = RequestMethod.GET)
    public List<Company> getByCity1() {
        return companyRepository.findByCityId1();
    }

    @RequestMapping(path = "/path2", method = RequestMethod.GET)
    public List<Company> getByCity2() {
        return companyRepository.findByCityId2();
    }

}
