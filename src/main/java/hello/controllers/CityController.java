package hello.controllers;

import hello.dto.CityDto;
import hello.entities.City;
import hello.entities.Country;
import hello.repositories.CityRepository;
import hello.repositories.CompanyRepository;
import hello.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping(path = "/city")
public class CityController {

    @Autowired
    CityRepository cityRepository;

    @Autowired
    CountryRepository countryRepository;

    @Autowired
    CompanyRepository companyRepository;

    @RequestMapping(path = "/all", method = RequestMethod.GET)
    public ResponseEntity<?> getListCity(@RequestParam int pagenum) {
        PageRequest pageRequest = new PageRequest(pagenum - 1, 10);

        Page<City> page = cityRepository.findAll(pageRequest);

        return new ResponseEntity<>(page, HttpStatus.OK);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public ResponseEntity<?> addCity(@RequestBody CityDto cityDto) {
        try {
            if(countryRepository.exists(cityDto.getCountryDto().getId())) {
                Country country = countryRepository.findOne(cityDto.getCountryDto().getId());

                City city = new City();
                city.setName(cityDto.getName());
                city.setCode(cityDto.getCode());
                city.setCountry(country);
                city.setCreateDate(new Date());
                city.setModifiedDate(new Date());
                cityRepository.save(city);
                return new ResponseEntity(city, HttpStatus.CREATED);
            }
            else
            {
                return new ResponseEntity<>("Country id isn't exist",HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity(HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCity(@RequestParam Long id) {
        if (cityRepository.exists(id)) {
            if (companyRepository.findByCityId(id) != null) {
                return new ResponseEntity<>("City is used",HttpStatus.CONFLICT);
            } else {
                countryRepository.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>("Id isn't exist",HttpStatus.NOT_FOUND);
    }
}
