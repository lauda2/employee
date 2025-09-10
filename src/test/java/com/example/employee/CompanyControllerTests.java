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
public class CompanyControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyController companyController;

    @BeforeEach
    public void setUp() {
        companyController.clear();
    }

    @Test
    public void should_return_new_company_when_create_company() throws Exception {
        String requestBody = """
                {
                    "name": "A"
                }""";
        MockHttpServletRequestBuilder request = post("/companies").contentType(MediaType.APPLICATION_JSON).content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.name").value("A"));
    }

    @Test
    public void should_return_all_company_whe_obtain_company_list() throws Exception {
        Company company = companyController.create(new Company(null, "A"));
        companyController.create(new Company(null, "B"));


        MockHttpServletRequestBuilder request = get("/companies").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(company.id()))
                .andExpect(jsonPath("$[0].name").value(company.name()));
    }

    @Test
    public void should_return_specific_company_whe_obtain_company() throws Exception {
        Company company = companyController.create(new Company(null, "A"));
        companyController.create(new Company(null, "B"));

        MockHttpServletRequestBuilder request = get("/companies/" + company.id()).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(company.id()))
                .andExpect(jsonPath("$.name").value(company.name()));
    }

    @Test
    public void should_return_company_when_update_company() throws Exception {
        Company company = companyController.create(new Company(null, "A"));
        String requestBody = """
                {
                    "name": "B"
                }""";
        MockHttpServletRequestBuilder request = put("/companies/" + company.id()).contentType(MediaType.APPLICATION_JSON).content(requestBody);

        mockMvc.perform(request)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(company.id()))
                .andExpect(jsonPath("$.name").value("B"));
    }

    @Test
    public void should_return_204_when_delete_employee() throws Exception {
        Company company = companyController.create(new Company(null, "A"));

        MockHttpServletRequestBuilder request = delete("/companies/" + company.id()).contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isNoContent());
    }

    @Test
    public void should_return_five_employees_on_page_1_when_select_page_equal_1_size_equal_5() throws  Exception {
        companyController.create(new Company(null, "A"));
        companyController.create(new Company(null, "A"));
        companyController.create(new Company(null, "A"));
        companyController.create(new Company(null, "B"));
        companyController.create(new Company(null, "B"));
        companyController.create(new Company(null, "B"));
        companyController.create(new Company(null, "C"));
        companyController.create(new Company(null, "C"));
        companyController.create(new Company(null, "D"));
        companyController.create(new Company(null, "A"));
        companyController.create(new Company(null, "A"));
        companyController.create(new Company(null, "A"));

        MockHttpServletRequestBuilder request = get("/companies?page=1&size=5").contentType(MediaType.APPLICATION_JSON);

        mockMvc.perform(request)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(5))
                .andExpect(jsonPath("$[0].name").value("B"))
                .andExpect(jsonPath("$[1].name").value("C"))
                .andExpect(jsonPath("$[2].name").value("C"))
                .andExpect(jsonPath("$[3].name").value("D"))
                .andExpect(jsonPath("$[4].name").value("A"));
    }
}
