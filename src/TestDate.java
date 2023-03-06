import java.util.*; 
import java.text.*; 


public class TestDate { 

	static public void main(String[] args) throws Exception { 

		String inputDate = "20030810"; 

		DateFormat df = new SimpleDateFormat("yyyyMMdd"); 
		Date myDate = df.parse(inputDate); 
		Calendar cld = Calendar.getInstance(); 
		cld.setTime(myDate); 

		if(cld.get(Calendar.DAY_OF_WEEK) == 1) 
			System.out.println("일요일입니다."); 
		}
} 
