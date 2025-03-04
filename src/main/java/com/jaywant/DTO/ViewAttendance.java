package com.jaywant.DTO;

public class ViewAttendance {

	private String email;
	
	private int Present;
	
	private int Absent;
	
	private int HalfDay;
	
	private int PaidLeave;
	
	private int WeekOff;
	
	private int holiday;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getPresent() {
		return Present;
	}

	public void setPresent(int present) {
		Present = present;
	}

	public int getAbsent() {
		return Absent;
	}

	public void setAbsent(int absent) {
		Absent = absent;
	}

	public int getHalfDay() {
		return HalfDay;
	}

	public void setHalfDay(int halfDay) {
		HalfDay = halfDay;
	}

	public int getPaidLeave() {
		return PaidLeave;
	}

	public void setPaidLeave(int paidLeave) {
		PaidLeave = paidLeave;
	}

	public int getWeekOff() {
		return WeekOff;
	}

	public void setWeekOff(int weekOff) {
		WeekOff = weekOff;
	}

	public int getHoliday() {
		return holiday;
	}

	public void setHoliday(int holiday) {
		this.holiday = holiday;
	}
	
	
}
