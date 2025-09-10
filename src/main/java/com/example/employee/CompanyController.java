package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("companies")
public class CompanyController {

    private final Map<Integer, Company> companies = new HashMap<>();
    private int id = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        Company newCompany = new Company(this.id, company.name());
        companies.put(newCompany.id(), newCompany);
        this.id++;
        return newCompany;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Company> getAll(@RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        List<Company> returnCompanies = new ArrayList<>(companies.values());
        if (pagination(page, size, returnCompanies)) {
            returnCompanies = returnCompanies.subList(page * size, Math.min(page * size + size, returnCompanies.size()));
        }
        return returnCompanies;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Company get(@PathVariable Integer id) {
        return companies.get(id);
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Company update(@PathVariable Integer id, @RequestBody Company company) {
        Company newCompany = new Company(id, company.name());
        companies.put(id, newCompany);
        return newCompany;
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Integer id) {
        companies.remove(id);
    }

    public void clear() {
        companies.clear();
        this.id = 1;
    }

    private boolean pagination(Integer page, Integer size, List<Company> returnCompanies) {
        return page != null && size != null && page >= 0 && page < (returnCompanies.size() / size + 1) && size > 0;
    }

}
