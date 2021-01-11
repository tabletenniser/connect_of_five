import javax.swing.*;
import java.util.ArrayList;
import java.lang.Math;

import java.awt.*;
import java.awt.event.*;
import java.io.File;

// this class enables the user to play against the computer
public class OnePlayer extends JFrame implements MouseListener{
  // declares serial version
  private static final long serialVersionUID = 1L;
  private static final int MAX_SCORE=1000000000;
  private static final int SCORE_OF_FIVE = 10000;
  // four in a row
  private static final int SCORE_OF_LIVE_FOUR = 2000;
  private static final int SCORE_OF_DEAD_FOUR = 400;
  private static final int SCORE_OF_DEAD_FOUR_PLAYER_MOVE = 1000;
  // three in a row
  private static final int SCORE_OF_LIVE_THREE = 200;
  private static final int SCORE_OF_LIVE_THREE_PLAYER_MOVE = 500;
  private static final int SCORE_OF_DEAD_THREE = 50;
  private static final int SCORE_OF_DEAD_THREE_PLAYER_MOVE = 100;
  // two in a row
  private static final int SCORE_OF_LIVE_TWO = 50;
  private static final int SCORE_OF_LIVE_TWO_PLAYER_MOVE = 100;
  private static final int SCORE_OF_DEAD_TWO = 5;
  private static final int SCORE_OF_DEAD_TWO_PLAYER_MOVE = 10;
  private static final int SCORE_OF_ADJACENT=5;
  private static final int SCORE_RANDOM=3;

  private static final int BLOCK_FOUR_SCORE = 3000;
  private static final int BLOCK_LIVE_THREE_SCORE = 500;
  private static final int BLOCK_DEAD_THREE_SCORE = 50;
  private static final int BLOCK_LIVE_TWO_SCORE = 100;

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


  /************************* ABS MINIMAX ALGORITHM STARTS *******************************/
  /************************* ABS MINIMAX ALGORITHM STARTS *******************************/
  public static boolean positionHasStone(int i, int j, int player_color){
    return i >= 0 && j >= 0 && i < 16 && j < 16 && interPanelOnePlayer.chessBoard[i][j] == player_color;
  }
  public static boolean hasStone(int i, int j){
    return i >= 0 && j >= 0 && i < 16 && j < 16 && interPanelOnePlayer.chessBoard[i][j] != 0;
  }
  public static int evaluateCurSituation(int next_player){
    int score=0;
    for (int i = 0; i < 16; i++) {
      score+=getDirectionScore(i, 0, 0, 1, next_player); // vertical
      score+=getDirectionScore(0, i, 1, 0, next_player);// horizontal
      // down right
      score+=getDirectionScore(0, i, 1, 1, next_player);
      if (i != 0)
        score+=getDirectionScore(i, 1, 1, 1, next_player);
      // down left
      score+=getDirectionScore(i, 0, -1, 1, next_player);
      if (i != 0)
        score+=getDirectionScore(15, i, -1, 1, next_player);
    }
    return score;
  }

  public static int getDirectionScore(int i, int j, int x_dir, int y_dir, int player) {
    assert(player!=0);
    ArrayList<Character> arrlist = new ArrayList<Character>();
    for (int m = 0; m <= 16; m++) {
      if (positionHasStone(i+m*x_dir, j+m*y_dir, player))
        arrlist.add('a');
      else if (positionHasStone(i+m*x_dir, j+m*y_dir, -player))
        arrlist.add('b');
      else if (positionHasStone(i+m*x_dir, j+m*y_dir, 0))
        arrlist.add('0');
      else
        break;
    }
    StringBuilder sb = new StringBuilder();
    for (Character ch : arrlist) {
        sb.append(ch);
    }
    String s = sb.toString();
    // if (s.contains("a") || s.contains("b"))
    //   System.out.println("String:"+s);
    int res = evaluate1DScore(s)*player;
    // if (res != 0)
    //   System.out.println("Interesting pattern found for "+player+"; "+res+"at: "+i+","+j+" - "+x_dir+","+y_dir+":"+s);
    return res;
  }
  public static int evaluate1DScore(String s) {
    if (s.length() < 5)
      return 0;
    if (s.matches(".*aaaaa.*"))
      return SCORE_OF_FIVE;
    if (s.matches(".*bbbbb.*"))
      return -SCORE_OF_FIVE;
    if (s.matches(".*0aaaa0.*"))
      return SCORE_OF_LIVE_FOUR;
    if (s.matches(".*0bbbb0.*"))
      return -SCORE_OF_LIVE_FOUR;
    if (s.matches(".*0aaaab.*") || s.matches(".*baaaa0.*") || s.matches(".*a0aaa.*") || s.matches(".*aa0aa.*") || s.matches(".*aaa0a.*"))
      return SCORE_OF_DEAD_FOUR_PLAYER_MOVE;
    if (s.matches(".*0bbbba.*") || s.matches(".*abbbb0.*") || s.matches(".*b0bbb.*") || s.matches(".*bb0bb.*") || s.matches(".*bbb0b.*"))
      return -SCORE_OF_DEAD_FOUR;
    if (s.matches(".*0aaa0.*") || s.matches(".*0a0aa0.*") || s.matches(".*0aa0a0.*"))
      return SCORE_OF_LIVE_THREE_PLAYER_MOVE;
    if (s.matches(".*0bbb0.*") || s.matches(".*0b0bb0.*") || s.matches(".*0bb0b0.*"))
      return -SCORE_OF_LIVE_THREE;
    if (s.matches(".*00aaa.*") || s.matches(".*aaa00.*") || s.matches(".*0a0aa.*") || s.matches(".*aa0a0.*") || s.matches(".*a0aa0.*") || s.matches(".*0aa0a.*"))
      return SCORE_OF_DEAD_THREE_PLAYER_MOVE;
    if (s.matches(".*00bbb.*") || s.matches(".*bbb00.*") || s.matches(".*0b0bb.*") || s.matches(".*bb0b0.*") || s.matches(".*b0bb0.*") || s.matches(".*0bb0b.*"))
      return -SCORE_OF_DEAD_THREE;
    if (s.matches(".*00aa0.*") || s.matches(".*0aa00.*") || s.matches(".*0a0a0.*"))
      return SCORE_OF_LIVE_TWO_PLAYER_MOVE;
    if (s.matches(".*00bb0.*") || s.matches(".*0bb00.*") || s.matches(".*0b0b0.*"))
      return -SCORE_OF_LIVE_TWO;
    return 0;
  }
  // Return a tuple of [bestMove, bestScore]
  public static int[] miniMaxRec(int depth, boolean isBlackTurn, int alpha, int beta) {
    int bestMove=0;
    int bestScore=isBlackTurn ? -MAX_SCORE : MAX_SCORE;
    int stoneColor = isBlackTurn ? 1 : -1;
    int curScore=0;
    if (depth==0) {
      int[] ans = {-1, evaluateCurSituation(stoneColor), 1};
      return ans;
    }
    int branchFactor = 0;
    int totalScenarios = 0;
    for (int i=0;i<16;i++){
      for (int j=0;j<16;j++){
        if (hasStone(i,j) || (!hasStone(i-1, j-1) && !hasStone(i-1, j) && !hasStone(i, j-1) && !hasStone(i-1, j+1) &&
        !hasStone(i+1, j+1) && !hasStone(i+1, j) && !hasStone(i, j+1) && !hasStone(i+1, j-1))){
          continue;
        }
        interPanelOnePlayer.chessBoard[i][j]= isBlackTurn ? 1 : -1;
        int[] recResult=miniMaxRec(depth-1, !isBlackTurn, alpha, beta);
        curScore = recResult[1];
        branchFactor++;
        totalScenarios+=recResult[2];
        if (isBlackTurn && curScore>bestScore || (!isBlackTurn && curScore<bestScore)){      //try to maximize the move
          // System.out.println("Better move found for "+stoneColor+"; Prev Move: "+bestMove+"New move: "+i+","+j+"; Prev Score: "+bestScore+" New score:"+curScore);
          bestScore=curScore;
          bestMove=100*i+j;
          if (isBlackTurn)
            alpha = curScore;
          else
            beta = curScore;
        }
        interPanelOnePlayer.chessBoard[i][j]=0;
        if (isBlackTurn && bestScore>SCORE_OF_LIVE_FOUR || !isBlackTurn && bestScore<-SCORE_OF_LIVE_FOUR) {
          System.out.println(stoneColor+"'s is WINNING with move"+bestMove+" and depth is "+depth+" best score is "+bestScore+"(Branch factor:"+branchFactor+"; Scenarios Evaluated:"+totalScenarios+")");
          int[] ans = {bestMove, bestScore, totalScenarios};
          return ans;
        }
        if (alpha >= beta) {
          // System.out.println("Pruning occurred at: "+branchFactor);
          break;
        }
      }
    }
    System.out.println(stoneColor+"'s best move"+bestMove+" and depth is "+depth+" best score is "+bestScore+"(Branch factor:"+branchFactor+"; Scenarios Evaluated:"+totalScenarios+")");
    int[] ans = {bestMove, bestScore, totalScenarios};
    return ans;
  }

  public static int calculateNEW(){
    return miniMaxRec(2, false, -MAX_SCORE, MAX_SCORE)[0];
  }
  /************************* ABS MINIMAX ALGORITHM ENDS *******************************/
  /************************* ABS MINIMAX ALGORITHM ENDS *******************************/

  /************************* DELTA MINIMAX ALGORITHM STARTS *******************************/
  /************************* DELTA MINIMAX ALGORITHM STARTS *******************************/
  // Return a tuple of [bestMove, bestScore]
  public static int[] miniMaxDeltaRec(int depth, boolean isBlackTurn) {
    int bestMove=0;
    int bestScore=isBlackTurn ? -MAX_SCORE : MAX_SCORE;
    int stoneColor = isBlackTurn ? 1 : -1;
    int curScore=0;
    if (depth==0) {
      int[] ans = {-1, 0};
      return ans;
    }
    for (int i=0;i<16;i++){
      for (int j=0;j<16;j++){
        if (hasStone(i,j) || (!hasStone(i-1, j-1) && !hasStone(i-1, j) && !hasStone(i, j-1) && !hasStone(i-1, j+1) &&
        !hasStone(i+1, j+1) && !hasStone(i+1, j) && !hasStone(i, j+1) && !hasStone(i+1, j-1))){
          continue;
        }
        interPanelOnePlayer.chessBoard[i][j]= isBlackTurn ? 1 : -1;
        int scoreGain = evaluateScoreDelta(i, j);
        curScore=miniMaxDeltaRec(depth-1, !isBlackTurn)[1]+scoreGain;
        if (isBlackTurn && curScore>bestScore || (!isBlackTurn && curScore<bestScore)){      //try to maximize the move
          System.out.println("Better move found for "+stoneColor+"; Prev Move: "+bestMove+"New move: "+i+","+j+"; Prev Score: "+bestScore+" New score:"+curScore);
          bestScore=curScore;
          bestMove=100*i+j;
        }
        interPanelOnePlayer.chessBoard[i][j]=0;
        // if (isBlackTurn && bestScore>SCORE_OF_LIVE_FOUR || !isBlackTurn && bestScore<-SCORE_OF_LIVE_FOUR) {
        //   System.out.println(stoneColor+" is winnning with move "+bestMove+" best score is "+bestScore);
        //   int[] ans = {bestMove, bestScore};
        //   return ans;
        // }
      }
    }
    System.out.println(stoneColor+"'s best move"+bestMove+" and depth is "+depth+" best score is "+bestScore);
    int[] ans = {bestMove, bestScore};
    return ans;
  }

  public static int calculateNEWDelta(){
    return miniMaxDeltaRec(2, false)[0];
  }

  public static int evaluateScoreDelta(int i, int j){
    int score=0;
    score+=getDirectionDeltaScore(i,j,0,1);	//right
    score+=getDirectionDeltaScore(i,j,1,0);	//down
    score+=getDirectionDeltaScore(i,j,1,1);	//down right
    score+=getDirectionDeltaScore(i,j,-1,1);	//up right
    //score+=Math.random() * SCORE_RANDOM;
    return score;
  }

  public static int getDirectionDeltaScore(int i, int j, int x_dir, int y_dir) {
    int player=interPanelOnePlayer.chessBoard[i][j];	// Stone placed, can be 1 (black) or -1(white)
    assert(player!=0);
    char[] arr = new char[9];
    for (int m = -4; m <= 4; m++) {
      if (positionHasStone(i+m*x_dir, j+m*y_dir, player))
        arr[4+m] = 'a';
      else if (positionHasStone(i+m*x_dir, j+m*y_dir, -player))
        arr[4+m] = 'b';
      else if (positionHasStone(i+m*x_dir, j+m*y_dir, 0))
        arr[4+m] = '0';
      else
        arr[4+m] = 'W';
    }
    String s = new String(arr);
    return (attackDeltaScore(s.replace('W', 'b')) + defenceDeltaScore(s.replace('W', 'a')))*player;
  }

  public static int defenceDeltaScore(String s) {
    if (s.matches(".*abbbb.*") || s.matches(".*bbbba.*"))
      return BLOCK_FOUR_SCORE;
    if (s.matches(".*abbb0.*") || s.matches(".*0bbba.*") || s.matches(".*bbab0.*") || s.matches(".*0bbab.*") || s.matches(".*babb0.*") || s.matches(".*0babb.*"))
      return BLOCK_LIVE_THREE_SCORE;
    if (s.matches(".*abbba.*"))
      return BLOCK_DEAD_THREE_SCORE;
    if (s.matches(".*abb00.*") || s.matches(".*00bba.*") || s.matches(".*0bab0.*"))
      return BLOCK_LIVE_TWO_SCORE;
    return 0;
  }

  public static int attackDeltaScore(String s) {
    if (s.matches(".*aaaaa.*"))
      return SCORE_OF_FIVE;
    if (s.matches(".*0aaaa0.*"))
      return SCORE_OF_LIVE_FOUR;
    if (s.matches(".*0aaaab.*") || s.matches(".*baaaa0.*") || s.matches(".*a0aaa.*") || s.matches(".*aa0aa.*") || s.matches(".*aaa0a.*"))
      return SCORE_OF_DEAD_FOUR;
    if (s.matches(".*0aaa0.*") || s.matches(".*0a0aa0.*") || s.matches(".*0aa0a0.*"))
      return SCORE_OF_LIVE_THREE;
    if (s.matches(".*00aaa.*") || s.matches(".*aaa00.*") || s.matches(".*0a0aa.*") || s.matches(".*aa0a0.*") || s.matches(".*a0aa0.*") || s.matches(".*0aa0a.*"))
      return SCORE_OF_DEAD_THREE;
    if (s.matches(".*00aa0.*") || s.matches(".*0aa00.*") || s.matches(".*0a0a0.*"))
      return SCORE_OF_LIVE_TWO;
    return 1;
  }

  /************************* DELTA MINIMAX ALGORITHM ENDS *******************************/
  /************************* DELTA MINIMAX ALGORITHM ENDS *******************************/

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
