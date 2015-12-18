import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;

/*
 * This class creates a JPanel underlying the TwoPlayers' chessboard image to listen
 * to mouseEvents and draw black/white stones accordingly
 */
public class interPanelOnePlayer extends JPanel {
	private static final long serialVersionUID = 1L;	// serialize interPanelTwoPlayers class

	ArrayList <Integer> movesList = new ArrayList <Integer> ();	// an Integer arrayList to store moves
	private final int radius=30;	// a constant storing radius of each stone
	protected static int chessBoard[][]=new int[16][16]; 	//a 2D array is used to hold the value of each spot, 1 for black, -1 for white and 0 for none
	//private int clickCount;		// a counter used in the inner class of mouseListener

	public interPanelOnePlayer() {
		System.out.println("constructor created!");

		//clickCount=0;

		//initialize the chessboard
		for (int i=0;i<16;i++){
			for (int j=0;j<16;j++){
				chessBoard[i][j]=0;
			}
		}
		addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (SwingUtilities.isLeftMouseButton(e)){
					// convert the clicked position into two integers between 0 and 15
					int x=(int)((e.getX()-2)*15/673);		//(673/15)px per grid
					int y=(int)((e.getY()-2)*15/673);
					if (x<0){		// adjustment made for the boundary
						x=0;				
					}else if (y<0){
						y=0;
					}else if (x>15){
						x=15;
					}else if (y>15){
						y=15;
					}

					// if the user clicks on an empty space, check if it's even or odd; otherwise, do nothing
					if (chessBoard[x][y]==0){
						// add the new move into movesList and update the GUI
						Integer temp=x*100+y;
						movesList.add(temp);
						drawCircle();

						chessBoard[x][y]=1;	//assign corresponding spot in the 2D array to 1

						//if black wins, return to mainProgram
						if (OnePlayer.IsBlackWin(chessBoard)){
							JOptionPane.showMessageDialog(null,"BLACK WINS!!!","Game Finished",JOptionPane.INFORMATION_MESSAGE);
							int response=JOptionPane.showConfirmDialog(null,"Do you want to save this game?","confirmation",JOptionPane.YES_NO_OPTION);

							// save current game by calling createAndWriteToData() method
							if(response==JOptionPane.YES_OPTION){
								createAndWriteToData();
							}
							new MainProgram("Connect of Five");
							JFrame curFrame=(JFrame)(SwingUtilities.getRoot(interPanelOnePlayer.this));
							curFrame.dispose();
							return;
						}
						
						//int bestMove=OnePlayer.Calculate();
                  int bestMove=OnePlayer.calculateNEW();

						chessBoard[bestMove/100][bestMove%100]=-1;	//computer move
						temp=bestMove;
						movesList.add(temp);
						drawCircle();
						
						// if white wins, go to MainProgram
						if (OnePlayer.IsWhiteWin(chessBoard)){
							JOptionPane.showMessageDialog(null,"COMPUTER WINS!!!","Game Finished",JOptionPane.INFORMATION_MESSAGE);
							int response=JOptionPane.showConfirmDialog(null,"Do you want to save this game?","confirmation",JOptionPane.YES_NO_OPTION);

							// save current game by calling createAndWriteToData() method
							if(response==JOptionPane.YES_OPTION){
								createAndWriteToData();
							}
							new MainProgram("Connect of Five");
							JFrame curFrame=(JFrame)(SwingUtilities.getRoot(interPanelOnePlayer.this));
							curFrame.dispose();
							return;
						}

					}
				}
			}
		});
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
			//System.out.println(value);
			counter++;
			g.fillOval(45*(value/100)+7,45*(value%100)+7, radius,radius);
		}    	
	}  

	// this method stores the movesList array into a file
	private void createAndWriteToData(){
		System.out.println("Data is written to file");
		File dataFile;
		FileWriter out;
		BufferedWriter writeFile;   

		//save the data into a file
		dataFile	=new File("./StoredGamesForConnectOfFive/"+
				OnePlayer.playerOne+"_vs_computer.dat");
		try{
			out=new FileWriter(dataFile);
			writeFile=new BufferedWriter(out);
			for (Iterator<Integer> iter=movesList.iterator(); iter.hasNext();){
				writeFile.write(iter.next()+"");
				writeFile.newLine();  
			}    	
			writeFile.close();
			out.close();
		}
		catch(Exception k){
			JOptionPane.showMessageDialog(null,"GAME CANNOT BE SAVED as file cannot be written to the current directory","GAME CANNOT BE SAVED",JOptionPane.INFORMATION_MESSAGE);
			System.err.println("Exception: "+k.getMessage());
		}
	}
}