package WebCamera;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;


//import eblink.Home.DaemonThread;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
//import eblink.Home.DaemonThread;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.awt.event.ActionEvent;
import java.awt.Font;
import javax.swing.JLabel;
import java.awt.Color;

public class Detection1 extends JFrame {

	private JPanel contentPane;
	
	private  DaemonThread mythread=null;
	int count=0;
	private int Bcount=0;
	//boolean isClose=false;

	VideoCapture websource=null;
	
	Mat frame1=new Mat();
	MatOfByte mem=new MatOfByte();
	MatOfRect Detections=new MatOfRect();
	
	JPanel panel = new JPanel();
	JButton btnStart = new JButton("Start");
	JButton btnStop = new JButton("Stop");
	JLabel capimg = new JLabel("");
	JLabel blbl = new JLabel("");
	
	CascadeClassifier BodyDetector = new CascadeClassifier(
			Detection1.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1).replace("%20", " "));
	
	
	class DaemonThread implements Runnable
    {
    protected volatile boolean runnable = false;

    @Override
    public  void run()
    {
        synchronized(this)
        {
            while(runnable)
            {
                if(websource.grab())
                {
		    	try
                        {
                            websource.retrieve(frame1);
                       
                            BodyDetector.detectMultiScale(frame1, Detections,1.1, 3, 0,new Size(), new Size());
                            
                            for(Rect recte:Detections.toArray())
							{								
							Imgproc.rectangle(frame1,new Point(recte.x,recte.y),new Point(recte.x+recte.width,recte.y+recte.height),new Scalar(255,0,255),3);
							}
                            Imgcodecs.imencode(".bmp", frame1, mem);
			                Image im = ImageIO.read(new ByteArrayInputStream(mem.toArray()));
             			    BufferedImage buff = (BufferedImage) im;
			                Graphics g=panel.getGraphics();

			    if (g.drawImage(buff, 0, 0, getWidth() - 800, getHeight() - 150, 0, 0, buff.getWidth(), buff.getHeight(),null))
			    {			    	
			    if(runnable == false)
                            {
			    	System.out.println("Going to wait");			    	
			    }
			 }
							
                        }
			 catch(Exception ex)
                         {
			    System.out.println("Error");
                         }
                }
            }
        }
     }
   }
	//////////////////////////////////////
	
	public static BufferedImage createAwtImage(Mat mat) {

	    int type = 0;
	    if (mat.channels() == 1) {
	        type = BufferedImage.TYPE_BYTE_GRAY;
	    } else if (mat.channels() == 3) {
	        type = BufferedImage.TYPE_3BYTE_BGR;
	    } else {
	        return null;
	    }

	    BufferedImage image = new BufferedImage(mat.width(), mat.height(), type);
	    WritableRaster raster = image.getRaster();
	    DataBufferByte dataBuffer = (DataBufferByte) raster.getDataBuffer();
	    byte[] data = dataBuffer.getData();
	    mat.get(0, 0, data);

	    return image;
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		System.load("C:\\opencv\\build\\x64\\vc14\\bin\\opencv_ffmpeg320_64.dll");
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Detection1 frame = new Detection1();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public Detection1() {
		setTitle("WebCam");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(50, 50, 1300, 678);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		panel.setBackground(Color.BLACK);
		
		
		panel.setBounds(24, 21, 503, 503);
		contentPane.add(panel);
		btnStart.setFont(new Font("Times New Roman", Font.BOLD, 30));
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//websource=new VideoCapture("F://Pictures//avcFace.mp4");
				websource=new VideoCapture(0);
				mythread=new DaemonThread();
				Thread t=new Thread(mythread);
				t.setDaemon(true);
				mythread.runnable=true;
				t.start();
				btnStart.setEnabled(false);
				btnStop.setEnabled(true);
			}
		});
		
		btnStart.setBounds(67, 571, 143, 42);
		contentPane.add(btnStart);
		btnStop.setFont(new Font("Times New Roman", Font.BOLD, 30));
		
		
		btnStop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				mythread.runnable=false;
				btnStop.setEnabled(false);
				btnStart.setEnabled(true);
				websource.release();
			}
		});
		btnStop.setBounds(333, 571, 143, 42);
		contentPane.add(btnStop);
		
		JButton capbtn = new JButton("Capture");
		capbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				capimg.setIcon(null);
				Imgcodecs.imwrite("test.jpg", frame1);
				 

				ImageIcon image = new ImageIcon("test.jpg");
				 
				 capimg.setIcon(image);
				 repaint();		
				}
		});
		capbtn.setFont(new Font("Times New Roman", Font.BOLD, 30));
		capbtn.setBounds(795, 571, 167, 42);
		contentPane.add(capbtn);
		capimg.setBackground(Color.PINK);
		
		
		capimg.setBounds(622, 38, 631, 503);
		contentPane.add(capimg);
		
		
		blbl.setFont(new Font("Elephant", Font.BOLD, 22));
		blbl.setBounds(1170, 68, 81, 50);
		contentPane.add(blbl);
	}
}
