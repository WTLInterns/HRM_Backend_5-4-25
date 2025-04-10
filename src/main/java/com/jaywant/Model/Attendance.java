// package com.jaywant.Model;

// import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

// import jakarta.persistence.Entity;
// import jakarta.persistence.GeneratedValue;
// import jakarta.persistence.GenerationType;
// import jakarta.persistence.Id;
// import jakarta.persistence.JoinColumn;
// import jakarta.persistence.ManyToOne;

// @Entity
// public class Attendance {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id; 

//     // Use @JsonIgnoreProperties to avoid serializing the employee's attendance list
//     @ManyToOne
//     @JoinColumn(name = "employee_id", nullable = false)
//     @JsonIgnoreProperties("attendance")
//     private AddEmployee employee;

//     private String date;
//     private String status;

//     public Attendance() {
//         super();
//     }

//     public Attendance(Long id, AddEmployee employee, String date, String status) {
//         super();
//         this.id = id;
//         this.employee = employee;
//         this.date = date;
//         this.status = status;
//     }

//     // Getters and setters
//     public Long getId() {
//         return id;
//     }
//     public void setId(Long id) {
//         this.id = id;
//     }
//     public AddEmployee getEmployee() {
//         return employee;
//     }
//     public void setEmployee(AddEmployee employee) {
//         this.employee = employee;
//     }
//     public String getDate() {
//         return date;
//     }
//     public void setDate(String date) {
//         this.date = date;
//     }
//     public String getStatus() {
//         return status;
//     }
//     public void setStatus(String status) {
//         this.status = status;
//     }
// }

package com.jaywant.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "Attendance")
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // For simplicity, store the date as a String (format: yyyy-MM-dd)
    private String date;
    private String status; // Example statuses: "Present", "Absent", "Half-Day", etc.

    @ManyToOne
    private AddEmployee employee;

    // ------------------------------------------------
    // Transient fields computed from relationships
    // ------------------------------------------------

    @Transient
    private Integer employeeId;

    @Transient
    private String employeeName;

    @Transient
    private Integer subAdminId;

    @Transient
    private String subAdminName;

    @Transient
    private String registerCompanyName;

    public Attendance() {
        // Default constructor
    }

    // ------------------------
    // Getters and Setters
    // ------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public AddEmployee getEmployee() {
        return employee;
    }

    public void setEmployee(AddEmployee employee) {
        this.employee = employee;
    }

    // ------------------------
    // Transient computed getters
    // ------------------------

    public Integer getEmployeeId() {
        if (employee != null) {
            return employee.getEmpId();
        }
        return null;
    }

    public String getEmployeeName() {
        if (employee != null) {
            // Combine firstName and lastName
            return employee.getFirstName() + " " + employee.getLastName();
        }
        return null;
    }

    public Integer getSubAdminId() {
        if (employee != null && employee.getAddSubAdmin() != null) {
            return employee.getAddSubAdmin().getId();
        }
        return null;
    }

    public String getSubAdminName() {
        if (employee != null && employee.getAddSubAdmin() != null) {
            // Combine sub admin's first and last names
            return employee.getAddSubAdmin().getName() + " " + employee.getAddSubAdmin().getLastname();
        }
        return null;
    }

    public String getRegisterCompanyName() {
        // You can choose to return the company value from the employee or
        // the registered company name from the subadmin.
        if (employee != null) {
            // If employee.getCompany() is maintained, you can return that.
            // Otherwise, check subadmin:
            if (employee.getCompany() != null && !employee.getCompany().isBlank()) {
                return employee.getCompany();
            } else if (employee.getAddSubAdmin() != null) {
                return employee.getAddSubAdmin().getRegistercompanyname();
            }
        }
        return null;
    }

    // Optionally, you can also add setters for transient fields
    // if you need to set them manually in certain cases.
    // However, in most cases these are computed values.
}
