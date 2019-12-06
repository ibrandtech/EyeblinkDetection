package eblink;
import org.opencv.core.*;
import org.opencv.videoio.*;
import org.opencv.imgcodecs.*;

public class VideoCap {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		VideoCapture camera=new VideoCapture(3);
		if(!camera.isOpened())
		{
			System.out.println("ERROR....");	
		}
		else
		{
			Mat frame=new Mat();
			while(true)
			{
				if(camera.read(frame))
				{
					System.out.println("Frame Obtained");
					System.out.println("Captured Frame Width:"+frame.width()+" Height:"+frame.height());
					Imgcodecs.imwrite("cameracap.jpg", frame);
					System.out.println("OK");
					break;
				}
			}
		}
		camera.release();
	}

}
