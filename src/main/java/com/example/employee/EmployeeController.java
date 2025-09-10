package com.example.employee;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("employees")
public class EmployeeController {

    private final Map<Integer, Employee> employees = new HashMap<>();
    private int id = 1;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Employee create(@RequestBody Employee employee) {
        Employee newEmployee = new Employee(this.id, employee.name(), employee.age(), employee.gender(), employee.salary());
        employees.put(newEmployee.id(), newEmployee);
        this.id++;
        return newEmployee;
    }

    @GetMapping("{id}")
    @ResponseStatus(HttpStatus.OK)
    public Employee get(@PathVariable int id) {
        return employees.get(id);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Employee> getAll(@RequestParam(required = false) String gender, @RequestParam(required = false) Integer page, @RequestParam(required = false) Integer size) {
        List<Employee> returnEmployees = new ArrayList<>(employees.values());
        if (gender != null) {
            returnEmployees = returnEmployees.stream().filter(employee -> employee.gender().equals(gender)).toList();
        }
        if (page != null && size != null && page >= 0 && page < (returnEmployees.size() / size + 1) && size > 0) {
            returnEmployees = returnEmployees.subList(page * size, Math.min(page * size + size, returnEmployees.size()));
        }
        return returnEmployees;
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee update(@PathVariable int id, @RequestBody Employee employee) {
        employees.put(id, employee);
        return employees.get(id);
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable int id) {
        employees.remove(id);
    }

    public void clear() {
        employees.clear();
        this.id = 1;
    }

}
