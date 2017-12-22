package hello.controllers;


import hello.dto.CountryDto;
import hello.entities.Country;
import hello.repositories.CityRepository;
import hello.repositories.CountryRepository;
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
@RequestMapping(path = "/country")
public class CountryController {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    CityRepository cityRepository;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getListCountry(@RequestParam int pagenum) {
        PageRequest pageRequest = new PageRequest(pagenum - 1, 10, Sort.Direction.ASC, "code");
        Page<Country> page = countryRepository.findAll(pageRequest);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addCountry(@RequestBody CountryDto countryDto) {
        if (countryDto.getName() == null || countryDto.getCode() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (countryDto.getName().equals("") || countryDto.getCode().equals(""))
            return new ResponseEntity<>("Name or code is empty", HttpStatus.BAD_REQUEST);
        Country country = new Country();
        if (countryRepository.findByName(countryDto.getName()).size() != 0)
            return new ResponseEntity<>("Name is used", HttpStatus.BAD_REQUEST);
        country.setName(countryDto.getName());
        if (countryRepository.findByCode(countryDto.getCode()).size() != 0)
            return new ResponseEntity<>("Code is used", HttpStatus.BAD_REQUEST);
        country.setCode(countryDto.getCode());
        country.setCreatedDate(new Date());
        country.setModifiedDate(new Date());
        countryRepository.save(country);
        return new ResponseEntity<>(country, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCountry(@RequestParam Long id) {
        if (countryRepository.exists(id)) {
            if (cityRepository.findByCountryId(id).size() == 0) {
                countryRepository.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("City is used", HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@RequestParam Long id) {
        if (countryRepository.exists(id)) {
            return new ResponseEntity<>(countryRepository.findOne(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/find/business", method = RequestMethod.GET)
    public ResponseEntity<?> findByBusinessCompany(@RequestParam String business) {
        List<Country> countryList = countryRepository.findByBusinessCompany(business);
        if (countryList.size() == 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(countryList, HttpStatus.OK);
    }

    //count city of country
    @RequestMapping(path = "/countcity", method = RequestMethod.GET)
    public ResponseEntity<?> countCityOfCountry(@RequestParam Long id) {
        if (id == null) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        if (countryRepository.exists(id)) {
            return new ResponseEntity<>(countryRepository.countCity(id), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCountry(@RequestBody CountryDto countryDto) {
        if (countryDto.getId() == null || countryDto.getName() == null || countryDto.getCode() == null) {
            return new ResponseEntity<>("value is null", HttpStatus.BAD_REQUEST);
        }
        if (countryRepository.exists(countryDto.getId())) {
            if (countryDto.getName().equals("") || countryDto.getCode().equals(""))
                return new ResponseEntity<>("Name and code is't empty", HttpStatus.BAD_REQUEST);
            Country country = countryRepository.findOne(countryDto.getId());
            country.setName(countryDto.getName());
            country.setCode(countryDto.getCode());
            country.setModifiedDate(new Date());
            countryRepository.save(country);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);

    }

}
