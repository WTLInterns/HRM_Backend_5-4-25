package com.jaywant.Service;

import com.jaywant.DTO.SalaryDTO;
import com.jaywant.Model.AddEmployee;
import com.jaywant.Model.Attendance;
import com.jaywant.Repo.AddEmployeeRepo;
import com.jaywant.Repo.AttendanceRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class SalaryService {

    @Autowired
    private AddEmployeeRepo addEmpRepo;

    @Autowired
    private AttendanceRepo attendanceRepo;

    public SalaryDTO generateSalaryReport(String employeeName, String startDate, String endDate) {
        // Find employee by name
        AddEmployee employee = addEmpRepo.findByFirstName(employeeName);
        if (employee == null) {
            throw new IllegalArgumentException("No employee found with name: " + employeeName);
        }

        // Fetch annual CTC from employee's salary field
        double annualCtc = employee.getSalary();
        if (annualCtc <= 0) {
            throw new IllegalArgumentException("Invalid salary for employee: " + employeeName);
        }

        // Parse dates
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        // Determine working days in the month
        int workingDays = start.lengthOfMonth(); // Auto-adjusts for 28, 30, 31 days

        // Fetch attendance records
        List<Attendance> attendances = attendanceRepo.findByEmployeeEmpIdAndDateBetween(
            employee.getEmpId(), startDate, endDate
        );

        // Salary calculations
        double monthlyCtc = annualCtc / 12.0;
        double basic = monthlyCtc * 0.50;
        double hra = basic * 0.10;
        double da = basic * 0.53;
        double special = basic * 0.37;
        double totalAllowance = hra + da + special;
        double grossSalary = basic + totalAllowance;
        double professionalTax = monthlyCtc * 0.02;
        double tds = 0.0;

        // Attendance tracking
        double presentDays = 0;
        int halfDays = 0, leaveTaken = 0, leaveAllowed = 0, weekOffs = 0, holidays = 0;

        for (Attendance att : attendances) {
            switch (att.getStatus().trim()) {
                case "Present":    presentDays++; break;
                case "Absent":     leaveTaken++; break;
                case "Half-Day":   halfDays++; break;
                case "Week Off":   weekOffs++; break;
                case "Holiday":    holidays++; break;
                case "Paid Leave": leaveAllowed++; break;
            }
        }

        presentDays += (0.5 * halfDays); // Half-days count as 0.5

        // Payable days now include weekends & holidays
        double payableDays = presentDays + leaveAllowed + weekOffs + holidays;

        // Salary deductions
        double perDayRate = monthlyCtc / workingDays;
        double dayBasedDeduction = (workingDays - payableDays) * perDayRate;
        double totalDeductions = dayBasedDeduction + professionalTax + tds;
        double netPayable = Math.max(grossSalary - totalDeductions, 0);

        // Build SalaryDTO
        SalaryDTO dto = new SalaryDTO();
        dto.setFirstName(employee.getFirstName());
        dto.setLastName(employee.getLastName());
        dto.setEmail(employee.getEmail());
        dto.setJobRole(employee.getJobRole());
        dto.setBankName(employee.getBankName());
        dto.setBankAccountNo(employee.getBankAccountNo());
        dto.setBranchName(employee.getBranchName());
        dto.setIfscCode(employee.getBankIfscCode());
        dto.setUid(String.valueOf(employee.getEmpId()));


        dto.setWorkingDays(workingDays);
        dto.setPayableDays(payableDays);
        dto.setLeaveTaken(leaveTaken);
        dto.setWeekoff(weekOffs);
        dto.setHalfDay(halfDays);
        dto.setHoliday(holidays);
        dto.setLeaveAllowed(leaveAllowed);

        dto.setBasic(basic);
        dto.setHra(hra);
        dto.setDaAllowance(da);
        dto.setSpecialAllowance(special);
        dto.setTotalAllowance(totalAllowance);
        dto.setGrossSalary(grossSalary);
        dto.setProfessionalTax(professionalTax);
        dto.setTds(tds);
        dto.setAdvance(dayBasedDeduction);
        dto.setTotalDeductions(totalDeductions);
        dto.setNetPayable(netPayable);

        dto.setPerDaySalary(perDayRate);
        dto.setTotalPayout(grossSalary);

        return dto;
    }

    
    public List<AddEmployee> getAllEmployee() {
        return addEmpRepo.findAll();
    }
}
