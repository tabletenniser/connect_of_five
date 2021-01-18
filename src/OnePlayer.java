import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.lang.Math;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import javax.swing.*;
import java.util.logging.*;

// this class enables the user to play against the computer
public class OnePlayer extends JFrame implements MouseListener{
  // declares serial version
  private static final long serialVersionUID = 1L;
  // Should be an even number so that we evaluate scenario after opponent moves, to avoid being overly optimistic.
  private static final int REC_DEPTH = 2;
  private static final int BOARD_SIZE = 16;
  private static final int MAX_SCORE=1000000000;

  // a static string used to store user's name
  static String playerOne;
  static Hashtable<String, Integer> scenarioHT = new Hashtable<>();
  static Logger logger = Logger.getLogger(OnePlayer.class.getName()); 
  static OnePlayerAI ai=new OnePlayerAI();

  //define those JComponents
  ImageIcon icon=new ImageIcon("CHESSBOARD.jpg");
  JLabel label2=new JLabel(icon,SwingConstants.CENTER);
  JLabel JLTitle=new JLabel("Connect of Five",SwingConstants.CENTER);
  JLabel label3=new JLabel();
  JLabel label4=new JLabel();

  // constructor of the class
  public OnePlayer(String title){
    // java.util.concurrent.ForkJoinPool.common.parallelism=8;
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
    ai.setBoard(interPanelOnePlayer.chessBoard);
  }

  public static int calculateNEW(){
    Date prevTimestamp = new Date();
    int move = ai.miniMaxRec(REC_DEPTH, false, -MAX_SCORE, MAX_SCORE)[0];
    Date newTimestamp = new Date();
    long difference = newTimestamp.getTime() - prevTimestamp.getTime();
    System.out.println("It takes "+difference+"ms to make a move.");
    return move;
  }

  public static int calculateNEWDelta(){
    return ai.miniMaxDeltaRec(2, false)[0];
  }

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
    for (int i=0;i<BOARD_SIZE;i++){
      for (int j=0;j<BOARD_SIZE;j++){
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
    for (int i=0;i<BOARD_SIZE;i++){
      for (int j=0;j<BOARD_SIZE;j++){
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
    logger.setLevel(Level.FINE);
    System.setProperty("java.util.logging.SimpleFormatter.format", "[%1$tM:%1$tS:%1$tL] [%4$-6s] %5$s %n");
    // Log to file.
    try {
      FileHandler fh = new FileHandler("ai.log");
      logger.addHandler(fh);
      logger.setUseParentHandlers(false);
      SimpleFormatter formatter = new SimpleFormatter();
      fh.setFormatter(formatter);
    } catch (IOException e) {  
        e.printStackTrace();  
    } 

    logger.info("Game is starting!");
    new OnePlayer("Connect of Five");
  }


  /************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
  /************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
  /************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
  /************************* BELOW IS THE OLD NON-RECURSIVE WAY TO FIND THE NEXT STEP *******************************/
  // this method helps the computer calculate the points that correspond to each positon so that the computer can choose its best move
  public static int Calculate(){

    // initialize the points of each position as 0
    int[][] score = new int [BOARD_SIZE][BOARD_SIZE];
    for (int i=0;i<BOARD_SIZE;i++){
      for (int j=0;j<BOARD_SIZE;j++){
        score[i][j]=0;
      }
    }

    //variables to store the best move
    int bestScore=0;
    int bestMove=0;

    // go over each available place to find the best move
    for (int i=0;i<BOARD_SIZE;i++){
      for (int j=0;j<BOARD_SIZE;j++){
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
          if(i==0 || j==0 || i==BOARD_SIZE-1 || j==BOARD_SIZE-1){
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
    for(int i=0;i<BOARD_SIZE;i++){
    for(int j=0;j<BOARD_SIZE;j++){
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
