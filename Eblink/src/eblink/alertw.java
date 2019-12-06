package eblink;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Window.Type;
import org.eclipse.wb.swing.FocusTraversalOnArray;
import java.awt.Component;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Image;

import javax.swing.JLabel;
import javax.swing.JCheckBox;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.SystemColor;
import java.awt.Color;

public class alertw extends JFrame {

	private JPanel contentPane;
	JLabel lblhighlow = new JLabel("");
	JLabel lblwar = new JLabel("");
	

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
								
				try {
					alertw frame = new alertw();
					frame.setVisible(true);
					//Home.playsound();
				
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public alertw() {
		
		setTitle("ALERT");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(900, 500, 450, 205);
		contentPane = new JPanel();
		contentPane.setBackground(SystemColor.control);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
		     dispose();
				
			}
		});
		btnNewButton.setFont(new Font("Times New Roman", Font.BOLD, 22));
		btnNewButton.setBounds(223, 110, 95, 30);
		contentPane.add(btnNewButton);
		
		JLabel lblalert = new JLabel("");
		Image img=new ImageIcon(this.getClass().getResource("/alerticon.png")).getImage();
		lblalert.setIcon(new ImageIcon(img));
		lblalert.setBounds(0, 30, 128, 128);
		contentPane.add(lblalert);
		lblhighlow.setForeground(Color.RED);
		
		
		lblhighlow.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblhighlow.setBounds(122, 30, 251, 30);
		contentPane.add(lblhighlow);
		
		
		lblwar.setFont(new Font("Times New Roman", Font.PLAIN, 18));
		lblwar.setBounds(174, 69, 235, 30);
		contentPane.add(lblwar);
		setFocusTraversalPolicy(new FocusTraversalOnArray(new Component[]{contentPane}));
		
	}
}
