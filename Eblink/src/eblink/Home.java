package eblink;


import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;
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

import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
//import javax.management.timer.Timer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import javax.swing.JLabel;
import java.awt.Color;

public class Home extends JFrame {
	
	private  DaemonThreadFE ThreadFE=null;
	private  DaemonThreadOE ThreadOE=null;
	private UpThread ut=null;
	private UpfeThread uft=null;
	
	private Timer t1;
	private int Count=20;
	private int Bcount=0;
	boolean isClose=false;
	boolean livestream=false;
	boolean uploadfile=false;
	boolean low=false;
	boolean high=false;
	
	int lbln[]= new int[100];
	
	
	VideoCapture websource=null;
	VideoCapture uwebsource=null;
	
	
	MatOfByte mem=new MatOfByte();
	MatOfByte umem=new MatOfByte();
	
	CascadeClassifier FaceDetector=new CascadeClassifier(Home.class.getResource("haarcascade_frontalface_alt.xml").getPath().substring(1).replaceAll("%20"," "));
	CascadeClassifier EyeDetector=new CascadeClassifier(Home.class.getResource("haarcascade_eye_tree_eyeglasses.xml").getPath().substring(1).replaceAll("%20"," "));
	CascadeClassifier LeftEyeDetector=new CascadeClassifier(Home.class.getResource("haarcascade_lefteye_2splits.xml").getPath().substring(1).replaceAll("%20"," "));
	CascadeClassifier RightEyeDetector=new CascadeClassifier(Home.class.getResource("haarcascade_righteye_2splits.xml").getPath().substring(1).replaceAll("%20"," "));
	
	MatOfRect FaceDetections=new MatOfRect();
	MatOfRect EyeDetections=new MatOfRect();
	MatOfRect LeftEyeDetections=new MatOfRect();
	MatOfRect RightEyeDetections=new MatOfRect();
	
	MatOfRect UFaceDetections=new MatOfRect();
	MatOfRect UEyeDetections=new MatOfRect();
	MatOfRect ULeftEyeDetections=new MatOfRect();
	MatOfRect URightEyeDetections=new MatOfRect();
	

	JPanel facepanel = new JPanel();
	JPanel faepanel = new JPanel();
	JButton btnPause = new JButton("Pause");
	JButton btnStart = new JButton("Start Tracking");
	JButton ufbtn = new JButton("Upload File/Resume");
	JButton upausebtn = new JButton("Pause");
	JButton btnRestartu = new JButton("Restart");
	JButton btnRestartl = new JButton("Restart");
	
	JLabel cnlbl = new JLabel("");
	JLabel lbl1 = new JLabel("NULL");
	JLabel lbl2 = new JLabel("NULL");
	JLabel lbl3 = new JLabel("NULL");
	JLabel lbl4 = new JLabel("NULL");
	JLabel lbl5 = new JLabel("NULL");
	JLabel avglbl = new JLabel("NULL");
	int Downc=0;
	
	

	private JPanel contentPane;
	private final JPanel panel = new JPanel();
	private final JLabel lblBlinkCount = new JLabel("BLINK COUNT:");
	private JLabel blbl = new JLabel("");
	
	/****************************************ONLY FACE PANEL********************************/
	
	class DaemonThreadFE implements Runnable
	{
		protected volatile boolean runnable=false;
		
		@Override
		public void run()
		{
			synchronized(this)
			{
				while(runnable)
				{
					if(websource.grab()) 
					{
						try
						{
							Mat frame1=new Mat();
						websource.retrieve(frame1);
						Graphics g1=facepanel.getGraphics();
						
						FaceDetector.detectMultiScale(frame1, FaceDetections,1.1,20,0,new Size(),new Size());
						
						for(Rect rect:FaceDetections.toArray())
						{
														
							Imgproc.rectangle(frame1,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,255,0),3);
							/*Mat faces=frame1.submat(rect);
							EyeDetector.detectMultiScale(faces, EyeDetections);
							for(Rect recte:EyeDetections.toArray())
							{
							Imgproc.rectangle(faces,new Point(recte.x,recte.y),new Point(recte.x+recte.width,recte.y+recte.height),new Scalar(255,0,255),3);
							}*/
						}
							Imgcodecs.imencode(".bmp", frame1, mem);
							Image im=ImageIO.read(new ByteArrayInputStream(mem.toArray()));
							BufferedImage buff=(BufferedImage) im;
							if(g1.drawImage(buff,0,0,getWidth()-800,getHeight() -150, 0, 0,buff.getWidth(),buff.getHeight(),null))
							{
																
								if(runnable==false)
								{
									System.out.println("Paused Face And Eye Detection.....");
									this.wait();
								}
							}						
						
						}
						catch(Exception ex)
						{
							System.out.println("ERROR LIVE FACE....");
						}
					}
				}
			}
		}
		
	}
	
	/********************************FACE AND EYE panel Thread class***********************************/
	
	class DaemonThreadOE implements Runnable
	{
		protected volatile boolean runnable=false;
		
		@Override
		public void run()
		{
			synchronized(this)
			{
				while(runnable)
				{
					Mat frame2=new Mat();
					if(websource.grab()) 
					{
						try
						{
						websource.retrieve(frame2);
						Graphics g2=faepanel.getGraphics();
						FaceDetector.detectMultiScale(frame2, FaceDetections,1.1,20,0,new Size(),new Size());
						
					for(Rect rect:FaceDetections.toArray())
						{
														
							Imgproc.rectangle(frame2,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,255,0),5);
							Mat faces=frame2.submat(rect);
							EyeDetector.detectMultiScale(faces, EyeDetections,1.1, 2, 0,new Size(), new Size());
							LeftEyeDetector.detectMultiScale(faces, LeftEyeDetections,1.30, 40, 0,new Size(), new Size());
							RightEyeDetector.detectMultiScale(faces, RightEyeDetections,1.30, 40, 0,new Size(), new Size());
							
							if(EyeDetections.toArray().length>0&&LeftEyeDetections.toArray().length==0&&RightEyeDetections.toArray().length==0&&isClose==false)
							{
								
								Bcount++;
								//System.out.println("EYE="+EyeDetections.toArray().length  +LeftEyeDetections.toArray().length  +RightEyeDetections.toArray().length);
								isClose=true;
																
								//System.out.println("Eye Blink "+Bcount);
								blbl.setText(String.valueOf(Bcount));
							}
							else if(EyeDetections.toArray().length>0&&LeftEyeDetections.toArray().length>0&&RightEyeDetections.toArray().length>0&&isClose==true)
							{
								isClose=false;
								
							}
							
							
							for(Rect recte:EyeDetections.toArray())
							{				
								
								
								Imgproc.rectangle(faces,new Point(recte.x,recte.y),new Point(recte.x+recte.width,recte.y+recte.height),new Scalar(255,0,255),2);
							
							}		
							}
							
							
							Imgcodecs.imencode(".bmp", frame2, mem);
							Image im=ImageIO.read(new ByteArrayInputStream(mem.toArray()));
							BufferedImage buff=(BufferedImage) im;
							if(g2.drawImage(buff,0,0,getWidth()-800,getHeight() -150, 0, 0,buff.getWidth(),buff.getHeight(),null))
							{											
								if(runnable==false)
								{
									System.out.println("Paused Eye Detection.....");															
									this.wait();
								}
							
							}
						
						
						}	
				
						catch(Exception ex)
						{
							System.out.println("ERROR LIVE FACE AND EYE....");
						}
					}
				}
			}
		}
		
	}
	
	
	
	
	/*********************************TIMER****************************************/
	
	public void Timert()
	{
		t1=new Timer(1000,new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				{
				
					Count--;
					cnlbl.setText(String.valueOf(Count));
					
					if(Count==0)
					{
						System.out.println("array"+Downc);
						
							lblset(Downc,Bcount);
							alertwin(Downc,Bcount);
							Downc++;						
							Bcount=0;
							blbl.setText("0");
							Count=20;
						
					}
					
				}
				
				
			}
			
		});
		
		t1.start();
		
	}
	/*****************************************ALERT AND SHUTDOWN FUNCTION******************/
	
	public void alertwin(int dc,int bcnt)
	{
		
		if(bcnt<2)
		{
			low=true;
			playsound();
			alertw alert = new alertw();
			alert.setVisible(true);
			alert.lblhighlow.setText("Your Blink Cout Is Too Low.......");
			alert.lblwar.setText("Please Increase your blinks...!!!");
			
			
				}
		else if(bcnt>4)
		{
			high=true;
			playsound();
			alertw alert = new alertw();
			alert.setVisible(true);
			alert.lblhighlow.setText("Your Blink Cout Is Too High.........");
			alert.lblwar.setText("Please Concentrate...!!!");
			
		}
		
	}
	
	/*****************************************LABEL UPDATE FUNCTION*************************/
	
	public void lblset(int d,int b)
	{
		int avg;
		lbln[d]=b;
		
		  lbl1.setText(String.valueOf(lbln[d]));
		  avglbl.setText(String.valueOf(lbln[d]));
	     if(d>=1)
	     {
		lbl2.setText(String.valueOf(lbln[d-1]));
		avg=(lbln[d]+lbln[d-1])/2;
		avglbl.setText(String.valueOf(avg));	    
	     } 
		if(d>=2)
		{
		lbl3.setText(String.valueOf(lbln[d-2]));
		avg=(lbln[d]+lbln[d-1]+lbln[d-2])/3;
		avglbl.setText(String.valueOf(avg));	
		}
	     if(d>=3)
	     {
		lbl4.setText(String.valueOf(lbln[d-3]));
		avg=(lbln[d]+lbln[d-1]+lbln[d-2]+lbln[d-3])/4;
		avglbl.setText(String.valueOf(avg));
	     }
	     if(d>=4)
	     {
		lbl5.setText(String.valueOf(lbln[d-4]));
	   int avgsum=(lbln[d]+lbln[d-1]+lbln[d-2]+lbln[d-3]+lbln[d-4]);
	   avg=(lbln[d]+lbln[d-1]+lbln[d-2]+lbln[d-3]+lbln[d-4])/5;
		avglbl.setText(String.valueOf(avg));
		if(avgsum==0)
		{
			Shutdowns();
		}
	     }
	     
	}
	/*******************************SHUTDOWN FUNCTION******************/
	
	public void Shutdowns()
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
	     
	    //System.exit(0);
	}
	
	/*****************************************FILE UPLOAD FACE DETECTION*************************************/
	
	class UpThread implements Runnable
    {
    protected volatile boolean runnable = false;

    @Override
    public  void run()
    {
        synchronized(this)
        {
            while(runnable)
            {
            	Mat uframe=new Mat();
                if(uwebsource.grab())
                {
		    	try
                        {
                            uwebsource.retrieve(uframe);
                            Graphics g=facepanel.getGraphics();
                            
                            FaceDetector.detectMultiScale(uframe, UFaceDetections,1.1,20,0,new Size(),new Size());
    						
    						for(Rect rect:UFaceDetections.toArray())
    						{
    														
    							Imgproc.rectangle(uframe,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,255,0),5);
    						} 
                            
                            Imgcodecs.imencode(".bmp", uframe, umem);
			                Image im = ImageIO.read(new ByteArrayInputStream(umem.toArray()));
                            BufferedImage buff = (BufferedImage) im;
			    

			    if (g.drawImage(buff, 0, 0, getWidth()-800, getHeight()-150, 0, 0, buff.getWidth(), buff.getHeight(),null))
			    {
			    if(runnable == false)
                            {
			    	System.out.println("Going to wait()");
			    	this.wait();
			    }
			 }
                        }
			 catch(Exception ex)
                         {
			    System.out.println("Error upload face");
                         }
                }
            }
        }
     }
   }
	
	/***********************************FACE AND EYE UPLOAD *******************************/
	
	class UpfeThread implements Runnable
    {
    protected volatile boolean runnable = false;

    @Override
    public  void run()
    {
        synchronized(this)
        {
            while(runnable)
            {
            	Mat uframe=new Mat();
                if(uwebsource.grab())
                {
		    	try
                        {
                            uwebsource.retrieve(uframe);
                            Graphics g=faepanel.getGraphics();
                            
                            FaceDetector.detectMultiScale(uframe, UFaceDetections,1.1,20,0,new Size(),new Size());
    						
    						for(Rect rect:UFaceDetections.toArray())
    						{
    														
    							Imgproc.rectangle(uframe,new Point(rect.x,rect.y),new Point(rect.x+rect.width,rect.y+rect.height),new Scalar(0,255,0),5);
    						
    							Mat ufaces=uframe.submat(rect);
    							EyeDetector.detectMultiScale(ufaces, UEyeDetections,1.1, 2, 0,new Size(), new Size());
    							LeftEyeDetector.detectMultiScale(ufaces, ULeftEyeDetections,1.30, 40, 0,new Size(), new Size());
    							RightEyeDetector.detectMultiScale(ufaces, URightEyeDetections,1.30, 40, 0,new Size(), new Size());
    							
    							if(UEyeDetections.toArray().length>0&&ULeftEyeDetections.toArray().length==0&&URightEyeDetections.toArray().length==0&&isClose==false)
    							{
    								
    								Bcount++;
    								//System.out.println("EYE="+EyeDetections.toArray().length  +LeftEyeDetections.toArray().length  +RightEyeDetections.toArray().length);
    								isClose=true;
    																
    								//System.out.println("Eye Blink "+Bcount);
    								blbl.setText(String.valueOf(Bcount));
    							}
    							else if(UEyeDetections.toArray().length>0&&ULeftEyeDetections.toArray().length>0&&URightEyeDetections.toArray().length>0&&isClose==true)
    							{
    								isClose=false;
    								
    							}
    							
    							
    							for(Rect recte:UEyeDetections.toArray())
    							{				
    								
    								
    								Imgproc.rectangle(ufaces,new Point(recte.x,recte.y),new Point(recte.x+recte.width,recte.y+recte.height),new Scalar(255,0,255),2);
    							
    							}		
    						} 
                            
                            Imgcodecs.imencode(".bmp", uframe, umem);
			                Image im = ImageIO.read(new ByteArrayInputStream(umem.toArray()));
                            BufferedImage buff = (BufferedImage) im;
			    

			    if (g.drawImage(buff, 0, 0, buff.getWidth(), buff.getHeight(), 0, 0, buff.getWidth(), buff.getHeight(),null))
			    {
			    if(runnable == false)
                            {
			    	System.out.println("Going to wait()");
			    	this.wait();
			    }
			 }
                        }
			 catch(Exception ex)
                         {
			    System.out.println("Error upload face and eye");
                         }
                }
            }
        }
     }
   }
	
	public static void playsound()
	{
		
		try
		{
			File alertsound=new File("sirentone.wav");
			Clip clip=AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(alertsound));
			clip.start();
			Thread.sleep(clip.getMicrosecondLength()/1000);
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			System.out.println("Sound Error..");
			
		}
		
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
					Home frame = new Home();
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
	public Home() {
		setTitle("Home");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//setBounds(100, 100, screenSize.width, screenSize.height);
		setBounds(0, 0, 1366, 768);
		contentPane = new JPanel();
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		facepanel.setBounds(26, 189, 505, 503);
		contentPane.add(facepanel);
		
		JPanel selectpanel = new JPanel();
		selectpanel.setBackground(Color.GRAY);
		selectpanel.setBounds(209, 11, 239, 138);
		contentPane.add(selectpanel);
		selectpanel.setLayout(null);
		btnStart.setBounds(32, 11, 168, 33);
		selectpanel.add(btnStart);
		
	/**********************************START TRACKIING BUTTON***************************/
		
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
								
				livestream=true;
				websource=new VideoCapture();
				websource.open(2);
				ThreadFE=new DaemonThreadFE();
				Thread tfe=new Thread(ThreadFE);
				tfe.setDaemon(true);
				ThreadFE.runnable=true;
				tfe.start();
				
				ThreadOE=new DaemonThreadOE();
				Thread toe=new Thread(ThreadOE);
				toe.setDaemon(true);
				ThreadOE.runnable=true;
				toe.start();
				
				Timert();
				
				
				btnStart.setEnabled(false);
				btnPause.setEnabled(true);
				btnRestartl.setEnabled(true);
				
				
			}
		});
		btnStart.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		String op[]= {"Live Stream","Upload File"};
		btnPause.setBounds(126, 72, 103, 33);
		selectpanel.add(btnPause);
		
		/******************************PAUSE LIVE***********************/
		
		btnPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ThreadFE.runnable=false;
				ThreadOE.runnable=false;
								
				btnPause.setEnabled(false);
				btnStart.setEnabled(true);
				btnRestartl.setEnabled(false);
				
				websource.release();
				
				t1.stop();
			}
		});
		
		
		btnPause.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		
		btnRestartl.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t1.stop();
				Count=20;
				Bcount=0;
				Downc=0;
				Timert();
				blbl.setText(String.valueOf(Bcount));
                lbl1.setText("NULL");
                lbl2.setText("NULL");
                lbl3.setText("NULL");
                lbl4.setText("NULL");
                lbl5.setText("NULL");
			}
		});
		btnRestartl.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnRestartl.setBounds(10, 72, 103, 33);
		selectpanel.add(btnRestartl);
		
		
		faepanel.setBounds(556, 189, 505, 503);
		contentPane.add(faepanel);
		
		JButton exitbtn = new JButton("");
		
		Image img=new ImageIcon(this.getClass().getResource("/exits.png")).getImage();
		exitbtn.setIcon(new ImageIcon(img));
		exitbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			
				if(livestream==true)
				{
				ThreadFE.runnable=false;
				ThreadOE.runnable=false;				
				websource.release();
				t1.stop();
				System.exit(0);
				}
				else if(uploadfile==true)
				{
			      ut.runnable=false;
			      uft.runnable=false;
				  uwebsource.release();
				  t1.stop();
				  System.exit(0);
				}
				else
					System.exit(0); 
								
			}
		});
		exitbtn.setBounds(1270, 11, 70, 72);
		contentPane.add(exitbtn);
		JComboBox comboBox = new JComboBox(op);
		comboBox.setBounds(26, 34, 168, 33);
		contentPane.add(comboBox);
		
		upausebtn.setEnabled(false);
		btnRestartu.setEnabled(false);
		comboBox.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent arg0) {
				if(comboBox.getSelectedItem().equals("Live Stream"))
					{

					if(uploadfile==true)
					{
				      ut.runnable=false;
				      uft.runnable=false;
					  uwebsource.release();
					  t1.stop();
					  Count=20;
						Bcount=0;
						Downc=0;
						//Timert();
						blbl.setText(String.valueOf(Bcount));
		                lbl1.setText("NULL");
		                lbl2.setText("NULL");
		                lbl3.setText("NULL");
		                lbl4.setText("NULL");
		                lbl5.setText("NULL");
					} 
					
					ufbtn.setEnabled(false);
					upausebtn.setEnabled(false);
					btnRestartu.setEnabled(false);
					
					btnPause.setEnabled(true);
					btnStart.setEnabled(true);
					btnRestartl.setEnabled(true);
					
				}
				else
				{
					if(livestream==true)
					{
					ThreadFE.runnable=false;
					ThreadOE.runnable=false;				
					websource.release();
					t1.stop();
					Count=20;
					Bcount=0;
					Downc=0;
					//Timert();
					blbl.setText(String.valueOf(Bcount));
	                lbl1.setText("NULL");
	                lbl2.setText("NULL");
	                lbl3.setText("NULL");
	                lbl4.setText("NULL");
	                lbl5.setText("NULL");
					} 

					
					ufbtn.setEnabled(true);
					upausebtn.setEnabled(true);
					btnRestartu.setEnabled(true);
					
					btnPause.setEnabled(false);
					btnStart.setEnabled(false);
					btnRestartl.setEnabled(false);
				}
			}
		});
		comboBox.setFont(new Font("Times New Roman", Font.BOLD, 24));
		
		JPanel uppanel = new JPanel();
		uppanel.setBackground(Color.GRAY);
		uppanel.setBounds(474, 11, 325, 138);
		contentPane.add(uppanel);
		uppanel.setLayout(null);
		ufbtn.setBounds(48, 11, 247, 38);
		uppanel.add(ufbtn);
		ufbtn.setEnabled(false);
		
		/********************************************UPLOAD FILE*****************************************/
		
		ufbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				uploadfile=true;
		/*	    JFileChooser chooser=new JFileChooser();
				chooser.showOpenDialog(null);
				File f=chooser.getSelectedFile();
				String filename=f.getAbsolutePath();   */
				
				
				//isClose=false;
			/*	websource=new VideoCapture(filename);
				ThreadFE=new DaemonThreadFE();
				Thread tfe=new Thread(ThreadFE);
				tfe.setDaemon(true);
				ThreadFE.runnable=true;
				tfe.start();
				
				ThreadOE=new DaemonThreadOE();
				Thread toe=new Thread(ThreadOE);
				toe.setDaemon(true);
				ThreadOE.runnable=true;
				toe.start();
				
				Timert();  */
				
				uwebsource=new VideoCapture(1);
				
				ut=new UpThread();
				uft=new UpfeThread();
				
				Thread tf=new Thread(ut);
				Thread tfe=new Thread(uft);
				
				tf.setDaemon(true);
				tfe.setDaemon(true);
				
				ut.runnable=true;
				uft.runnable=true;
				
				tf.start();
				tfe.start();
				
				Timert();
				
				ufbtn.setEnabled(false); 
				upausebtn.setEnabled(true);
				btnRestartu.setEnabled(true);
				

				
				
			}
		});
		ufbtn.setFont(new Font("Times New Roman", Font.BOLD, 20));
		
		
		upausebtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
			ut.runnable=false;
			uft.runnable=false;
			
			uwebsource.release();
			t1.stop();
			
			
			ufbtn.setEnabled(true); 
			upausebtn.setEnabled(false);
			btnRestartu.setEnabled(false);
			
	
				
			}
		});
		upausebtn.setFont(new Font("Times New Roman", Font.BOLD, 20));
		upausebtn.setBounds(192, 75, 103, 33);
		uppanel.add(upausebtn);
		
		
		btnRestartu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				t1.stop();
				Count=20;
				Bcount=0;
				Downc=0;
				Timert();
				blbl.setText(String.valueOf(Bcount));
                lbl1.setText("NULL");
                lbl2.setText("NULL");
                lbl3.setText("NULL");
                lbl4.setText("NULL");
                lbl5.setText("NULL");
                }
		});
		btnRestartu.setFont(new Font("Times New Roman", Font.BOLD, 20));
		btnRestartu.setBounds(49, 75, 103, 33);
		uppanel.add(btnRestartu);
		panel.setBackground(Color.GRAY);
		panel.setBounds(812, 11, 448, 138);
		
		contentPane.add(panel);
		panel.setLayout(null);
		lblBlinkCount.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblBlinkCount.setBounds(10, 11, 172, 27);
		
		panel.add(lblBlinkCount);
		
		JLabel lblCounter = new JLabel("COUNTER:");
		lblCounter.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblCounter.setBounds(59, 67, 123, 27);
		panel.add(lblCounter);
		
		
		cnlbl.setFont(new Font("Times New Roman", Font.BOLD, 22));
		cnlbl.setBounds(202, 67, 60, 27);
		panel.add(cnlbl);
		blbl.setFont(new Font("Times New Roman", Font.BOLD, 22));
		blbl.setBounds(202, 11, 60, 27);
		
		panel.add(blbl);
		
		JLabel lblLastFiveMinute = new JLabel("Last Five Minute Records");
		lblLastFiveMinute.setForeground(Color.WHITE);
		lblLastFiveMinute.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblLastFiveMinute.setBounds(1078, 189, 245, 27);
		contentPane.add(lblLastFiveMinute);
		
		JLabel lblLastMinuteRecord = new JLabel("Last Minute:");
		lblLastMinuteRecord.setForeground(Color.WHITE);
		lblLastMinuteRecord.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblLastMinuteRecord.setBounds(1078, 238, 128, 27);
		contentPane.add(lblLastMinuteRecord);
		
		JLabel lblOlder = new JLabel("Older 1:");
		lblOlder.setForeground(Color.WHITE);
		lblOlder.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblOlder.setBounds(1122, 287, 84, 27);
		contentPane.add(lblOlder);
		
		JLabel lblOlder_1 = new JLabel("Older 2:");
		lblOlder_1.setForeground(Color.WHITE);
		lblOlder_1.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblOlder_1.setBounds(1122, 336, 84, 27);
		contentPane.add(lblOlder_1);
		
		JLabel lblOlder_2 = new JLabel("Older 3:");
		lblOlder_2.setForeground(Color.WHITE);
		lblOlder_2.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblOlder_2.setBounds(1122, 385, 84, 27);
		contentPane.add(lblOlder_2);
		
		JLabel lblOlder_3 = new JLabel("Older 4:");
		lblOlder_3.setForeground(Color.WHITE);
		lblOlder_3.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lblOlder_3.setBounds(1122, 433, 84, 27);
		contentPane.add(lblOlder_3);
		
		
		lbl1.setForeground(Color.WHITE);
		lbl1.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lbl1.setBounds(1227, 238, 84, 27);
		contentPane.add(lbl1);
		
		
		lbl2.setForeground(Color.WHITE);
		lbl2.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lbl2.setBounds(1227, 287, 84, 27);
		contentPane.add(lbl2);
		
		
		lbl3.setForeground(Color.WHITE);
		lbl3.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lbl3.setBounds(1227, 336, 84, 27);
		contentPane.add(lbl3);
		
		
		lbl4.setForeground(Color.WHITE);
		lbl4.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lbl4.setBounds(1227, 385, 84, 27);
		contentPane.add(lbl4);
		
		
		lbl5.setForeground(Color.WHITE);
		lbl5.setFont(new Font("Times New Roman", Font.BOLD, 22));
		lbl5.setBounds(1227, 433, 84, 27);
		contentPane.add(lbl5);
		
		JLabel lblAvgBlinkCount = new JLabel("Avg Blink Count");
		lblAvgBlinkCount.setForeground(new Color(0, 255, 255));
		lblAvgBlinkCount.setFont(new Font("Times New Roman", Font.BOLD, 28));
		lblAvgBlinkCount.setBounds(1093, 509, 233, 45);
		contentPane.add(lblAvgBlinkCount);
		
		
		avglbl.setForeground(Color.WHITE);
		avglbl.setFont(new Font("Times New Roman", Font.BOLD, 28));
		avglbl.setBounds(1167, 578, 93, 33);
		contentPane.add(avglbl);
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
				
			}
		});
		
		
	}
}
