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
    public ResponseEntity<?> getListCompany(@RequestParam int pagenum, @RequestParam int size) {
        Sort sort = new Sort(new Sort.Order(Sort.Direction.ASC, "businessLicense"),
                new Sort.Order(Sort.Direction.ASC, "id"));
        PageRequest pageRequest = new PageRequest(pagenum - 1, size, sort);
        Page<Company> page = companyRepository.findAll(pageRequest);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addCompany(@RequestBody CompanyDto companyDto) {
        try {
            if (cityRepository.exists(companyDto.getCityDto().getId())) {
                City city = cityRepository.findOne(companyDto.getCityDto().getId());

                Company company = new Company();
                company.setName(companyDto.getName());
                company.setBusinessLicense(companyDto.getBusinessLicense());
                company.setCity(city);
                company.setCreatedDate(new Date());
                company.setModifiedDate(new Date());
                companyRepository.save(company);
                return new ResponseEntity<>(company, HttpStatus.CREATED);
            }
            return new ResponseEntity<>("City id is not exist", HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCompany(@RequestParam Long id) {
        if (companyRepository.exists(id)) {
            companyRepository.delete(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>("Id isn't exist", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/find", method = RequestMethod.GET)
    public ResponseEntity<?> findCompany(@RequestParam String value) {
        List<Company> companyList = companyRepository.findByNameContainsOrBusinessLicenseContains(value, value);
        if (companyList.size() == 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(companyList, HttpStatus.OK);
    }

    @RequestMapping(path = "/find/country", method = RequestMethod.GET)
    public ResponseEntity<?> findByCountryName(@RequestParam String countryName) {
        List<Company> companyList = companyRepository.findByCountryName(countryName);
        if (companyList.size() != 0)
            return new ResponseEntity<>(companyList, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


}
