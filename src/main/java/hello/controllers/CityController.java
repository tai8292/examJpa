package hello.controllers;

import hello.entities.City;
import hello.repositories.CityRepository;
import hello.repositories.CompanyRepository;
import hello.repositories.CountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public Page<City> getListCity(@RequestParam int pagenum) {
        PageRequest pageRequest = new PageRequest(pagenum - 1, 10);
        return cityRepository.findAll(pageRequest);
    }

    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public String addCity(@ModelAttribute("city")City city ,@ModelAttribute("country_id") Long country_id) {
        if(countryRepository.exists(country_id)) {
            city.setCountry(countryRepository.findOne(country_id));
            city.setCreate_date(new Date());
            city.setModified_date(new Date());
            cityRepository.save(city);
            return "Save";
        }
        return "country id is not exist";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String deleteCity(@RequestParam Long id)
    {
        if(cityRepository.exists(id))
        {
            if(companyRepository.findByCityId(id)!=null)
            {
                return "";
            }
            else
            {
                countryRepository.delete(id);
                return "delete";
            }
        }
        else
        {
            return "Company ís not exist";
        }
    }
}
