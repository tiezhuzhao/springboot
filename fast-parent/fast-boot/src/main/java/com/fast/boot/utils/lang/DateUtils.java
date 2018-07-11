package com.fast.boot.utils.lang;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	  public static final DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
	  public static final DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	  public static final DateFormat df3 = new SimpleDateFormat("yyyyMMddHHmmss");
	  public static final DateFormat df4 = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
	  public static final DateFormat df5 = new SimpleDateFormat("yyyy年MM月dd日");
	  public static final DateFormat df6 = new SimpleDateFormat("yyyy年");
	  public static final DateFormat df7 = new SimpleDateFormat("yyyy年MM月");
	  public static final DateFormat df8 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	  
	  public static String addMoth(Date date, String pattern, int num){
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    Calendar calender = Calendar.getInstance();
	    calender.setTime(date);
	    calender.add(2, num);
	    return simpleDateFormat.format(calender.getTime());
	  }
	  
	  public static String addDay(Date date, String pattern, int num){
	    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
	    Calendar calender = Calendar.getInstance();
	    calender.setTime(date);
	    calender.add(5, num);
	    return simpleDateFormat.format(calender.getTime());
	  }
	  
	  public static String getNowTime(){
	    return df2.format(new Date());
	  }
	  
	  public static String getNowTime(int df){
	    Date date = new Date();
	    switch (df){
		    case 1: 
		      return df1.format(date);
		    case 2: 
		      return df2.format(date);
		    case 3: 
		      return df3.format(date);
		    case 4: 
		      return df4.format(date);
		    case 5: 
		      return df5.format(date);
		    case 6: 
		      return df6.format(date);
		    case 7: 
		      return df7.format(date);
		    case 8: 
		      return df8.format(date);
	    }
	    return df2.format(date);
	  }
	  
	  public static String dateToString(Date date, int df){
	    switch (df){
		    case 1: 
		      return df1.format(date);
		    case 2: 
		      return df2.format(date);
		    case 3: 
		      return df3.format(date);
		    case 4: 
		      return df4.format(date);
		    case 5: 
		      return df5.format(date);
		    case 6: 
		      return df6.format(date);
		    case 7: 
		      return df7.format(date);
		    case 8: 
		      return df8.format(date);
	    }
	    return df2.format(date);
	  }
	  
	  public static Date stringToDate(String date, int df){
	    try{
	      switch (df){
		      case 1: 
		        return df1.parse(date);
		      case 2: 
		        return df2.parse(date);
		      case 3: 
		        return df3.parse(date);
		      case 4: 
		        return df4.parse(date);
		      case 5: 
		        return df5.parse(date);
		      case 6: 
		        return df6.parse(date);
		      case 7: 
		        return df7.parse(date);
		      case 8: 
		        return df8.parse(date);
	      }
	      return df2.parse(date);
	    }
	    catch (ParseException e) {}
	    return null;
	  }
	  
	  public static long minuteBetween(Date start, Date end){
	    long d1 = start.getTime();
	    long d2 = end.getTime();
	    long interval = (d2 - d1) / 1000L;
	    long minute = interval / 60L;
	    if (minute > 0L) {
	      return minute;
	    }
	    return 0L;
	  }
}
