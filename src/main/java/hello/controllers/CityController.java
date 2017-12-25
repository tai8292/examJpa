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
import java.util.List;

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
        if (cityDto.getCode() == null || cityDto.getName() == null || cityDto.getCountryDto() == null
                || cityDto.getCountryDto().getId() == null)
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        if (cityDto.getName().equals("") || cityDto.getCode().equals(""))
            return new ResponseEntity<>("Name or code is null", HttpStatus.BAD_REQUEST);
        if (countryRepository.exists(cityDto.getCountryDto().getId())) {
            Country country = countryRepository.findOne(cityDto.getCountryDto().getId());

            City city = new City();
            if (cityRepository.findByName(cityDto.getName()).size() != 0)
                return new ResponseEntity<>("Name is used", HttpStatus.CONFLICT);
            city.setName(cityDto.getName());
            if (cityRepository.findByCode(cityDto.getCode()).size() != 0)
                return new ResponseEntity<>("Code is used", HttpStatus.CONFLICT);
            city.setCode(cityDto.getCode());
            city.setCountry(country);
            city.setCreatedDate(new Date());
            city.setModifiedDate(new Date());
            cityRepository.save(city);
            return new ResponseEntity(city, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Country id isn't exist", HttpStatus.NOT_FOUND);
        }
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public ResponseEntity<?> deleteCity(@RequestParam Long id) {
        if (cityRepository.exists(id)) {
            if (companyRepository.findByCityId(id).size() == 0) {
                cityRepository.delete(id);
                return new ResponseEntity<>(HttpStatus.OK);
            } else {
                return new ResponseEntity<>("City is used", HttpStatus.CONFLICT);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/findByBusiness", method = RequestMethod.GET)
    public ResponseEntity<?> findByBusinessCompany(@RequestParam String business) {
        List<City> cityList = cityRepository.findByCompanyBusiness(business);
        if (cityList.size() == 0)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return new ResponseEntity<>(cityList, HttpStatus.OK);
    }

    @RequestMapping(path = "/find1", method = RequestMethod.GET)
    public ResponseEntity<?> findByCountryNameAndCompanyBusiness(@RequestParam String name, @RequestParam String business) {
        List<City> cityList = cityRepository.findByCountryNameAndBusinessCompany(name, business);
        if (cityList.size() != 0)
            return new ResponseEntity<>(cityList, HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @RequestMapping(path = "/update", method = RequestMethod.PUT)
    public ResponseEntity<?> updateCity(@RequestBody CityDto cityDto) {
        if (cityDto.getId() == null || cityDto.getName() == null || cityDto.getCode() == null
                || cityDto.getCountryDto() == null || cityDto.getCountryDto().getId() == null)
            return new ResponseEntity<>("value is null", HttpStatus.BAD_REQUEST);
        if (cityRepository.exists(cityDto.getId())) {
            if (cityDto.getName().equals("") || cityDto.getCode().equals("")) {
                return new ResponseEntity<>("name or code is empty", HttpStatus.BAD_REQUEST);
            }
            if (countryRepository.exists(cityDto.getCountryDto().getId())) {
                City city = cityRepository.findOne(cityDto.getId());
                city.setName(cityDto.getName());
                city.setCode(cityDto.getCode());
                city.setCountry(countryRepository.findOne(cityDto.getCountryDto().getId()));
                city.setModifiedDate(new Date());
                return new ResponseEntity<>(cityRepository.save(city), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("country id is't exist", HttpStatus.NOT_FOUND);
            }
        }
        return new ResponseEntity<>("city id is't exist", HttpStatus.NOT_FOUND);
    }
}
