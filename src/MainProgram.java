import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

// this class displays the main interface
public class MainProgram extends JFrame implements ActionListener{
	// declares serial version
	private static final long serialVersionUID = 1L;

	// create a series of JComponents
	JLabel JLTitle=new JLabel("Connect of Five v1.1",SwingConstants.CENTER);
	JLabel JLAuthor=new JLabel("----Created by Zexuan Wang in 2009",SwingConstants.CENTER);
	JButton JBOnePlayer=new JButton();
	JButton JBTwoPlayer=new JButton();
	JButton JBRead=new JButton();
	JButton JBRule=new JButton();
	JButton JBQuit=new JButton();


	// constructor to create the main interface
	public MainProgram(String title){
		// Check the user's screen resolution
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		double width = screenSize.getWidth();
		double height = screenSize.getHeight();
		if (width<1152 || height<900){
			int response=JOptionPane.showConfirmDialog(null,
					"It has detected that your resolution is smaller than 1152*900 and some content" +
							"may not be displayed correctly, are you sure you wish to continue?" +
							"YES to keep playing, NO to exit","confirmation",
							JOptionPane.YES_NO_OPTION);
			if(response==JOptionPane.NO_OPTION){
				System.exit(0);
			}
		}

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

		//construct the frame
		JFrame.setDefaultLookAndFeelDecorated(true);	
		this.setSize(1152,900);
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.CYAN);

		//title
		JLTitle.setFont(new Font("Courier New",Font.BOLD,65));
		JLTitle.setForeground(Color.RED);
		JLTitle.setBounds(150,50,800,50);
		this.add(JLTitle);

		JLAuthor.setFont(new Font("Arial",Font.PLAIN,20));
		JLAuthor.setForeground(Color.blue);
		JLAuthor.setBounds(600,120,450,50);
		this.add(JLAuthor);

		final int bWidth=400;
		final int bHeight=85;
		final int bHorDiff=350;
		final int bVerDiff=115;
		// one player button
		JBOnePlayer.setText("One Player");
		JBOnePlayer.setFont(new Font("Arial",Font.PLAIN,36));
		JBOnePlayer.setBounds(bHorDiff,100+bVerDiff,bWidth,bHeight);      
		this.add(JBOnePlayer);
		JBOnePlayer.addActionListener(this);

		//two players button
		JBTwoPlayer.setText("Two Players");
		JBTwoPlayer.setFont(new Font("Arial",Font.PLAIN,36));
		JBTwoPlayer.setBounds(bHorDiff,100+bVerDiff*2,bWidth,bHeight);
		this.add(JBTwoPlayer);
		JBTwoPlayer.addActionListener(this);

		//replay button
		JBRead.setText("Replay");
		JBRead.setFont(new Font("Arial",Font.PLAIN,36));
		JBRead.setBounds(bHorDiff,100+bVerDiff*3,bWidth,bHeight);
		this.add(JBRead);
		JBRead.addActionListener(this);

		//rules button
		JBRule.setText("Rules");
		JBRule.setFont(new Font("Arial",Font.PLAIN,36));
		JBRule.setBounds(bHorDiff,100+bVerDiff*4,bWidth,bHeight);
		this.add(JBRule);
		JBRule.addActionListener(this);

		//quit button
		JBQuit.setText("Quit");
		JBQuit.setFont(new Font("Arial",Font.PLAIN,36));
		JBQuit.setBounds(bHorDiff,100+bVerDiff*5,bWidth,bHeight);
		this.add(JBQuit);
		JBQuit.addActionListener(this);

	}

	// add actions to different buttons
	public void actionPerformed(ActionEvent e){
		this.setVisible(false);
		if(e.getSource()==JBOnePlayer){
			new OnePlayer("Connect of Five");
			dispose();
			return;
		}
		else if (e.getSource()==JBTwoPlayer){
			new TwoPlayers("Connect of Five");
			dispose();
			return;
		}
		else if (e.getSource()==JBRead){
			new Reader("Connect of Five");
			dispose();
			return;
		}
		else if (e.getSource()==JBRule){
			new RulesReader("Connect of Five");
			dispose();
			return;
		}
		else if (e.getSource()==JBQuit){
			JOptionPane.showMessageDialog(null,
					"Thanks for using Connect of Five v1.1!",
					"Thank you", JOptionPane.INFORMATION_MESSAGE);
			System.exit(0);


		}
	}

	// main program
	public static void main(String[] args){
		new MainProgram("Connect of Five");
	}
}
