import java.io.*;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

// this class displays the rule of this game
public class RulesReader extends JFrame implements ActionListener {
	// declares serial version
	private static final long serialVersionUID = 1L;
	
	//JComponents
	JLabel label1=new JLabel("Connect of Five",SwingConstants.CENTER);
	JButton button=new JButton();

	// constructor
	public RulesReader(String title) {

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

		//create the frame
		JFrame.setDefaultLookAndFeelDecorated(true);
		this.setTitle(title);
		this.setSize(1152,950);
		this.setVisible(true);
		this.setResizable(false);
		this.setLayout(null);
		this.getContentPane().setBackground(Color.CYAN);

		
		//title
				label1.setFont(new Font("Courier New",Font.BOLD,65));
				label1.setForeground(Color.RED);
				label1.setBounds(150,50,800,50);
				this.add(label1);


		// read the rule from a file
		File dataFile = new File("Rules.txt");
		FileReader in;
		BufferedReader readFile;
		String line;
		final int gap=50;
		int counter=0;

		try {
			in =new FileReader(dataFile);
			readFile = new BufferedReader(in);
			while(( line= readFile.readLine())!=null) {
				JLabel temp=new JLabel(line);
				temp.setBounds(200, 150+counter*gap, 800, 70);
				temp.setVisible(true);
				temp.setFont(new Font("Arial",Font.PLAIN,30));
				temp.setForeground(Color.black);
				this.add(temp);
				counter++;
			}
			readFile.close();
			in.close();
		}
		catch (FileNotFoundException e){
			JOptionPane.showMessageDialog(null,"Sorry, the file rules.txt file cannot be found, please make sure it is present in the current directory!",
					"WARNING",JOptionPane.INFORMATION_MESSAGE);      

			new MainProgram("Connect of Five");
			this.dispose();
			System.err.println("FileNotFoundException: "+e.getMessage());
		}
		catch(IOException e ) {
			JOptionPane.showMessageDialog(null,"Sorry, the file rules.txt file cannot be read, please make sure it is readable!",
					"WARNING",JOptionPane.INFORMATION_MESSAGE);      

			new MainProgram("Connect of Five");
			this.dispose();
			System.err.println("IOException: "+e.getMessage());
		}

		// back button
		button.setText("BACK");
		button.setBounds(300,800,540,50);
		this.add(button);
		button.addActionListener(this);

	}

	//action to the button
	public void actionPerformed(ActionEvent e){
		if(e.getSource()==button){
			this.dispose();
			new MainProgram("Connect of Five");
		}
	}

	//main program
	public static void main(String[] args){
		new RulesReader("Connect of Five");
	}
}