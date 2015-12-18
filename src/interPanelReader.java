import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

/*
 * This class creates a JPanel underlying the TwoPlayers' chessboard image to listen
 * to mouseEvents and draw black/white stones accordingly
 */
public class interPanelReader extends JPanel {
	private static final long serialVersionUID = 1L;	// serialize interPanelTwoPlayers class

	ArrayList <Integer> movesList = new ArrayList <Integer> ();	// an Integer arrayList to store moves
	private final int radius=30;	// a constant storing radius of each stone
	private int chessBoard[][]=new int[16][16]; 	//a 2D array is used to hold the value of each spot, 1 for black, -1 for white and 0 for none

	// class variables used for the inner timer class
	File dataFile;
	FileReader in;
	BufferedReader readFile;
	static Timer t;

	public interPanelReader() {
		System.out.println("constructor created!");
		//initialize the chessboard
		for (int i=0;i<16;i++){
			for (int j=0;j<16;j++){
				chessBoard[i][j]=0;
			}
		}

	}

	private void drawCircle() {
		repaint();		// programmably evokes the paintComponent() method
	}

	// this method rewrite the paintComponent() for JPanel
	protected void paintComponent(Graphics g) {
		System.out.println("paintComponent called!");
		super.paintComponent(g); 

		int counter=0;

		// iterate through movesList and draw the stones
		for (Iterator<Integer> iter=movesList.iterator(); iter.hasNext();){
			int value=iter.next();
			if (counter%2==0)
				g.setColor(Color.BLACK);
			else
				g.setColor(Color.WHITE);
			System.out.println(value);
			counter++;
			g.fillOval(45*(value/100)+7,45*(value%100)+7, radius,radius);
		}    	
	}  

	// this method stores the movesList array into a file
	protected void readFromFile(){
		System.out.println("Data is read from file");

		// read the data from file and also display stones on the chessboard
		try{
			dataFile = new File("./StoredGamesForConnectOfFive/"+
					Reader.playerOne+"_vs_"+Reader.playerTwo+".dat");
			in=new FileReader(dataFile);
			readFile=new BufferedReader(in);
		}
		catch(Exception k){
			JOptionPane.showMessageDialog(null,"Sorry, file cannot be read, please make sure the current directory is readable!",
					"WARNING",JOptionPane.INFORMATION_MESSAGE);      

			new MainProgram("Connect of Five");
			JFrame curFrame=(JFrame)(SwingUtilities.getRoot(interPanelReader.this));
			curFrame.dispose();
			System.err.println("Exception: "+k.getMessage());
			return;
		}

		t = new Timer(800, null);
		t.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.out.println("WAIT!!!");
				try{
					String line= readFile.readLine();
					if (line==null){
						readFile.close();
						in.close();
						JOptionPane.showMessageDialog(null,"Replay completed!",
								"INFORMATION",JOptionPane.INFORMATION_MESSAGE);      

						new MainProgram("Connect of Five");
						JFrame curFrame=(JFrame)(SwingUtilities.getRoot(interPanelReader.this));
						curFrame.dispose();
						t.stop();
						return;
					}else{
						movesList.add(Integer.parseInt(line));
						drawCircle();
					}								
				}catch(Exception ioe){
					JOptionPane.showMessageDialog(null,"Sorry, file cannot be read, please make sure the current directory is readable!",
							"WARNING",JOptionPane.INFORMATION_MESSAGE);      

					new MainProgram("Connect of Five");
					JFrame curFrame=(JFrame)(SwingUtilities.getRoot(interPanelReader.this));
					curFrame.dispose();
					System.err.println("Exception: "+ioe.getMessage());
					return;
				}
			}
		});
		t.setRepeats(true);
		t.start();
	}
}