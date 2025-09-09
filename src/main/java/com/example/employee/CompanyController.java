package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("companies")
public class CompanyController {

    private Map<Integer, Company> Companies = new HashMap<>();
    private int id = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Company create(@RequestBody Company company) {
        Company newCompany = new Company(this.id, company.name());
        Companies.put(newCompany.id(), newCompany);
        this.id++;
        return newCompany;
    }

    public void clear() {
        Companies.clear();
        this.id = 1;
    }

}
