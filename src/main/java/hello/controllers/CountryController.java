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
        Country country = new Country();
        country.setName(countryDto.getName());
        country.setCode(countryDto.getCode());
        country.setCreatedDate(new Date());
        country.setModifiedDate(new Date());
        countryRepository.save(country);
        return new ResponseEntity<>(country, HttpStatus.CREATED);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCountry(@RequestParam Long id) {
        if (countryRepository.exists(id)) {
            if (cityRepository.findByCountryId(id) != null) {
                countryRepository.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("City is used", HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>("id isn't exist", HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findById(@PathVariable Long id) {
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
}
