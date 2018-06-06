package com.matt.employees.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@ApiModel
public class Employee {

    @Id
    @GeneratedValue
    @ReadOnlyProperty
    @ApiModelProperty(example="1")
    private Long id;

    @ApiModelProperty(example="Bob")
    private String firstName;
    @ApiModelProperty(example="N")
    private String middleInitial;
    @ApiModelProperty(example="Ross")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(example="1942-10-29")
    private LocalDate dateOfBirth;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @ApiModelProperty(example="2006-06-06")
    private LocalDate dateOfEmployment;

    @ApiModelProperty(example="ACTIVE")
    @Enumerated(EnumType.STRING)
    private EmployeeStatus status;

    private Employee() { }

    public Employee(final String firstName, final String middleInitial, final String lastName,
                    final LocalDate dateOfBirth, final LocalDate dateOfEmployment, final EmployeeStatus status) {

        this.firstName = firstName;
        this.middleInitial = middleInitial;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.dateOfEmployment = dateOfEmployment;
        this.status = status;
    }

    public Long getId() {
        return this.id;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleInitial() {
        return this.middleInitial;
    }

    public void setMiddleInitial(String middleInitial) {
        this.middleInitial = middleInitial;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return this.dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public LocalDate getDateOfEmployment() {
        return this.dateOfEmployment;
    }

    public void setDateOfEmployment(LocalDate dateOfEmployment) {
        this.dateOfEmployment = dateOfEmployment;
    }

    public EmployeeStatus getStatus() {
        return this.status;
    }

    public void setStatus (EmployeeStatus status) {
        this.status = status;
    }
}
