package com.jaywant.Controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.jaywant.DTO.SalaryDTO;
import com.jaywant.Model.AddEmployee;
import com.jaywant.Model.Attendance;
import com.jaywant.Service.AttendaceService;
import com.jaywant.Service.EmployeeService;
import com.jaywant.Service.SalaryService;

@RestController
@RequestMapping("/public")
@CrossOrigin(origins = "http://localhost:3000")
public class AdminController {

    @Autowired
    private EmployeeService empService;
    
//    @Autowired
//    private AddEmployee 

    @Autowired
    private AttendaceService attService;

    @Autowired
    private SalaryService salaryService;

    @PostMapping("/addEmp")
    public AddEmployee addEmployee(@RequestBody AddEmployee addEmp) {
        return this.empService.addEmp(addEmp);
    }

    // When marking attendance, we use createOrUpdateAttendance so that if an attendance record already exists for the same date, it updates the status.
    @PostMapping("/att")
    public ResponseEntity<Attendance> createOrUpdateAttendance(@RequestBody Attendance attendance) {
        try {
            Attendance updatedAttendance = attService.createOrUpdateAttendance(attendance);
            return new ResponseEntity<>(updatedAttendance, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllAtt")
    public ResponseEntity<List<Attendance>> getAllAttendace() {
        try {
            List<Attendance> attendanceList = attService.getAllAttendace();
            return ResponseEntity.ok(attendanceList);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }

    @GetMapping("/getAllEmp")
    public List<AddEmployee> getAllEmp(){
        return this.empService.getAllEmployee();
    }

    @GetMapping("/deleteAddEmp/{empId}")
    public void deleteAddEmployee(@PathVariable int empId) {
        this.empService.addEmployeeDelete(empId);
        System.out.println("deletd sucess" + empId);

        
    }

    @GetMapping("/generateReport")
    public SalaryDTO generateSalaryReport(@RequestParam String employeeName,
                                          @RequestParam String startDate,
                                          @RequestParam String endDate) {
        System.out.println("generateReport called with: employeeName=" + employeeName +
                           ", startDate=" + startDate + ", endDate=" + endDate);
        return salaryService.generateSalaryReport(employeeName, startDate, endDate);
    }

    @GetMapping("/getAllAttendace/{empId}")
    public List<Attendance> getAttendanceByEmployee(@PathVariable int empId) {
        return attService.getAllAttendance(empId);
    }

    
    
    
    @GetMapping("/getAttendance/{input}")
    public ResponseEntity<List<Attendance>> getAttendance(@PathVariable String input) {
        List<Attendance> attendanceList;

        // Check if input is a number (empId) or a name (firstName)
        if (input.matches("\\d+")) {  // Check if input is a number (empId)
            int empId = Integer.parseInt(input);
            attendanceList = attService.getAllAttendance(empId);
        } else {
            attendanceList = attService.getAttendanceByEmployeeName(input);
        }

        if (attendanceList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(attendanceList);
        }
        
        return ResponseEntity.ok(attendanceList);
    }

    @DeleteMapping("/deleteEmp/{empId}")
    public void deleteEmpById(@PathVariable int empId) {
        this.empService.deleteEmpId(empId);
        System.out.println("deletd sucess" + empId);
    }

    @PutMapping("/update/{empId}")
    public AddEmployee updateEmp(@PathVariable int empId, @RequestBody AddEmployee addEmp) {
        return this.empService.updateEmployee(addEmp, empId);
        
    }

    @GetMapping("/getAllSalary")
    public List<AddEmployee> getAllSalaries() {
        return this.salaryService.getAllEmployee();
    }
    
    @GetMapping("/getAttendanceByName/{firstName}")
    public ResponseEntity<List<Attendance>> getAttendanceByName(@PathVariable String firstName) {
        List<Attendance> attendanceList = attService.getAttendanceByEmployeeName(firstName);
        return ResponseEntity.ok(attendanceList);
    }

    // Update attendance status for a specific record by its id
    @PutMapping("/updateAttendanceStatus/{id}")
    public ResponseEntity<Attendance> updateAttendanceStatus(
            @PathVariable Long id, 
            @RequestParam String status) {
        try {
            Attendance updatedAttendance = attService.updateAttendanceStatus(id, status);
            return ResponseEntity.ok(updatedAttendance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    // Update attendance status by employee id and date (assumes unique record per employee per date)
    @PutMapping("/updateAttendanceStatusByEmpAndDate/{empId}/{date}/{newStatus}")
    public ResponseEntity<Attendance> updateAttendanceStatusByEmpAndDate(
            @PathVariable int empId,
            @PathVariable String date,
            @PathVariable String newStatus) {
        try {
            Attendance updatedAttendance = attService.updateAttendanceStatusByEmployeeAndDate(empId, date, newStatus);
            return ResponseEntity.ok(updatedAttendance);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    
    @GetMapping("/find/{name}")
    public AddEmployee findByName(@PathVariable String name) {
    	return this.empService.findByEmployeeName(name);
    }
}
