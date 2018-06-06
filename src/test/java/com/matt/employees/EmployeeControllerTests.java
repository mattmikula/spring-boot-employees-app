package com.matt.employees;

import com.matt.employees.models.Employee;
import com.matt.employees.models.EmployeeStatus;
import com.matt.employees.repositories.EmployeeRepository;
import com.matt.employees.services.EmployeeService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.time.LocalDate;
import java.util.Arrays;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = EmployeesApplication.class)
@WebAppConfiguration
@ContextConfiguration
public class EmployeeControllerTests {

    private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));

    private MockMvc mockMvc;

    private static final String username = "user";
    private static final String endpoint = "/employees/";

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    private Employee employeeOne;
    private Employee employeeTwo;
    private Employee inactiveEmployee;

    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).apply(springSecurity()).build();

        this.employeeRepository.deleteAll();

        this.employeeOne = new Employee("First", "O", "User", LocalDate.parse("1985-01-02"), LocalDate.parse("2018-06-03"), EmployeeStatus.ACTIVE);
        this.employeeTwo = new Employee("Second", "O", "User", LocalDate.parse("1985-01-02"), LocalDate.parse("2018-06-03"), EmployeeStatus.ACTIVE);
        this.inactiveEmployee = new Employee("Inactive", "O", "User", LocalDate.parse("1985-01-02"), LocalDate.parse("2018-06-03"), EmployeeStatus.INACTIVE);

        this.employeeService.save(this.employeeOne);
        this.employeeService.save(this.employeeTwo);
        this.employeeService.save(this.inactiveEmployee);
    }

    /**
     * Ensure that we can retrieve a list of all employees and confirm that the data is what is expected. This should
     * only return active employees
     * @throws Exception
     */
    @Test
    public void getEmployees() throws Exception {
        mockMvc.perform(get(this.endpoint))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(this.employeeOne.getId().intValue())))
                .andExpect(jsonPath("$[0].firstName", is("First")))
                .andExpect(jsonPath("$[0].middleInitial", is("O")))
                .andExpect(jsonPath("$[0].lastName", is("User")))
                .andExpect(jsonPath("$[0].dateOfBirth", is("1985-01-02")))
                .andExpect(jsonPath("$[0].dateOfEmployment", is("2018-06-03")))
                .andExpect(jsonPath("$[0].status", is("ACTIVE")))
                .andExpect(jsonPath("$[1].id", is(this.employeeTwo.getId().intValue())))
                .andExpect(jsonPath("$[1].firstName", is("Second")))
                .andExpect(jsonPath("$[1].middleInitial", is("O")))
                .andExpect(jsonPath("$[1].lastName", is("User")))
                .andExpect(jsonPath("$[1].dateOfBirth", is("1985-01-02")))
                .andExpect(jsonPath("$[1].dateOfEmployment", is("2018-06-03")))
                .andExpect(jsonPath("$[1].status", is("ACTIVE")));
    }

    /**
     * Ensure that we can retrieve a single employee by ID and confirm that the data is what we expect.
     * @throws Exception
     */
    @Test
    public void getSingleEmployee() throws Exception {
        mockMvc.perform(get(this.endpoint
                + this.employeeOne.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.employeeOne.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("First")))
                .andExpect(jsonPath("$.middleInitial", is("O")))
                .andExpect(jsonPath("$.lastName", is("User")))
                .andExpect(jsonPath("$.dateOfBirth", is("1985-01-02")))
                .andExpect(jsonPath("$.dateOfEmployment", is("2018-06-03")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));
    }

    /**
     * Ensure that we can't retrieve a single employee if that employee is inactive.
     * @throws Exception
     */
    @Test
    public void getSingleInactiveEmployee() throws Exception {
        mockMvc.perform(get(this.endpoint
                + this.inactiveEmployee.getId()))
                .andExpect(status().isNotFound());
    }

    /**
     * Confirm that we can add a new employee.
     * @throws Exception
     */
    @Test
    public void addEmployee() throws Exception {
        String employeeJson = json(new Employee(
                "Third", "A", "User", LocalDate.parse("1985-01-02"), LocalDate.parse("2018-06-03"), EmployeeStatus.ACTIVE));

        this.mockMvc.perform(post(this.endpoint)
                .contentType(this.contentType)
                .content(employeeJson))
                .andExpect(status().isCreated());
    }

    /**
     * Confirm that we can update an existing employee and that the changes persist.
     * @throws Exception
     */
    @Test
    public void updateEmployee() throws Exception {
        String employeeJson = json(new Employee(
                "First", "N", "User", LocalDate.parse("1985-01-02"), LocalDate.parse("2018-06-03"), EmployeeStatus.ACTIVE));

        // confirm update call is accepted.
        this.mockMvc.perform(put(this.endpoint
                + this.employeeOne.getId())
                .contentType(this.contentType)
                .content(employeeJson))
                .andExpect(status().isOk());

        // confirm changes were made
        this.mockMvc.perform(get(this.endpoint
                + this.employeeOne.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(this.employeeOne.getId().intValue())))
                .andExpect(jsonPath("$.firstName", is("First")))
                .andExpect(jsonPath("$.middleInitial", is("N")))
                .andExpect(jsonPath("$.lastName", is("User")))
                .andExpect(jsonPath("$.dateOfBirth", is("1985-01-02")))
                .andExpect(jsonPath("$.dateOfEmployment", is("2018-06-03")))
                .andExpect(jsonPath("$.status", is("ACTIVE")));;
    }

    /**
     * Confirm that trying to delete an employee without being authorized isn't allowed.
     * @throws Exception
     */
    @Test
    public void deleteEmployeeWithoutAuth() throws Exception {
        this.mockMvc.perform(delete(this.endpoint
                + this.employeeOne.getId()))
                .andExpect(status().isUnauthorized());
    }

    /**
     * Confirm that an authorized user can delete an employee. This shouldn't actually delete the employee from our
     * records and should only mark the employee as INACTIVE.
     * @throws Exception
     */
    @Test
    @WithMockUser(username = username, roles = "USER")
    public void deleteEmployee() throws Exception {
        Long employeeId = this.employeeOne.getId();

        this.mockMvc.perform(delete(this.endpoint + employeeId))
                .andExpect(status().isAccepted());

        // confirm we can't return the employee from the API
        this.mockMvc.perform(get(this.endpoint + employeeId))
                .andExpect(status().isNotFound());

        // confirm that the employee record exists with an inactive status
        assertEquals(this.employeeRepository.findById(employeeId).get().getStatus(), EmployeeStatus.INACTIVE);
    }

    /**
     *  Convert a POJO to a json representation.
     *
     * @param o - POJO
     * @return json string of supplied object
     * @throws IOException
     */
    private String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }
}
