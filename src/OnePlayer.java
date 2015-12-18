import javax.swing.*;

import java.awt.*;	 
import java.awt.event.*;
import java.io.File;

// this class enables the user to play against the computer
public class OnePlayer extends JFrame implements MouseListener{
	// declares serial version
	private static final long serialVersionUID = 1L;
	private static final int SCORE_OF_FIVE = 999999999;
	// four in a row
	private static final int SCORE_OF_LIVE_FOUR = 999999999;
	private static final int SCORE_OF_DEAD_FOUR = 99999999;
	private static final int SCORE_OF_DEAD_FOUR_PLAYER_MOVE = 599999999;	//player move = next player is the player who has the dead four
	// three in a row
	private static final int SCORE_OF_LIVE_THREE = 999999;
	private static final int SCORE_OF_LIVE_THREE_PLAYER_MOVE = 9999999;
	private static final int SCORE_OF_DEAD_THREE = 99999;
	private static final int SCORE_OF_DEAD_THREE_PLAYER_MOVE = 999999;
	// two in a row
	private static final int SCORE_OF_LIVE_TWO = 9999;
	private static final int SCORE_OF_LIVE_TWO_PLAYER_MOVE = 99999;
	private static final int SCORE_OF_DEAD_TWO = 999;
	private static final int SCORE_OF_DEAD_TWO_PLAYER_MOVE = 9999;
	private static final int SCORE_OF_ADJACENT=300;
	private static final int SCORE_RANDOM=5;
	private static final int MAX_SCORE=1000000000;

	// a static string used to store user's name
	static String playerOne;

	//define those JComponents
	ImageIcon icon=new ImageIcon("CHESSBOARD.jpg");
	JLabel label2=new JLabel(icon,SwingConstants.CENTER);
	JLabel JLTitle=new JLabel("Connect of Five",SwingConstants.CENTER);
	JLabel label3=new JLabel();
	JLabel label4=new JLabel();

	// constructor of the class
	public OnePlayer(String title){
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

		//create a frame
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
		
		// check if CHESSBOARD.jpg exist in the current directory, if not, go back to mainProgram
		File chessBoard	=new File("CHESSBOARD.jpg");
		if (chessBoard.exists()==false){
			JOptionPane.showMessageDialog(null,"Sorry, the file CHESSBOARD.jpg file cannot be found, please make sure it is present in the current directory! Program terminated!",
					"WARNING",JOptionPane.INFORMATION_MESSAGE);      
			new MainProgram("Connect of Five");
			this.dispose();
			return;
		}
		// create an interactive panel evoked when the user clicks somewhere on the chessboard
		interPanelOnePlayer myInterPanel=new interPanelOnePlayer();
		myInterPanel.setVisible(true);
		myInterPanel.setOpaque(false);
		myInterPanel.setBorder(null);
		myInterPanel.setBounds(45, 110, 718, 718);
		this.add(myInterPanel);

		//chess board
		label2.setBounds(45,110,718,718);
		this.add(label2);

		do{
			//let user input his/her name, and show in the game.
			playerOne=JOptionPane.showInputDialog(null,"Please enter your name:","");
			// create the folder if it does not exist
			File folder = new File("./StoredGamesForConnectOfFive");
			if (!folder.exists()) {
				while (!folder.mkdir()) {
					int result =JOptionPane.showConfirmDialog(null,"Sorry, the folder StoredGamesForConnectOfFive" +
							" cannot be created, please make sure the current directory is writable! \n Do you wish to retry?" +
							" YES to make the directory writable and retry, NO to contine a game without saving.",
							"WARNING",JOptionPane.YES_NO_OPTION);      
					if (result==JOptionPane.NO_OPTION)
						break;
				}
			}
			//save the data into a file
			File file	=new File("./StoredGamesForConnectOfFive/"+
					playerOne+"_vs_computer.dat");
			if (file.exists()==true){
				int result =JOptionPane.showConfirmDialog(null,
						"Game with this user already exists, do you still wish to continue? (YES to " +
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
		label4.setText("WHITE: COMPUTER");
		label4.setForeground(Color.white);
		label4.setFont(new Font("Courier New",Font.BOLD,20));
		label4.setBounds(900,650,500,50);
		this.add(label4);
		/*
		Graphics a=label4.getGraphics();
		this.update(a);
		 */

		label2.addMouseListener(this);      
	}


/************************* MINIMAX ALGORITHM STARTS *******************************/
/************************* MINIMAX ALGORITHM STARTS *******************************/
/************************* MINIMAX ALGORITHM STARTS *******************************/
/************************* MINIMAX ALGORITHM STARTS *******************************/
/************************* MINIMAX ALGORITHM STARTS *******************************/
/************************* MINIMAX ALGORITHM STARTS *******************************/
/************************* MINIMAX ALGORITHM STARTS *******************************/
	public static int whiteMoves(int alpha, int beta, int depth){
      int next_depth=depth-1;
   	int bestMove=0;
		int bestScore=MAX_SCORE;
		int curScore;    
		if (depth==0){		//base case
			return evaluateCurrentSituation(-1);	//next move is white
		}else{
			for (int i=0;i<16;i++){
				for (int j=0;j<16;j++){
					if (interPanelOnePlayer.chessBoard[i][j]==0){					
						interPanelOnePlayer.chessBoard[i][j]=-1;	//put a white stone
						curScore=blackMoves(alpha, beta, next_depth);
						if (curScore<bestScore){   //try to minimize the move
							bestScore=curScore;
							bestMove=100*i+j;
						}	
						interPanelOnePlayer.chessBoard[i][j]=0;		//undo the move
					}
				}
			}
		}
		System.out.println("whiteMove's best move"+bestMove);
		return bestScore;
	}
	public static int blackMoves(int alpha, int beta, int depth){
      int next_depth=depth-1;
   	int bestMove=0;
		int bestScore=-MAX_SCORE;
		int curScore;
		if (depth==0){		//base case
         int val=evaluateCurrentSituation(1);
         System.out.println("blackMove current situation score is "+val);
		   return val;         
		}else{
			for (int i=0;i<16;i++){
				for (int j=0;j<16;j++){
					if (interPanelOnePlayer.chessBoard[i][j]==0){					
						interPanelOnePlayer.chessBoard[i][j]=1;   //puts down a black stone
						curScore=whiteMoves(alpha, beta, next_depth);
						if (curScore>bestScore){      //try to maximize the move
							bestScore=curScore;
							bestMove=100*i+j;
						}	
						interPanelOnePlayer.chessBoard[i][j]=0;
					}
				}
			}
		}
		System.out.println("blackMoves's best move"+bestMove+" and depth is "+depth+" best score is "+bestScore);
		return bestScore;
	}
	public static int calculateNEW(){
		int MAX_DEPTH_OF_SEARCH=2;
      int next_depth=MAX_DEPTH_OF_SEARCH-1;
		int bestMove=0;
		int bestScore=MAX_SCORE;
		int curScore;

		for (int i=0;i<16;i++){
			for (int j=0;j<16;j++){
				if (interPanelOnePlayer.chessBoard[i][j]==0){
               if (i==3 && j==3){
                  i=3;
               }			
               System.out.println("i is "+i+" j is "+j);		
					interPanelOnePlayer.chessBoard[i][j]=-1;     //puts down a white stone
					curScore=blackMoves(MAX_SCORE,-MAX_SCORE,next_depth);	//alpha=-MAX_SCORE, beta=MAX_SCORE	
					if (curScore<bestScore){
						bestScore=curScore;
						bestMove=100*i+j;
					}						
					interPanelOnePlayer.chessBoard[i][j]=0;
				}
			}
		}
		System.out.println("Computer's best move"+bestMove+" best score is "+bestScore);
		return bestMove;
	}



	public static int evaluateCurrentSituation(int next_player){	//next_player can be -1(white) or 1(black)
		int score=0;
		for (int i=0;i<16;i++){
			for (int j=0;j<16;j++){
				if(interPanelOnePlayer.chessBoard[i][j]!=0){
					score+=evaluateLongestConnection(i,j,0,1, next_player);	//right
					score+=evaluateLongestConnection(i,j,1,0, next_player);	//down
					score+=evaluateLongestConnection(i,j,1,1, next_player);	//down right
					score+=evaluateLongestConnection(i,j,-1,1, next_player);	//up right
				}
			}
		}
		//score+=Math.random() * SCORE_RANDOM;
		return score;
	}
	//i,j is the left bottom corner of the LiveFour. player=1 for black, 0 for white (i.e computer)
	//(up,right) can be (1,0) (0,1) (1,1) or (-1,1)
	public static int evaluateLongestConnection(int i, int j, int up, int right, int next_player){	
		int player=interPanelOnePlayer.chessBoard[i][j];	// can be 1 (black), -1(white) or 0 (empty)
		boolean is_player_move=(player==next_player) ? true : false;
		if (player==0)
			assert(false);
		int num=0;
		boolean skip=false;

		if (i+up<16 && i+up>0 && j+right<16 && interPanelOnePlayer.chessBoard[i+up][j+right]==player){		//two in a row		
			if (i+up*2<16 && i+up*2>0 && j+right*2<16 && interPanelOnePlayer.chessBoard[i+up*2][j+right*2]==player){
				if (i+up*3<16 && i+up*3>0 && j+right*3<16 && interPanelOnePlayer.chessBoard[i+up*3][j+right*3]==player){
					if (i+up*4<16 && i+up*4>0 && j+right*4<16 && interPanelOnePlayer.chessBoard[i+up*4][j+right*4]==player){
						num+=SCORE_OF_FIVE;
						skip=true;		//already five in a row, need not consider any other cases
					}else{
						//four in a row and the fifth one is not
						if (skip){
							num+=0;
						}else if (i+up*4<16 && i+up*4>0 && j+right*4<16 && interPanelOnePlayer.chessBoard[i+up*4][j+right*4]==0 &&
							(i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0)){
							num+=SCORE_OF_LIVE_FOUR;
							skip=true;
						}else if (i+up*4<16 && i+up*4>0 && j+right*4<16 && interPanelOnePlayer.chessBoard[i+up*4][j+right*4]==0){							
							num+=is_player_move ? SCORE_OF_DEAD_FOUR_PLAYER_MOVE : SCORE_OF_DEAD_FOUR;
							//skip=true;
						}
						else if (i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0){
							num+=is_player_move ? SCORE_OF_DEAD_FOUR_PLAYER_MOVE : SCORE_OF_DEAD_FOUR;
							//skip=true;
						}
					}
				}else{
					//three in a row and the forth one is not
					if (skip){
						num+=0;					
					//Case#1: forth elem is empty & fifth is mine again
					}else if (i+up*3<16 && i+up*3>0 && j+right*3<16 && interPanelOnePlayer.chessBoard[i+up*3][j+right*3]==0 &&
						i+up*4<16 && i+up*4>0 && j+right*4<16 && interPanelOnePlayer.chessBoard[i+up*4][j+right*4]==player){
						if (i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0){
							num+=is_player_move ? SCORE_OF_LIVE_THREE_PLAYER_MOVE : SCORE_OF_LIVE_THREE;
						}
						num+=is_player_move ? SCORE_OF_DEAD_FOUR_PLAYER_MOVE : SCORE_OF_DEAD_FOUR;
					}

					if (skip){
						num+=0;					
					//Case#2: -1th elem is empty & -2 is mine again
					}else if (i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0 &&
						i-up*2>0 && i-up*2<16 && j-right*2>0 && interPanelOnePlayer.chessBoard[i-up*2][j-right*2]==player){
						if (i+up*3<16 && i+up*3>0 && j+right*3<16 && interPanelOnePlayer.chessBoard[i+up*3][j+right*3]==0){
							num+=is_player_move ? SCORE_OF_LIVE_THREE_PLAYER_MOVE : SCORE_OF_LIVE_THREE;
						}
						num+=is_player_move ? SCORE_OF_DEAD_FOUR_PLAYER_MOVE : SCORE_OF_DEAD_FOUR;							
					//case#3: normal deadThree or liveThree
					}else if (i+up*3<16 && i+up*3>0 && j+right*3<16 && interPanelOnePlayer.chessBoard[i+up*3][j+right*3]==0 &&
						(i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0)){
						num+=is_player_move ? SCORE_OF_LIVE_THREE_PLAYER_MOVE : SCORE_OF_LIVE_THREE;
					}else if (i+up*3<16 && i+up*3>0 && j+right*3<16 && interPanelOnePlayer.chessBoard[i+up*3][j+right*3]==0){							
						num+=is_player_move ? SCORE_OF_DEAD_THREE_PLAYER_MOVE : SCORE_OF_DEAD_THREE;
					}
					else if (i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0){
						num+=is_player_move ? SCORE_OF_DEAD_THREE_PLAYER_MOVE : SCORE_OF_DEAD_THREE;
					}
					//skip=true;
				}
			}else{
				//three in a row and the forth one is not
				if (skip){
					num+=0;					
				//Case#1: third elem is empty & forth & fifth are mine again NOTE:no need to do -1 & -2 as it's symmetrical and we do not over-count
				}else if (i+up*2<16 && i+up*2>0 && j+right*2<16 && interPanelOnePlayer.chessBoard[i+up*2][j+right*2]==0 &&
					i+up*3<16 && i+up*3>0 && j+right*3<16 && interPanelOnePlayer.chessBoard[i+up*3][j+right*3]==player &&
					i+up*4<16 && i+up*4>0 && j+right*4<16 && interPanelOnePlayer.chessBoard[i+up*4][j+right*4]==player){
					if (i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0){
						num+=is_player_move ? SCORE_OF_LIVE_TWO_PLAYER_MOVE : SCORE_OF_LIVE_TWO;
					}
					if (i+up*5>0 && i+up*5<16 && j+right*5>0 && interPanelOnePlayer.chessBoard[i+up*5][j+right*5]==0){
						num+=is_player_move ? SCORE_OF_LIVE_TWO_PLAYER_MOVE : SCORE_OF_LIVE_TWO;
					}	
					num+=is_player_move ? SCORE_OF_DEAD_FOUR_PLAYER_MOVE : SCORE_OF_DEAD_FOUR;
				//Case#2: third elem is empty & forth are mine again
				}else if (i+up*2<16 && i+up*2>0 && j+right*2<16 && interPanelOnePlayer.chessBoard[i+up*2][j+right*2]==0 &&
					i+up*3<16 && i+up*3>0 && j+right*3<16 && interPanelOnePlayer.chessBoard[i+up*3][j+right*3]==player){
					if (i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0){
						num+=is_player_move ? SCORE_OF_LIVE_TWO_PLAYER_MOVE : SCORE_OF_LIVE_TWO;
					}
					num+=is_player_move ? SCORE_OF_DEAD_THREE_PLAYER_MOVE : SCORE_OF_DEAD_THREE;									
				}

				if (skip){
					num+=0;					
				//Case#3: -1th elem is empty & -2 is mine again
				}else if (i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0 &&
					i-up*2>0 && i-up*2<16 && j-right*2>0 && interPanelOnePlayer.chessBoard[i-up*2][j-right*2]==player){
					if (i+up*2<16 && i+up*2>0 && j+right*2<16 && interPanelOnePlayer.chessBoard[i+up*2][j+right*2]==0){
						num+=is_player_move ? SCORE_OF_LIVE_TWO_PLAYER_MOVE : SCORE_OF_LIVE_TWO;
					}
					num+=is_player_move ? SCORE_OF_DEAD_THREE_PLAYER_MOVE : SCORE_OF_DEAD_THREE;
				//case#4: normal deadTwo or liveTwo
				}else if (i+up*2<16 && i+up*2>0 && j+right*2<16 && interPanelOnePlayer.chessBoard[i+up*2][j+right*2]==0 &&
					(i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0)){
					num+=is_player_move ? SCORE_OF_LIVE_TWO_PLAYER_MOVE : SCORE_OF_LIVE_TWO;
				}else if (i+up*2<16 && i+up*2>0 && j+right*2<16 && interPanelOnePlayer.chessBoard[i+up*2][j+right*2]==0){							
					num+=is_player_move ? SCORE_OF_DEAD_TWO_PLAYER_MOVE : SCORE_OF_DEAD_TWO;
				}
				else if (i-up>0 && i-up<16 && j-right>0 && interPanelOnePlayer.chessBoard[i-up][j-right]==0){
					num+=is_player_move ? SCORE_OF_DEAD_TWO_PLAYER_MOVE : SCORE_OF_DEAD_TWO;
				} 
				//skip=true;
			}
		}else{
			if (i+up>0 && i+up<16 && j+right>0 && j+right<16 && interPanelOnePlayer.chessBoard[i+up][j+right]!=0){
			   num+=SCORE_OF_ADJACENT;
		   }
		}
		if (next_player==1)
			num*=-1;

		return num;
	}
/************************* MINIMAX ALGORITHM ENDS *******************************/
/************************* MINIMAX ALGORITHM ENDS *******************************/
/************************* MINIMAX ALGORITHM ENDS *******************************/
/************************* MINIMAX ALGORITHM ENDS *******************************/
/************************* MINIMAX ALGORITHM ENDS *******************************/
/************************* MINIMAX ALGORITHM ENDS *******************************/
/************************* MINIMAX ALGORITHM ENDS *******************************/



	// is excited when the user clicks the mouse
	public void mouseClicked(MouseEvent e){

	}    	


	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {      
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {      
	}

	//this method checks if white wins
	public static boolean IsBlackWin(int a[][]){
		for (int i=0;i<16;i++){
			for (int j=0;j<16;j++){
				if (j<12 && a[i][j]==1 && a[i][j+1]==1 && a[i][j+2]==1 && a[i][j+3]==1 && a[i][j+4]==1){
					return true;
				}
				else if (j<12 && i<12 && a[i][j]==1 && a[i+1][j+1]==1 && a[i+2][j+2]==1 && a[i+3][j+3]==1 && a[i+4][j+4]==1){
					return true;
				}
				else if (j<12 && i>3 && a[i][j]==1 && a[i-1][j+1]==1 && a[i-2][j+2]==1 && a[i-3][j+3]==1 && a[i-4][j+4]==1){
					return true;
				}
				else if (i<12 && a[i][j]==1 && a[i+1][j]==1 && a[i+2][j]==1 && a[i+3][j]==1 && a[i+4][j]==1){
					return true;
				}
			}
		}
		return false;
	}

	//this method checks if white wins
	public static boolean IsWhiteWin(int a[][]){
		for (int i=0;i<16;i++){
			for (int j=0;j<16;j++){
				if (j<12 && a[i][j]==-1 && a[i][j+1]==-1 && a[i][j+2]==-1 && a[i][j+3]==-1 && a[i][j+4]==-1){
					return true;
				}
				else if (i<12 && j<12 && a[i][j]==-1 && a[i+1][j+1]==-1 && a[i+2][j+2]==-1 && a[i+3][j+3]==-1 && a[i+4][j+4]==-1){
					return true;
				}
				else if (i>3 && j<12 && a[i][j]==-1 && a[i-1][j+1]==-1 && a[i-2][j+2]==-1 && a[i-3][j+3]==-1 && a[i-4][j+4]==-1){
					return true;
				}
				else if (i<12 && a[i][j]==-1 && a[i+1][j]==-1 && a[i+2][j]==-1 && a[i+3][j]==-1 && a[i+4][j]==-1){
					return true;
				}            
			}
		}
		return false;
	}

	// main method
	public static void main(String[] args){
		new OnePlayer("Connect of Five");
	}



/************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
/************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
/************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
/************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
/************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
/************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
/************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
/************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
	// this method helps the computer calculate the points that correspond to each positon so that the computer can choose its best move
	public static int Calculate(){

		// initialize the points of each position as 0
		int[][] score = new int [16][16];
		for (int i=0;i<16;i++){
			for (int j=0;j<16;j++){
				score[i][j]=0;
			}
		}

		//variables to store the best move
		int bestScore=0;
		int bestMove=0;

		// go over each available place to find the best move
		for (int i=0;i<16;i++){
			for (int j=0;j<16;j++){
				if (interPanelOnePlayer.chessBoard[i][j]==0){
					if (ConnectFive(i,j)!=0){
						score[i][j]+=100000000*ConnectFive(i,j);
					}
					if (BlockFour( i, j)!=0){
						score [i][j]+=20000000*BlockFour(i,j);
					}
					if (ConnectFour(i,j)!=0){
						score[i][j]+=4000000*ConnectFour(i,j);
					}
					if (BlockThree(i,j)!=0){
						score[i][j]+=800000*BlockThree(i,j);
					}
					if (ConnectThree2(i,j)!=0){		// if connect three with both sides open (i.e huosan)
						score[i][j]+=160000*ConnectThree2(i,j);
					}
					if (ConnectThree1(i,j)!=0){
						score[i][j]+=8000*ConnectThree1(i,j);
					}
					if (ConnectTwo2(i,j)!=0){
						score[i][j]+=8000*ConnectTwo2(i,j);
					}
					if (ConnectTwo1(i,j)!=0){
						score[i][j]+=1600*ConnectTwo1(i,j);
					}
					if(BlockTwo(i,j)!=0){
						score[i][j]+=1600*BlockTwo(i,j);
					}
					if(BlockOne(i,j)!=0){
						score[i][j]+=600*BlockOne(i,j);
					}      
					if(i==0 || j==0 || i==15 || j==15){
						score[i][j]-=100;
					}else if(i==1 || j==1 || i==14 || j==14){
						score[i][j]-=80;
					}else if(i==2 || j==2 || i==13 || j==13){
						score[i][j]-=60;
					}else if(i==3 || j==3 || i==12 || j==12){
						score[i][j]-=40;
					}else if(i==4 || j==4 || i==11 || j==11){
						score[i][j]-=20;
					}else if (i>6 && i<9 && j>6 && j<9){
						score[i][j]+=40;
					}
					score[i][j]+=Math.random() * 30;
					if (score[i][j]>=bestScore){
						bestScore=score[i][j];
						bestMove=100*i+j;
					}
				}
			}
		}

		/*
		// find the best move
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				if (score[i][j]>=bestScore){
					bestScore=score[i][j];
					bestMove=100*i+j;
				}
			}
		}
		 */

		System.out.print("BlockThree:"+BlockThree(bestMove/100,bestMove%100));    
		System.out.println("ConnectTheree2:"+ConnectThree2(bestMove/100,bestMove%100)); 
		System.out.print("ConnectTheree1:"+ConnectThree1(bestMove/100,bestMove%100));  
		System.out.println("ConnectTwo2:"+ConnectTwo2(bestMove/100,bestMove%100));
		System.out.print("ConnectTwo1:"+ConnectTwo1(bestMove/100,bestMove%100)); 
		System.out.println("BlockTwo:"+BlockTwo(bestMove/100,bestMove%100));
		System.out.print("BlockOne:"+BlockOne(bestMove/100,bestMove%100));  
		System.out.println("Best Score:"+bestScore);

		return bestMove;
	}


	public static int  BlockFour(int i,int j){//enable the computer to be able to block the user when the user gets 4 stones in one row,col,diagonal.
		int num=0;
		if (j<12 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1 && interPanelOnePlayer.chessBoard[i][j+2]==1 && interPanelOnePlayer.chessBoard[i][j+3]==1 && interPanelOnePlayer.chessBoard[i][j+4]==1){
			num++;
		}
		if (j>3 && interPanelOnePlayer.chessBoard[i][j-4]==1 && interPanelOnePlayer.chessBoard[i][j-3]==1 && interPanelOnePlayer.chessBoard[i][j-2]==1 && interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (j<13 && j>0 &&  interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1 && interPanelOnePlayer.chessBoard[i][j+2]==1 && interPanelOnePlayer.chessBoard[i][j+3]==1){
			num++;
		}

		if (j<15 && j>2 && interPanelOnePlayer.chessBoard[i][j-3]==1 && interPanelOnePlayer.chessBoard[i][j-2]==1 && interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1){
			num++;
		}

		if (j<14 && j>1 && interPanelOnePlayer.chessBoard[i][j-2]==1 && interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1 && interPanelOnePlayer.chessBoard[i][j+2]==1){
			num++;
		}
		//vertical

		if (j<12 && i<12 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1 && interPanelOnePlayer.chessBoard[i+2][j+2]==1 && interPanelOnePlayer.chessBoard[i+3][j+3]==1 && interPanelOnePlayer.chessBoard[i+4][j+4]==1){
			num++;
		}

		if (j>3 && i>3 && interPanelOnePlayer.chessBoard[i-4][j-4]==1 && interPanelOnePlayer.chessBoard[i-3][j-3]==1 && interPanelOnePlayer.chessBoard[i-2][j-2]==1 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}

		if (j<13 && i<13 && j>0 && i>0 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1 && interPanelOnePlayer.chessBoard[i+2][j+2]==1 && interPanelOnePlayer.chessBoard[i+3][j+3]==1){
			num++;
		}

		if (j<15 && i<15 && j>2 && i>2 && interPanelOnePlayer.chessBoard[i-3][j-3]==1 && interPanelOnePlayer.chessBoard[i-2][j-2]==1 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1){
			num++;
		}

		if (j<14 && i<14 && j>1 && i>1 && interPanelOnePlayer.chessBoard[i-2][j-2]==1 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1 && interPanelOnePlayer.chessBoard[i+2][j+2]==1){
			num++;
		}
		//diagonal


		if (j<12 && i>3 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1 && interPanelOnePlayer.chessBoard[i-2][j+2]==1 && interPanelOnePlayer.chessBoard[i-3][j+3]==1 && interPanelOnePlayer.chessBoard[i-4][j+4]==1){
			num++;
		}
		if (j>3 && i<12 && interPanelOnePlayer.chessBoard[i+4][j-4]==1 && interPanelOnePlayer.chessBoard[i+3][j-3]==1 && interPanelOnePlayer.chessBoard[i+2][j-2]==1 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (j<13 && i<15 && j>0 && i>2 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1 && interPanelOnePlayer.chessBoard[i-2][j+2]==1 && interPanelOnePlayer.chessBoard[i-3][j+3]==1){
			num++;
		}
		if (j<15 && i<13 && j>2 && i>0 && interPanelOnePlayer.chessBoard[i+3][j-3]==1 && interPanelOnePlayer.chessBoard[i+2][j-2]==1 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1){
			num++;
		}
		if (j<14 && i<14 && j>1 && i>1 && interPanelOnePlayer.chessBoard[i+2][j-2]==1 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1 && interPanelOnePlayer.chessBoard[i-2][j+2]==1){
			num++;
		}			
		//diagonal													
		if (i<12 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1 && interPanelOnePlayer.chessBoard[i+2][j]==1 && interPanelOnePlayer.chessBoard[i+3][j]==1 && interPanelOnePlayer.chessBoard[i+4][j]==1){
			num++;
		}
		if (i>3 && interPanelOnePlayer.chessBoard[i-4][j]==1 && interPanelOnePlayer.chessBoard[i-3][j]==1 && interPanelOnePlayer.chessBoard[i-2][j]==1 && interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (i<15 && i>2 && interPanelOnePlayer.chessBoard[i-3][j]==1 && interPanelOnePlayer.chessBoard[i-2][j]==1 && interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1){
			num++;
		}
		if (i<13 && i>0 && interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1 && interPanelOnePlayer.chessBoard[i+2][j]==1 && interPanelOnePlayer.chessBoard[i+3][j]==1){
			num++;
		}															
		if (i<14 && i>1 && interPanelOnePlayer.chessBoard[i-2][j]==1 && interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1 && interPanelOnePlayer.chessBoard[i+2][j]==1){
			num++;
		}
		//horizontal

		return num;
	}



	public static int  BlockThree(int i,int j){// enable the computer be able to block the user when user gets3 stones in one row.
		int num=0;
		if (j<13 && j>0 && interPanelOnePlayer.chessBoard[i][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1 && interPanelOnePlayer.chessBoard[i][j+2]==1 && interPanelOnePlayer.chessBoard[i][j+3]==1 ){
			num++;
		}
		if (j>2 && j<15 && interPanelOnePlayer.chessBoard[i][j-3]==1 && interPanelOnePlayer.chessBoard[i][j-2]==1 && interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==0){
			num++;
		}
		if (j>0 && j<14 && interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1 && interPanelOnePlayer.chessBoard[i][j+2]==1 ){
			num++;
		}
		if (j>1 && j<15 && interPanelOnePlayer.chessBoard[i][j-2]==1 && interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1 ){
			num++;
		}

		//row

		if (j<13 && i<13 && i>0 && j>0  && interPanelOnePlayer.chessBoard[i-1][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1 && interPanelOnePlayer.chessBoard[i+2][j+2]==1 && interPanelOnePlayer.chessBoard[i+3][j+3]==1){
			num++;
		}

		if (j>2 && i>2 && i<15 && j<15 && interPanelOnePlayer.chessBoard[i-3][j-3]==1 && interPanelOnePlayer.chessBoard[i-2][j-2]==1 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==0){
			num++;
		}

		if (i>0 && i<14 && j>0 && j<14 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1 && interPanelOnePlayer.chessBoard[i+2][j+2]==1) {
			num++;
		}

		if (i>1 && i<15 && j>1 && j<15 && interPanelOnePlayer.chessBoard[i-2][j-2]==1 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1){
			num++;
		}

		//diagonal
		if (i>2 && j<13 && i<15 && j>0 && interPanelOnePlayer.chessBoard[i+1][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1 && interPanelOnePlayer.chessBoard[i-2][j+2]==1 && interPanelOnePlayer.chessBoard[i-3][j+3]==1 ){
			num++;
		}
		if (j>2 && i<13 && j<15 && i>0 && interPanelOnePlayer.chessBoard[i+3][j-3]==1 && interPanelOnePlayer.chessBoard[i+2][j-2]==1 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==0 ){
			num++;
		}
		if (i>1 && i<15 && j>0 && j<14 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1 && interPanelOnePlayer.chessBoard[i-2][j+2]==1 ){
			num++;
		}
		if (j>1 && j<15 && i>0 && i<14 &&  interPanelOnePlayer.chessBoard[i+2][j-2]==1 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1){
			num++;
		}

		//diagonal													
		if (i<13 && i>0 && interPanelOnePlayer.chessBoard[i-1][j]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1 && interPanelOnePlayer.chessBoard[i+2][j]==1 && interPanelOnePlayer.chessBoard[i+3][j]==1 ){
			num++;
		}
		if (i>2 && i<15 && interPanelOnePlayer.chessBoard[i-3][j]==1 && interPanelOnePlayer.chessBoard[i-2][j]==1 && interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==0){
			num++;
		}
		if (i>1 && i<15 &&interPanelOnePlayer.chessBoard[i-2][j]==1 && interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1 ){
			num++;
		}
		if (i>0 && i<14 && interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1 && interPanelOnePlayer.chessBoard[i+2][j]==1){
			num++;
		}															
		//col
		return num;
	}


	public static int  BlockTwo(int i,int j){// enable the computer be able to block the user when user gets3 stones in one row.
		int num=0;
		if (j<14 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1 && interPanelOnePlayer.chessBoard[i][j+2]==1  ){
			num++;
		}
		if (j>1 && interPanelOnePlayer.chessBoard[i][j-2]==1 && interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (j>0 && j<15 && interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1 ){
			num++;
		}

		//horiaontal

		if  (j<14 && i<14 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1 && interPanelOnePlayer.chessBoard[i+2][j+2]==1 ){
			num++;
		}
		if (j>0 && j<15 && i>0 && i<15 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1 ) {
			num++;
		}

		if (j>1 && i>1 && interPanelOnePlayer.chessBoard[i-2][j-2]==1 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 ){
			num++;
		}

		//diagonal
		if (j<14 && i>1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1 && interPanelOnePlayer.chessBoard[i-2][j+2]==1){
			num++;
		}
		if (j>0 && j<15 && i>0 && i<15 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1){
			num++;
		}
		if (i<14 && j>1 && interPanelOnePlayer.chessBoard[i+2][j-2]==1 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}

		//diagonal													
		if (i<14  && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1 && interPanelOnePlayer.chessBoard[i+2][j]==1  ){
			num++;
		}
		if (i>1 && interPanelOnePlayer.chessBoard[i-2][j]==1 && interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (i<15 && i>0 &&  interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1){
			num++;
		}															

		//col

		return num;
	}




	public static int  BlockOne (int i,int j){// enable the computer to make its first move.
		int num=0;
		if (j<15 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==1){
			num++;
		}
		if (j>0 && interPanelOnePlayer.chessBoard[i][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}

		//vertical
		if (i<15 && j<15 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==1 ){
			num++;
		}

		if (i>0 && j>0 && interPanelOnePlayer.chessBoard[i-1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}

		//diagonal
		if (i>0 && j<15 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==1 ){
			num++;
		}
		if (i<15 && j>0 && interPanelOnePlayer.chessBoard[i+1][j-1]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}

		//diagonal													
		if (i<15 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==1 ){
			num++;
		}
		if (i>0 && interPanelOnePlayer.chessBoard[i-1][j]==1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		//horizontal

		return num;
	}

	public static int  ConnectFive(int i,int j){
		int num=0;
		if (j<12 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 && interPanelOnePlayer.chessBoard[i][j+2]==-1 && interPanelOnePlayer.chessBoard[i][j+3]==-1 && interPanelOnePlayer.chessBoard[i][j+4]==-1){
			num++;
		}
		if (j>3 && interPanelOnePlayer.chessBoard[i][j-4]==-1 && interPanelOnePlayer.chessBoard[i][j-3]==-1 && interPanelOnePlayer.chessBoard[i][j-2]==-1 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (j>0 && j<13 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 && interPanelOnePlayer.chessBoard[i][j+2]==-1 && interPanelOnePlayer.chessBoard[i][j+3]==-1){
			num++;
		}
		if (j>2 && j<15 && interPanelOnePlayer.chessBoard[i][j-3]==-1 && interPanelOnePlayer.chessBoard[i][j-2]==-1 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1){
			num++;
		}
		if (j>1 && j<14 && interPanelOnePlayer.chessBoard[i][j-2]==-1 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 && interPanelOnePlayer.chessBoard[i][j+2]==-1){
			num++;
		}
		//row

		if (i<12 && j<12 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 && interPanelOnePlayer.chessBoard[i+2][j+2]==-1 && interPanelOnePlayer.chessBoard[i+3][j+3]==-1 && interPanelOnePlayer.chessBoard[i+4][j+4]==-1){
			num++;
		}

		if (i>3 && j>3 &&  interPanelOnePlayer.chessBoard[i-4][j-4]==-1 && interPanelOnePlayer.chessBoard[i-3][j-3]==-1 && interPanelOnePlayer.chessBoard[i-2][j-2]==-1 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}

		if (i>0 && i<13 && j>0 && j<13 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 && interPanelOnePlayer.chessBoard[i+2][j+2]==-1 && interPanelOnePlayer.chessBoard[i+3][j+3]==-1){
			num++;
		}

		if (i>2 && i<15 && j>2 && j<15 && interPanelOnePlayer.chessBoard[i-3][j-3]==-1 && interPanelOnePlayer.chessBoard[i-2][j-2]==-1 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1){
			num++;
		}

		if (i>1 && i<14 && j>1 && j<14 && interPanelOnePlayer.chessBoard[i-2][j-2]==-1 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+2][j+2]==-1 && interPanelOnePlayer.chessBoard[i+3][j+3]==-1){
			num++;
		}
		//diagonal

		if (i>3 && j<12 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1 && interPanelOnePlayer.chessBoard[i-2][j+2]==-1 && interPanelOnePlayer.chessBoard[i-3][j+3]==-1 && interPanelOnePlayer.chessBoard[i-4][j+4]==-1){
			num++;
		}
		if (j>3 && i<12 && interPanelOnePlayer.chessBoard[i+4][j-4]==-1 && interPanelOnePlayer.chessBoard[i+3][j-3]==-1 && interPanelOnePlayer.chessBoard[i+2][j-2]==-1 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (i>2 && i<15 && j>0 && j<13 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1 && interPanelOnePlayer.chessBoard[i-2][j+2]==-1 && interPanelOnePlayer.chessBoard[i-3][j+3]==-1){
			num++;
		}
		if (i>0 && i<13 && j>2 && j<15 && interPanelOnePlayer.chessBoard[i+3][j-3]==-1 && interPanelOnePlayer.chessBoard[i+2][j-2]==-1 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1){
			num++;
		}
		if (i>1 && i<14 && j>1 && j<14 && interPanelOnePlayer.chessBoard[i+2][j-2]==-1 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1 && interPanelOnePlayer.chessBoard[i-2][j+2]==-1){
			num++;
		}			
		//diagonal													
		if ( i<12 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 && interPanelOnePlayer.chessBoard[i+2][j]==-1 && interPanelOnePlayer.chessBoard[i+3][j]==-1 && interPanelOnePlayer.chessBoard[i+4][j]==-1){
			num++;
		}
		if (i>3 && interPanelOnePlayer.chessBoard[i-4][j]==-1 && interPanelOnePlayer.chessBoard[i-3][j]==-1 && interPanelOnePlayer.chessBoard[i-2][j]==-1 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (i>2 && i<15 && interPanelOnePlayer.chessBoard[i-3][j]==-1 && interPanelOnePlayer.chessBoard[i-2][j]==-1 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1){
			num++;
		}
		if (i>0 && i<13 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 && interPanelOnePlayer.chessBoard[i+2][j]==-1 && interPanelOnePlayer.chessBoard[i+3][j]==-1){
			num++;
		}															
		if (i>1 && i<14 && interPanelOnePlayer.chessBoard[i-2][j]==-1 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 && interPanelOnePlayer.chessBoard[i+2][j]==-1){
			num++;
		}
		//col
		return num;
	}


	public static int  ConnectFour(int i,int j){ // Let computer be able to connect four stones.
		int num=0;
		if (j>0 && j<13 && interPanelOnePlayer.chessBoard[i][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 && interPanelOnePlayer.chessBoard[i][j+2]==-1 && interPanelOnePlayer.chessBoard[i][j+3]==-1 ){
			num++;
		}
		if (j>3 && j<15 && interPanelOnePlayer.chessBoard[i][j-3]==-1 && interPanelOnePlayer.chessBoard[i][j-2]==-1 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==0){
			num++;
		}
		if (j>0 && j<14 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 && interPanelOnePlayer.chessBoard[i][j+2]==-1 ){
			num++;
		}
		if (j>1 && j<15 &&   interPanelOnePlayer.chessBoard[i][j-2]==-1 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 ){
			num++;
		}

		//row

		if (i>0 && i<13 && j>0 && j<13 && interPanelOnePlayer.chessBoard[i-1][j-1]==0 &&interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 && interPanelOnePlayer.chessBoard[i+2][j+2]==-1 && interPanelOnePlayer.chessBoard[i+3][j+3]==-1){
			num++;
		}
		if (i>3 && i<15 && j>3 && j<15 &&  interPanelOnePlayer.chessBoard[i-3][j-3]==-1 && interPanelOnePlayer.chessBoard[i-2][j-2]==-1 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==0){
			num++;
		}
		if (i>0 && i<14 && j>0 && j<14 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 && interPanelOnePlayer.chessBoard[i+2][j+2]==-1) {
			num++;
		}
		if (i>1 && i<15 &&  j>1 && j<15 &&  interPanelOnePlayer.chessBoard[i-2][j-2]==-1 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1){
			num++;
		}

		//diagonal
		if (j>0 && j<13 && i>2 && i<15 && interPanelOnePlayer.chessBoard[i+1][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1 && interPanelOnePlayer.chessBoard[i-2][j+2]==-1 && interPanelOnePlayer.chessBoard[i-3][j+3]==-1 ){
			num++;
		}
		if (i>0 && i<13 && j>2 && j<15 && interPanelOnePlayer.chessBoard[i+3][j-3]==-1 && interPanelOnePlayer.chessBoard[i+2][j-2]==-1 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==0){
			num++;
		}
		if (i>1 && i<15 && j>0 && j<14 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1 && interPanelOnePlayer.chessBoard[i-2][j+2]==-1 ){
			num++;
		}
		if (j>1 && j<15 && i>0 && i<14 && interPanelOnePlayer.chessBoard[i+2][j-2]==-1 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1){
			num++;
		}

		//diagonal														
		if (i>0 && i<13 && interPanelOnePlayer.chessBoard[i-1][j]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 && interPanelOnePlayer.chessBoard[i+2][j]==-1 && interPanelOnePlayer.chessBoard[i+3][j]==-1 ){
			num++;
		}
		if (i>2 && i<15 &&  interPanelOnePlayer.chessBoard[i-3][j]==-1 && interPanelOnePlayer.chessBoard[i-2][j]==-1 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==0 ){
			num++;
		}
		if (i>1 && i<15 &&  interPanelOnePlayer.chessBoard[i-2][j]==-1 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1){
			num++;
		}
		if (i>0 && i<14 &&  interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 && interPanelOnePlayer.chessBoard[i+2][j]==-1){
			num++;
		}															
		//col
		return num;
	}   

	public static int  ConnectThree2(int i,int j){ //let the computer be able to connect 3 stones with two space on each side.
		int num=0;
		if (j>0 && j<13 && interPanelOnePlayer.chessBoard[i][j-1]==0 &&interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 && interPanelOnePlayer.chessBoard[i][j+2]==-1&& interPanelOnePlayer.chessBoard[i][j+3]==0){
			num++;
		}
		if (j>2 && j<15 && interPanelOnePlayer.chessBoard[i][j-3]==0 &&interPanelOnePlayer.chessBoard[i][j-2]==-1 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0&& interPanelOnePlayer.chessBoard[i][j+1]==0){
			num++;
		}
		if (j>1 && j<14 && interPanelOnePlayer.chessBoard[i][j-2]==0 &&interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1&& interPanelOnePlayer.chessBoard[i][j+2]==0 ){
			num++;
		}

		//vertical
		if (i>0 && i<13 &&  j>0 && j<13 &&  interPanelOnePlayer.chessBoard[i-1][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 && interPanelOnePlayer.chessBoard[i+2][j+2]==-1 && interPanelOnePlayer.chessBoard[i+3][j+3]==0 ){
			num++;
		}
		if (i>2 && i<15 && j>2 && j<15 && interPanelOnePlayer.chessBoard[i-3][j-3]==0 && interPanelOnePlayer.chessBoard[i-2][j-2]==-1 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==0){
			num++;
		}      
		if (i>1 && i<14 && j>1 && j<14 && interPanelOnePlayer.chessBoard[i-2][j-2]==0 &&interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 && interPanelOnePlayer.chessBoard[i+2][j+2]==0) {
			num++;
		}


		//diagonal
		if (i>2 && i<15 && j>0 && j<13 && interPanelOnePlayer.chessBoard[i+1][j-1]==0 &&interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1 && interPanelOnePlayer.chessBoard[i-2][j+2]==-1 && interPanelOnePlayer.chessBoard[i-3][j+3]==0 ){
			num++;
		}
		if (j>2 && j<15 && i>0 && i<13 && interPanelOnePlayer.chessBoard[i+3][j-3]==0 &&interPanelOnePlayer.chessBoard[i+2][j-2]==-1 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==0){
			num++;
		}
		if (i>1 && i<14 && j>1 && j<14 && interPanelOnePlayer.chessBoard[i+2][j-2]==-0 &&interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1&& interPanelOnePlayer.chessBoard[i-2][j+2]==0 ){
			num++;
		}

		//diagonal													
		if (i>0 && i<13 && interPanelOnePlayer.chessBoard[i-1][j]==0 &&interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 && interPanelOnePlayer.chessBoard[i+2][j]==-1 && interPanelOnePlayer.chessBoard[i+3][j]==-1 ){
			num++;
		}
		if (i>2 && i<15 && interPanelOnePlayer.chessBoard[i-3][j]==0 &&interPanelOnePlayer.chessBoard[i-2][j]==-1 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==0){
			num++;
		}
		if (i>1 && i<14 && interPanelOnePlayer.chessBoard[i-2][j]==0 && interPanelOnePlayer.chessBoard[i-1][j]==-1 &&interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 && interPanelOnePlayer.chessBoard[i+2][j]==0){
			num++;
		}															

		//horizontal
		return num;
	}

	public static int  ConnectThree1(int i,int j){ //let the computer be able to connect 3 stones with 1 space on one side.
		int num=0;
		if (j<14 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 && interPanelOnePlayer.chessBoard[i][j+2]==-1){
			num++;
		}
		if (j>1 && interPanelOnePlayer.chessBoard[i][j-2]==-1 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (j>0 && j<15 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 ){
			num++;
		}

		//vertical
		if (i<14 && j<14 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 && interPanelOnePlayer.chessBoard[i+2][j+2]==-1 ){
			num++;
		}
		if (i>1 && j>1 && interPanelOnePlayer.chessBoard[i-2][j-2]==-1 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}      
		if (i>0 && i<15 && j>0 && j<15 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 ) {
			num++;
		}

		//diagonal
		if (i>1 && j<14 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1 && interPanelOnePlayer.chessBoard[i-2][j+2]==-1 ){
			num++;
		}
		if (j>1 && i<14 && interPanelOnePlayer.chessBoard[i+2][j-2]==-1 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (i>0 && i<15 && j>0 && j<15 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1){
			num++;
		}

		//diagonal													
		if (i<14 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 && interPanelOnePlayer.chessBoard[i+2][j]==-1 ){
			num++;
		}
		if (i>1 &&interPanelOnePlayer.chessBoard[i-2][j]==-1 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		if (i>0 && i<15 && interPanelOnePlayer.chessBoard[i-1][j]==-1 &&interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 ){
			num++;
		}															

		//horizontal
		return num;
	}
	public static int  ConnectTwo2 (int i,int j){ //enable the computer to connect two stones.
		int num=0;
		if (j>0 && j<14 && interPanelOnePlayer.chessBoard[i][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1 && interPanelOnePlayer.chessBoard[i][j+2]==0){
			num++;
		}
		if ( j<13 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==0 && interPanelOnePlayer.chessBoard[i][j+2]==-1 && interPanelOnePlayer.chessBoard[i][j+3]==0){
			num++;
		}
		if (j>1 && j<15 && interPanelOnePlayer.chessBoard[i][j-2]==0 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==0){
			num++;
		}
		if (j>2 && interPanelOnePlayer.chessBoard[i][j-3]==0 && interPanelOnePlayer.chessBoard[i][j-2]==-1 && interPanelOnePlayer.chessBoard[i][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==0){
			num++;
		}
		//vertical

		if (i>0 && i<14 && j>0 && j<14 && interPanelOnePlayer.chessBoard[i-1][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 && interPanelOnePlayer.chessBoard[i+2][j+2]==0){
			num++;
		}
		if (i>1 && i<15 && j>1 && j<15 && interPanelOnePlayer.chessBoard[i-2][j-2]==0 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==0){
			num++;
		}              
		//diagonal
		if (i>1 && i<15 && j>0 && j<14 && interPanelOnePlayer.chessBoard[i+1][j-1]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1 && interPanelOnePlayer.chessBoard[i-2][j+2]==0){
			num++;
		}
		if (i>0 && i<14 && j>1 && j<15 && interPanelOnePlayer.chessBoard[i+2][j-2]==0 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==0){
			num++;
		}

		//diagonal													
		if (i>0 && i<14 && interPanelOnePlayer.chessBoard[i-1][j]==0 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 && interPanelOnePlayer.chessBoard[i+2][j]==0){
			num++;
		}
		if (i>1 && i<15 && interPanelOnePlayer.chessBoard[i-2][j]==0 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==0){
			num++;
		}
		//horizontal
		return num;
	}

	public static int  ConnectTwo1 (int i,int j){ //let the computer connect two stones.
		int num=0;
		if (j<15 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i][j+1]==-1){
			num++;
		}
		if (j>0 && interPanelOnePlayer.chessBoard[i][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		//vertical

		if (i<15 && j<15 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j+1]==-1 ){
			num++;
		}

		if (i>0 && j>0 && interPanelOnePlayer.chessBoard[i-1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}              
		//diagonal
		if (i>0 && j<15 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i-1][j+1]==-1 ){
			num++;
		}
		if (j>0 && i<15 && interPanelOnePlayer.chessBoard[i+1][j-1]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}

		//diagonal													
		if (i<15 && interPanelOnePlayer.chessBoard[i][j]==0 && interPanelOnePlayer.chessBoard[i+1][j]==-1 ){
			num++;
		}
		if (i>0 && interPanelOnePlayer.chessBoard[i-1][j]==-1 && interPanelOnePlayer.chessBoard[i][j]==0){
			num++;
		}
		//hotizontal
		return num;
	}

}