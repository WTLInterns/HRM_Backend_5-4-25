package com.jaywant.Model;

import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "Employee")
public class AddEmployee implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int empId;

    private String firstName;
    private String lastName;
    private String email;
    private Long phone;
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
    private String bankAccountNo;
    private String bankIfscCode;
    private String branchName;
    private Long salary;
    private String password;
    private String roll = "EMPLOYEE";
    private String company;

    @JsonIgnore
    @ManyToOne
    private AddSubAdmin addSubAdmin;

    @JsonIgnore
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Attendance> attendance;

    public AddEmployee() {
        // Default constructor
    }

    public AddEmployee(int empId, String firstName, String lastName, String email, Long phone, String aadharNo,
            String panCard, String education, String bloodGroup, String jobRole, String gender,
            String address, String birthDate, String joiningDate, String status, String bankName,
            String bankAccountNo, String bankIfscCode, String branchName, Long salary, String password,
            String company, List<Attendance> attendance) {
        this.empId = empId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
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
        this.password = password;
        this.company = company;
        this.attendance = attendance;
    }

    // Getters and setters
    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
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

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
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

    public String getBankAccountNo() {
        return bankAccountNo;
    }

    public void setBankAccountNo(String bankAccountNo) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoll() {
        return roll;
    }

    public void setRoll(String roll) {
        this.roll = roll;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public AddSubAdmin getAddSubAdmin() {
        return addSubAdmin;
    }

    public void setAddSubAdmin(AddSubAdmin addSubAdmin) {
        this.addSubAdmin = addSubAdmin;
    }

    public List<Attendance> getAttendance() {
        return attendance;
    }

    public void setAttendance(List<Attendance> attendance) {
        this.attendance = attendance;
    }

    // --- UserDetails interface methods ---
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(roll));
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
