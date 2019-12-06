package eblink;

import java.io.IOException;



public class Shutdownc extends Home {
	
	public static void main(String arg[]){
		
		{
		try
		{
	    Runtime runtime = Runtime.getRuntime();
	     runtime.exec("shutdown -s -t 60");
	     playsound();
		}
		catch(Exception e)
		{
			e.printStackTrace();
			
		}
	     
	    System.exit(0);
	}
}
}
