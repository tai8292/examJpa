package hello.controllers;


import hello.dto.CountryDto;
import hello.entities.City;
import hello.entities.Country;
import hello.repositories.CityRepository;
import hello.repositories.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(path = "/country")
public class CountryController {

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    CityRepository cityRepository;

    @Autowired
    ModelMapper modelMapper;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public Page<Country> getListCountry(@RequestParam int pagenum) {
        PageRequest pageRequest = new PageRequest(pagenum - 1, 10, Sort.Direction.ASC,"code");
        return countryRepository.findAll(pageRequest);
    }


    @RequestMapping(path = "add", method = RequestMethod.POST)
    public ResponseEntity<Country> addCountry(@RequestBody @Valid CountryDto countryDto) {
        Country country = new Country();
        country.setName(countryDto.getName());
        country.setCode(countryDto.getCode());
        country.setCreated_date(new Date());
        country.setModified_date(new Date());
        countryRepository.save(country);
        return new ResponseEntity<Country>(HttpStatus.CREATED);
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String deleteCountry(@RequestParam Long id) {
        if (countryRepository.exists(id)) {
            if (cityRepository.findByCountryId(id) != null) {
                return "Country be using";
            } else {
                countryRepository.delete(id);
                return "delete";
            }
        }
        return "Country is not exist";
    }
}
