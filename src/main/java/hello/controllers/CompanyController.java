package hello.controllers;

import hello.entities.Company;
import hello.repositories.CityRepository;
import hello.repositories.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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
    public String addCompany(@ModelAttribute("name") String name, @ModelAttribute("business") String business,
                             @ModelAttribute("cityid") Long city_id) {
        Company company = new Company();
        company.setName(name);
        company.setBusiness_license(business);
        company.setCity(cityRepository.findOne(city_id));
        company.setCreate_date(new Date());
        company.setModified_date(new Date());
        companyRepository.save(company);
        return "Save";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.DELETE)
    public String deleteCompany(@RequestParam Long id) {

        companyRepository.delete(id);
        return "detele";
    }

}
