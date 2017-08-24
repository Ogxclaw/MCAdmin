package com.kirik.zen.config.time;

public class Days {
	
	public static int findDay(String day){
		if(day.equals("Sunday")){
			return 1;
		}else if(day.equals("Monday")){
			return 2;
		}else if(day.equals("Tuesday")){
			return 3;
		}else if(day.equals("Wednesday")){
			return 4;
		}else if(day.equals("Thursday")){
			return 5;
		}else if(day.equals("Friday")){
			return 6;
		}else if(day.equals("Saturday")){
			return 7;
		}else{
			return 0;
		}
	}
	
	public static String getDayFromInteger(int day){
		if(day == 1){
			return "Sunday";
		}else if(day == 2){
			return "Monday";
		}
		else if(day == 3){
			return "Tuesday";
		}
		else if(day == 4){
			return "Wednesday";
		}
		else if(day == 5){
			return "Thursday";
		}
		else if(day == 6){
			return "Friday";
		}
		else if(day == 7){
			return "Saturday";
		}else{
			return "";
		}
	}
	
	public static int addDays(int day1, int day2){
		int day3 = day1 + day2;
		while(day3 > 365){
			day3 -= 365;
		}
		return day3;
	}

}
