package com.jaywant.DTO;

import java.util.List;
import com.jaywant.Model.AddEmployee;

public class SubAdminWithEmployeesDTO {

  private int id;
  private String name;
  private String lastname;
  private String registercompanyname;
  private List<AddEmployee> employees;

  // Constructors
  public SubAdminWithEmployeesDTO() {
  }

  public SubAdminWithEmployeesDTO(int id, String name, String lastname, String registercompanyname,
      List<AddEmployee> employees) {
    this.id = id;
    this.name = name;
    this.lastname = lastname;
    this.registercompanyname = registercompanyname;
    this.employees = employees;
  }

  // Getters and setters

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getLastname() {
    return lastname;
  }

  public void setLastname(String lastname) {
    this.lastname = lastname;
  }

  public String getRegistercompanyname() {
    return registercompanyname;
  }

  public void setRegistercompanyname(String registercompanyname) {
    this.registercompanyname = registercompanyname;
  }

  public List<AddEmployee> getEmployees() {
    return employees;
  }

  public void setEmployees(List<AddEmployee> employees) {
    this.employees = employees;
  }
}
