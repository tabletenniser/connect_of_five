import javax.swing.*;

import java.awt.*;
import java.awt.event.*;	
import java.io.File;

// this class enables two users to play together
public class TwoPlayers extends JFrame{
	// declares serial version
	private static final long serialVersionUID = 1L;
	static String playerOne, playerTwo;	// two strings store the names of players

	// create different JComponents
	ImageIcon icon=new ImageIcon("CHESSBOARD.jpg");
	JLabel label2=new JLabel(icon,SwingConstants.CENTER);

	JLabel label1=new JLabel("Connect of Five",SwingConstants.CENTER);	// title
	JLabel label3=new JLabel();		// first player
	JLabel label4=new JLabel();		// second player

	// constructor of the TwoPlayers JFrame
	public TwoPlayers(String title){
		// set up windowListener for JFrame upon closure with confirmation
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				int result = JOptionPane.showConfirmDialog(null,
						"Are you sure you want to exit Connect_of_five?",
						"Exit Confirmation", JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION){
					System.exit(0);
				}
			}
		});		

		// create the frame
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setSize(1152, 950);
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.CYAN);

		//title
		label1.setFont(new Font("Courier New",Font.BOLD,65));
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
		
		interPanelTwoPlayers myInterPanel=new interPanelTwoPlayers();
		myInterPanel.setVisible(true);
		myInterPanel.setOpaque(false);
		myInterPanel.setBorder(null);
		myInterPanel.setBounds(45, 145, 718, 718);
		this.add(myInterPanel);

		//chessboard
		label2.setBounds(45,145,718,718);
		label2.setVisible(true);
		this.add(label2);

		do{
			//prompt the user to enter the names of players
			playerOne=JOptionPane.showInputDialog(null,"Please enter the name of the first player (player that will go first):","");
			playerTwo=JOptionPane.showInputDialog(null,"Please enter the name of the second player (player that will go second):","");

			// create the folder if it does not exist
			File folder = new File("./StoredGamesForConnectOfFive");
			if (!folder.exists()) {
				while (!folder.mkdir()) {
					int result =JOptionPane.showConfirmDialog(null,"Sorry, the folder StoredGamesForConnectOfFive" +
							"cannot be created, please make sure the current directory is writable! \n Do you wish to retry?" +
							"YES to make the directory writable and retry, NO to contine a game without saving.",
							"WARNING",JOptionPane.YES_NO_OPTION);      
					if (result==JOptionPane.NO_OPTION)
						break;
				}
			}
			//save the data into a file
			File file	=new File("./StoredGamesForConnectOfFive/"+
					playerOne+"_vs_"+playerTwo+".dat");
			if (file.exists()==true){
				int result =JOptionPane.showConfirmDialog(null,
						"Game with this user already exists, do you still wish to continue? \n (YES to " +
								"erase the previous game, NO to re-enter user's name","WARNING",JOptionPane.YES_NO_OPTION);
				if (result == JOptionPane.YES_OPTION){
					break;
				}
			}else{
				break;
			}
		}while (true);

		// label the player
		label3.setText("BLACK: "+playerOne);
		label3.setForeground(Color.black);
		label3.setFont(new Font("Courier New",Font.BOLD,20));
		label3.setBounds(900,300,500,50);
		this.add(label3);

		//label the white
		label4.setText("WHITE: "+playerTwo);
		label4.setForeground(Color.white);
		label4.setFont(new Font("Courier New",Font.BOLD,20));
		label4.setBounds(900,650,500,50);
		this.add(label4);

		Graphics a=label4.getGraphics();
		this.update(a);
	}

	// main method
	public static void main(String[] args){
		new TwoPlayers("Connect of Five");
	}
}