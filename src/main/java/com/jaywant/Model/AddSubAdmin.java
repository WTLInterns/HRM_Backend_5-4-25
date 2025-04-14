package com.jaywant.Model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "SubAdmin")
public class AddSubAdmin {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private int id;
  private String name;
  private String lastname;
  private String stampImg;
  private String signature; // Renamed for consistency.
  private String email;
  private String phoneno;
  private String password;
  private String registercompanyname;
  private String companylogo;
  private String role = "SUB_ADMIN";  
  private String status;
  

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  @OneToMany(mappedBy = "addSubAdmin")
  private List<AddEmployee> employees;

  @JsonIgnore
  private Collection<? extends GrantedAuthority> authorities;

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

  public String getStampImg() {
    return stampImg;
  }

  public void setStampImg(String stampImg) {
    this.stampImg = stampImg;
  }

  public String getSignature() {
    return signature;
  }

  public void setSignature(String signature) {
    this.signature = signature;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPhoneno() {
    return phoneno;
  }

  public void setPhoneno(String phoneno) {
    this.phoneno = phoneno;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getRegistercompanyname() {
    return registercompanyname;
  }

  public void setRegistercompanyname(String registercompanyname) {
    this.registercompanyname = registercompanyname;
  }

  public String getCompanylogo() {
    return companylogo;
  }

  public void setCompanylogo(String companylogo) {
    this.companylogo = companylogo;
  }

  public String getRole() {
    return role;
  }

  public void setRole(String role) {
    this.role = role;
  }

  public List<AddEmployee> getEmployees() {
    return employees;
  }

  public void setEmployees(List<AddEmployee> employees) {
    this.employees = employees;
  }
}
