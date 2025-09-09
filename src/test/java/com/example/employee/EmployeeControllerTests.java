package com.example.employee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class EmployeeControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private EmployeeController employeeController;

    @BeforeEach
    public void setUp() {
        employeeController.clear();
    }

    @Test
    public void should_return_created_employee_when_post() throws Exception {
        String requestBody = """
                {
                    "name": "John Smith",
                    "age": 32,
                    "gender": "Male",
                    "salary": 5000.0
                }""";
        MockHttpServletRequestBuilder request = post("/employees").contentType(MediaType.APPLICATION_JSON).content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("John Smith"))
                .andExpect(jsonPath("$.gender").value("Male"))
                .andExpect(jsonPath("$.age").value(32))
                .andExpect(jsonPath("$.salary").value(5000.0));
    }

    @Test
    public void should_return_employee_when_get_id() throws Exception {
        Employee employee = new Employee(null, "John Smith", 32, "Male", 5000.0);
        Employee createdEmployee = employeeController.create(employee);

        MockHttpServletRequestBuilder request = get("/employees/" + createdEmployee.id()).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(createdEmployee.id()))
                .andExpect(jsonPath("$.name").value(createdEmployee.name()))
                .andExpect(jsonPath("$.gender").value(createdEmployee.gender()))
                .andExpect(jsonPath("$.age").value(createdEmployee.age()))
                .andExpect(jsonPath("$.salary").value(createdEmployee.salary()));
    }

    @Test
    public void should_return_employee_when_list_by_male() throws Exception {
        Employee createdMaleEmployee = employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "Lily", 22, "Female", 5000.0));

        MockHttpServletRequestBuilder request = get("/employees?gender=male").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(createdMaleEmployee.id()))
                .andExpect(jsonPath("$[0].name").value(createdMaleEmployee.name()))
                .andExpect(jsonPath("$[0].gender").value(createdMaleEmployee.gender()))
                .andExpect(jsonPath("$[0].age").value(createdMaleEmployee.age()))
                .andExpect(jsonPath("$[0].salary").value(createdMaleEmployee.salary()));
    }

    @Test
    public void should_return_all_employees_when_get_employee_list() throws Exception {
        Employee createdMaleEmployee = employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "", 0, "", 0.0));
        employeeController.create(new Employee(null, "", 0, "", 0.0));
        employeeController.create(new Employee(null, "", 0, "", 0.0));
        employeeController.create(new Employee(null, "", 0, "", 0.0));
        MockHttpServletRequestBuilder request = get("/employees").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].id").value(createdMaleEmployee.id()))
                .andExpect(jsonPath("$[0].name").value(createdMaleEmployee.name()))
                .andExpect(jsonPath("$[0].gender").value(createdMaleEmployee.gender()))
                .andExpect(jsonPath("$[0].age").value(createdMaleEmployee.age()))
                .andExpect(jsonPath("$[0].salary").value(createdMaleEmployee.salary()));
    }

    @Test
    public void should_return_updated_employee_when_update_employee() throws Exception {
        Employee createdMaleEmployee = employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        String requestBody = """
                {
                    "id": 1,
                    "name": "John Smith",
                    "age": 33,
                    "gender": "Male",
                    "salary": 10000.0
                }""";

        MockHttpServletRequestBuilder request = put("/employees/1").contentType(MediaType.APPLICATION_JSON).content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$.id").value(createdMaleEmployee.id()))
                .andExpect(jsonPath("$.name").value(createdMaleEmployee.name()))
                .andExpect(jsonPath("$.gender").value(createdMaleEmployee.gender()))
                .andExpect(jsonPath("$.age").value(33))
                .andExpect(jsonPath("$.salary").value(10000.0));
    }

    @Test
    public void should_return_204_when_delete_employee() throws Exception {
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));

        MockHttpServletRequestBuilder request = delete("/employees/1").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_five_employees_on_page_1_when_select_page_equal_1_size_equal_5() throws Exception {
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "Lily", 22, "Female", 5000.0));
        employeeController.create(new Employee(null, "Lily", 22, "Female", 5000.0));
        employeeController.create(new Employee(null, "Lily", 22, "Female", 5000.0));
        employeeController.create(new Employee(null, "Lily", 22, "Female", 5000.0));
        employeeController.create(new Employee(null, "Lily", 22, "Female", 5000.0));
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));
        employeeController.create(new Employee(null, "John Smith", 32, "Male", 5000.0));

        MockHttpServletRequestBuilder request = get("/employees?page=1&size=5").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("Lily"))
                .andExpect(jsonPath("$[1].name").value("Lily"))
                .andExpect(jsonPath("$[2].name").value("Lily"))
                .andExpect(jsonPath("$[3].name").value("Lily"))
                .andExpect(jsonPath("$[4].name").value("Lily"));
    }

}
