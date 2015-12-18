import java.io.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

// this class is the replay function
public class Reader extends JFrame implements ActionListener {
	// declares serial version
	private static final long serialVersionUID = 1L;

	static String playerOne, playerTwo;	// two static Strings holding players' names

	//create JComponents
	JLabel label1=new JLabel("Connect of Five",SwingConstants.CENTER);
	JButton button=new JButton();
	ImageIcon icon=new ImageIcon("CHESSBOARD.jpg");
	JLabel label2=new JLabel(icon,SwingConstants.CENTER);
	JLabel label3=new JLabel();
	JLabel label4=new JLabel();

	// constructor
	public Reader(String title) {
		// set up windowListener for JFrame upon closure
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				int result = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to exit Connect_of_five?",
						"Exit Confirmation",
						JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});		

		// display the title
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setTitle(title);
		this.setSize(1152,950);
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.CYAN);

		//title
		label1.setFont(new Font("Courier New",Font.BOLD,45));
		label1.setForeground(Color.RED);
		label1.setBounds(150,50,800,50);
		this.add(label1);

		// check if CHESSBOARD.jpg exist in the current directory, if not, go back to mainProgram
		File chessBoard	=new File("CHESSBOARD.jpg");
		if (chessBoard.exists()==false){
			JOptionPane.showMessageDialog(null,"Sorry, the file CHESSBOARD.jpg file cannot be found, please make sure it is present in the current directory! Program terminated!",
					"WARNING",JOptionPane.INFORMATION_MESSAGE);      
			new MainProgram("Connect of Five");
			this.dispose();
			return;
		}

		interPanelReader myInterPanel=new interPanelReader();
		myInterPanel.setVisible(true);
		myInterPanel.setOpaque(false);
		myInterPanel.setBorder(null);
		myInterPanel.setBounds(45, 145, 718, 718);
		this.add(myInterPanel);

		//chessboard
		label2.setBounds(45,145,718,718);
		this.add(label2);

		do{
			// display a message saying what this program does and also prompt the user for the players' names      	
			playerOne=JOptionPane.showInputDialog(null,"Please enter name of the first (black) player of the game you wish to replay:","");
			playerTwo=JOptionPane.showInputDialog(null,"Please enter name of the second (white) player (Enter 'computer' if it's player vs computer):","computer");

			// search for the data file
			File dataFile = new File("./StoredGamesForConnectOfFive/"+playerOne+"_vs_"+playerTwo+".dat");

			// if there's no such a file, exit; otherwise, keep looping
			if (dataFile.exists()==true)	
				break;
			int result = JOptionPane.showConfirmDialog(null,
					"Sorry, the users do not exist, do you want to reenter? YES to reenter, NO to go back to main menu.",
					"User Not Exist", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.NO_OPTION){				
				this.dispose();
				new MainProgram("Connect of Five");
				return;
			}
		}while(true);

		// label the black player
		label3.setText("BLACK: "+playerOne);
		label3.setForeground(Color.black);
		label3.setFont(new Font("Courier New",Font.BOLD,20));
		label3.setBounds(900,300,500,50);
		this.add(label3);
		Graphics g3=label3.getGraphics();
		label3.update(g3);

		// label the white player
		label4.setText("WHITE: "+playerTwo);
		label4.setForeground(Color.white);
		label4.setFont(new Font("Courier New",Font.BOLD,20));
		label4.setBounds(900,650,500,50);
		this.add(label4);
		Graphics g4=label4.getGraphics();
		label4.update(g4);

		myInterPanel.readFromFile();

		//display the back button
		button.setText("BACK");
		button.setBounds(1000,800,100,50);
		this.add(button);
		button.addActionListener(this);

	}

	public void actionPerformed(ActionEvent e){
		if(e.getSource()==button){
			interPanelReader.t.stop();
			int result = JOptionPane.showConfirmDialog(null,
					"Are you sure you to stop replaying?",
					"Confirmation", JOptionPane.YES_NO_OPTION);
			if (result == JOptionPane.YES_OPTION){
				this.dispose();
				new MainProgram("Connect of Five");
			}else{
				interPanelReader.t.start();
			}
		}
	}

	//main program
	public static void main(String[] args){
		new Reader("Connect of Five"); 
	}
}