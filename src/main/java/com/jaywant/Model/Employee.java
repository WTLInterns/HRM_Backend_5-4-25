package com.jaywant.Model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Employee implements UserDetails{

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private int empId;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String password;
	
	private Long phone;
	
	public String role;
	
	private String aadharNo;
    private String panCard;
    private String education;
    private String bloodGroup;
    private String jobRole;
    private String gender;
    private String address;
    private String birthDate;
    private String joiningDate;
    private String status;
    private String bankName;
    private Long bankAccountNo;
    private String bankIfscCode;
    private String branchName;
    private Long salary;
    
    
	
	public Employee() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Employee(int empId, String firstName, String lastName, String email, String password, Long phone,
			String role, String aadharNo, String panCard, String education, String bloodGroup, String jobRole,
			String gender, String address, String birthDate, String joiningDate, String status, String bankName,
			Long bankAccountNo, String bankIfscCode, String branchName, Long salary) {
		super();
		this.empId = empId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.password = password;
		this.phone = phone;
		this.role = role;
		this.aadharNo = aadharNo;
		this.panCard = panCard;
		this.education = education;
		this.bloodGroup = bloodGroup;
		this.jobRole = jobRole;
		this.gender = gender;
		this.address = address;
		this.birthDate = birthDate;
		this.joiningDate = joiningDate;
		this.status = status;
		this.bankName = bankName;
		this.bankAccountNo = bankAccountNo;
		this.bankIfscCode = bankIfscCode;
		this.branchName = branchName;
		this.salary = salary;
	}



	public int getEmployeeId() {
		return empId;
	}

	public void setEmpoyeeId(int empId) {
		this.empId = empId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	

	public Long getPhone() {
		return phone;
	}

	public void setPhone(Long phone) {
		this.phone = phone;
	}
	
	
	
	

	
	
	

	

	

	public int getEmpId() {
		return empId;
	}

	public void setEmpId(int empId) {
		this.empId = empId;
	}

	public String getAadharNo() {
		return aadharNo;
	}

	public void setAadharNo(String aadharNo) {
		this.aadharNo = aadharNo;
	}

	public String getPanCard() {
		return panCard;
	}

	public void setPanCard(String panCard) {
		this.panCard = panCard;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getJobRole() {
		return jobRole;
	}

	public void setJobRole(String jobRole) {
		this.jobRole = jobRole;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(String birthDate) {
		this.birthDate = birthDate;
	}

	public String getJoiningDate() {
		return joiningDate;
	}

	public void setJoiningDate(String joiningDate) {
		this.joiningDate = joiningDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public Long getBankAccountNo() {
		return bankAccountNo;
	}

	public void setBankAccountNo(Long bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	public String getBankIfscCode() {
		return bankIfscCode;
	}

	public void setBankIfscCode(String bankIfscCode) {
		this.bankIfscCode = bankIfscCode;
	}

	public String getBranchName() {
		return branchName;
	}

	public void setBranchName(String branchName) {
		this.branchName = branchName;
	}

	public Long getSalary() {
		return salary;
	}

	public void setSalary(Long salary) {
		this.salary = salary;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return List.of(new SimpleGrantedAuthority("EMPLOYEE"));
	}

	@Override
	public String getUsername() {
		return email;
	}

	
	
	
}
