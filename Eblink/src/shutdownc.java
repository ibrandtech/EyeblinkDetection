
import java.io.IOException;



public class shutdownc {
	
	public static void main(String arg[]) throws IOException{
	    Runtime runtime = Runtime.getRuntime();
	     runtime.exec("shutdown -s -t 60");
	     
	    System.exit(0);
	}

}
