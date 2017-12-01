package hello.controllers;

import hello.dto.CityDto;
import hello.entities.City;
import hello.entities.Country;
import hello.repositories.CityRepository;
import hello.repositories.CompanyRepository;
import hello.repositories.CountryRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
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
    public ResponseEntity<Page<City>> getListCity(@RequestParam int pagenum) {
        PageRequest pageRequest = new PageRequest(pagenum - 1, 10);
        return ResponseEntity.ok(cityRepository.findAll(pageRequest));
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public CityDto addCity(@RequestBody @Valid CityDto cityDto) {

        Country country = countryRepository.findOne(cityDto.getCountryDto().getId());

        City city = new City();
        city.setName(cityDto.getName());
        city.setCode(cityDto.getCode());
        city.setCountry(country);
        city.setCreate_date(new Date());
        city.setModified_date(new Date());
        cityRepository.save(city);
        return cityDto;
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String deleteCity(@RequestParam Long id) {
        if (cityRepository.exists(id)) {
            if (companyRepository.findByCityId(id) != null) {
                return "City be using ";
            } else {
                countryRepository.delete(id);
                return "delete";
            }
        }
        return "City ís not exist";
    }
}
