package com.example.anl2;

public class DayEvent {

	private int year;
	private int month;
	private int day;
	private int hour;
	private String event;

	public DayEvent(int year, int month, int day, int hour, String event) {
		super();
		this.year	= year;
		this.month	= month;
		this.day	= day;
		this.hour	= hour;
		this.event	= event;
	}
	
	public int getYear() {
		return this.year;
	}
	
	public int getMonth() {
		return this.month;
	}
	
	public int getDay() {
		return this.day;
	}
	
	public int getHour() {
		return this.hour;
	}
	
	public String getTime() {
		return (this.hour > 12 ? this.hour - 12 : this.hour) 
				+ "\n"
				+ (this.hour > 12 ? "pm" : "am");
	}

	public String getEvent() {
		return this.event;
	}

}