package com.jaywant.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    // Use @JsonIgnoreProperties to avoid serializing the employee's attendance list
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonIgnoreProperties("attendance")
    private AddEmployee employee;

    private String date;
    private String status;

    public Attendance() {
        super();
    }

    public Attendance(Long id, AddEmployee employee, String date, String status) {
        super();
        this.id = id;
        this.employee = employee;
        this.date = date;
        this.status = status;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public AddEmployee getEmployee() {
        return employee;
    }
    public void setEmployee(AddEmployee employee) {
        this.employee = employee;
    }
    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }
    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
