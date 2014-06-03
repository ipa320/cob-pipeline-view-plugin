package jenkins.plugins.CobPipelineViewTable;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Timestamp;
import java.util.concurrent.TimeUnit;


public class MethodsGeneral {

	// build array out of string
	public String[] getSplitString(String str0, String pattern){
		String[] output = str0.split(pattern);		
		return output;
	}	

	// get first part of a string
	public String getFirstPartOutOfString(String str1, String pattern){
		try{
			return str1.substring(0,str1.indexOf(pattern));
		}
		catch(Exception e){
			return null;
		}
	}
	
	// get last part of a string
	public String getLastPartOutOfString(String str1, String pattern, Integer offset){
		try{
			return str1.substring(str1.indexOf(pattern) + offset);
		}
		catch(Exception e){
			return null;
		}
	}
	
	// search keyword in string
	public Boolean isKeyWordInString(String keyword, String str1){		
		Boolean result = false;		
		
		if(str1.toLowerCase().contains(keyword.toLowerCase())) {
			result = true;
		}
		
		return result;
	}	
	
	// delete whitespace in string
	public String delKeyWordOutOfString(String str1, String pattern){
		String output;
		
		output = str1.replace(pattern, ""); 	// one whitespace
		
		return output;		
	}
	
	// merge two strings with pattern
	public String mergeString(String str1, String str2, String pattern){	
		return str1 + pattern + str2;
	}
	
	// get current time
	public Long getTimeCurrent(){
		Timestamp time_current = new Timestamp(System.currentTimeMillis());	// timestamp		
		return time_current.getTime();	// time in milliseconds since 01.01.1970
	}
	
	// get time difference as days, hours, minutes, seconds
	public String getTimeDifference(Long time1, Long time2){	//time1 > time2; times in milliseconds
		String output = "";
		String output_seconds = "";
		String output_minutes = "";
		String output_hours = "";
		String output_days = "";
		String output_months = "";
			
		Long diffInSec;
		Long seconds;
		Long minutes;
		Long hours;
		Long days;
		Long months;
			
		diffInSec = TimeUnit.MILLISECONDS.toSeconds(time1 - time2);		// change time difference in milliseconds to seconds	
		seconds = diffInSec % 60;
		diffInSec /= 60;
		minutes = diffInSec % 60;
		diffInSec /= 60;
		hours	= diffInSec % 24;
		diffInSec /= 24;
		days 	= diffInSec % 30;
		diffInSec /= 30;
		months 	= diffInSec;

		if(seconds != 0L)
			output_seconds = seconds + "sec ";
		
		if(minutes != 0L)
			output_minutes = minutes + "min ";
		
		if(hours != 0L)
			output_hours = hours + "hr ";
		
		if(days != 0L)
			output_days = days + "days ";		
		
		if(months != 0L)
			output_months = months + "mo ";
		
		output = output_months + output_days + output_hours + output_minutes + output_seconds;
				
		return output;
	}
	
	
	// check url connection
	public String checkUrl(String str_url){   
	    /*
		try {
	        URL url = new URL(str_url);
	        URLConnection conn = url.openConnection();
	        conn.connect();
	        return str_url;
	    } catch (MalformedURLException e) {
	    	return "malformed";
	    } catch (IOException e) {
	        return "ioexception";
	    }	*/
		
		/*
		 try {
		      HttpURLConnection.setFollowRedirects(false);
		      // note : you may also need
		            //HttpURLConnection.setInstanceFollowRedirects(false)
		      HttpURLConnection con = (HttpURLConnection) new URL(str_url).openConnection();
		      con.setRequestMethod("HEAD");
		      return Boolean.toString((con.getResponseCode() == HttpURLConnection.HTTP_OK));
		    }
		    catch (Exception e) {
		       return str_url;
		    }
		    */
		/*
		HttpURLConnection httpUrlConn;
        try {
            httpUrlConn = (HttpURLConnection) new URL(str_url)
                    .openConnection();
 
            // A HEAD request is just like a GET request, except that it asks
            // the server to return the response headers only, and not the
            // actual resource (i.e. no message body).
            // This is useful to check characteristics of a resource without
            // actually downloading it,thus saving bandwidth. Use HEAD when
            // you don't actually need a file's contents.
            httpUrlConn.setRequestMethod("HEAD");
 
            // Set timeouts in milliseconds
            httpUrlConn.setConnectTimeout(30000);
            httpUrlConn.setReadTimeout(30000);
 
            // Print HTTP status code/message for your information.
            System.out.println("Response Code: "
                    + httpUrlConn.getResponseCode());
            System.out.println("Response Message: "
                    + httpUrlConn.getResponseMessage());
 
            return Boolean.toString((httpUrlConn.getResponseCode() == HttpURLConnection.HTTP_OK));
        } catch (Exception e) {
            //System.out.println("Error: " + e.getMessage());
            return "";
        }*/
	    
	    return str_url;
	    
	}
	
	
}
