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
public class OnePlayerAI {
  // declares serial version
  private final long serialVersionUID = 1L;
  // Should be an even number so that we evaluate scenario after opponent moves, to avoid being overly optimistic.
  private final int REC_DEPTH = 2;
  protected boolean isTest = false;
  private int BOARD_SIZE = 16;
  private final int MAX_SCORE=1000000000;

  // Row of thumb: better move needs to be 3x of a less optimal move
  private final int SCORE_OF_FIVE = 95000;
  // four in a row
  private final int SCORE_OF_LIVE_FOUR = 9000;
  private final int SCORE_OF_DEAD_FOUR = 400;
  private final int SCORE_OF_DEAD_FOUR_PLAYER_MOVE = 30000;
  // need to be more than SCORE_OF_LIVE_TWO_PLAYER_MOVE so that we know to block it.
  private final int SCORE_OF_LIVE_THREE = 300;
  private final int SCORE_OF_LIVE_THREE_WITH_SPACE = 200;
  private final int SCORE_OF_LIVE_THREE_PLAYER_MOVE = 1000;
  private final int SCORE_OF_DEAD_THREE = 50;
  private final int SCORE_OF_DEAD_THREE_PLAYER_MOVE = 200;
  // two in a row
  private final int SCORE_OF_LIVE_TWO = 30;
  private final int SCORE_OF_LIVE_TWO_PLAYER_MOVE = 70;
  private final int SCORE_OF_DEAD_TWO = 5;
  private final int SCORE_OF_DEAD_TWO_PLAYER_MOVE = 10;
  private final int SCORE_OF_ADJACENT=5;
  private final int SCORE_RANDOM=2;

  private final int BLOCK_FOUR_SCORE = SCORE_OF_LIVE_FOUR * 3 + 1000;
  private final int BLOCK_LIVE_THREE_SCORE = SCORE_OF_LIVE_THREE * 3 + 50;
  private final int BLOCK_DEAD_THREE_SCORE = SCORE_OF_DEAD_THREE * 3 + 20;
  private final int BLOCK_LIVE_TWO_SCORE = SCORE_OF_LIVE_TWO * 3 + 20;

 //2D array to hold the value of each cell, 1 for black, -1 for white and 0 for none;
  private int chessBoard[][]=new int[16][16];
  Hashtable<String, Integer> scenarioHT = new Hashtable<>();
  Logger logger = Logger.getLogger(OnePlayer.class.getName());

  // constructor of the class
  public OnePlayerAI(){
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
  }

  public void setBoard(int[][] board) {
    chessBoard = board;
    BOARD_SIZE = chessBoard.length;
  }


  /************************* ABS MINIMAX ALGORITHM STARTS *******************************/
  /************************* ABS MINIMAX ALGORITHM STARTS *******************************/
  public boolean positionHasStone(int i, int j, int player_color){
    return i >= 0 && j >= 0 && i < BOARD_SIZE && j < BOARD_SIZE && chessBoard[i][j] == player_color;
  }
  public boolean hasStone(int i, int j){
    return i >= 0 && j >= 0 && i < BOARD_SIZE && j < BOARD_SIZE && chessBoard[i][j] != 0;
  }
  public String getBoardHashCode(){
    ArrayList<Character> arr = new ArrayList<Character>();
    for (int[] row: chessBoard) {
      for (int cell: row) {
        if (cell == 0)
          arr.add('0');
        else if (cell == 1)
          arr.add('a');
        else if (cell == -1)
          arr.add('b');
        else
          assert(false);
      }
    }
    StringBuilder sb = new StringBuilder();
    for (Character ch : arr) {
        sb.append(ch);
    }
    return sb.toString();
  }
  public int evaluateCurSituation(int next_player){
    String boardAsString = getBoardHashCode();
    if (scenarioHT.containsKey(boardAsString)) {
      logger.fine("Cache hit: "+boardAsString+": "+scenarioHT.get(boardAsString));
      return scenarioHT.get(boardAsString);
    }
    AtomicInteger atomicScore = new AtomicInteger(0);
    // Parallel for loop for performance.
    IntStream.range(0, BOARD_SIZE).parallel().forEach(i -> {
      atomicScore.getAndAdd(getDirectionScore(i, 0, 0, 1, next_player)); // vertical
      atomicScore.getAndAdd(getDirectionScore(0, i, 1, 0, next_player));// horizontal
      // down right
      atomicScore.getAndAdd(getDirectionScore(0, i, 1, 1, next_player));
      if (i != 0)
        atomicScore.getAndAdd(getDirectionScore(i, 1, 1, 1, next_player));
      // down left
      atomicScore.getAndAdd(getDirectionScore(i, 0, -1, 1, next_player));
      if (i != 0)
        atomicScore.getAndAdd(getDirectionScore(BOARD_SIZE-1, i, -1, 1, next_player));
    });
    int score = atomicScore.intValue();
    scenarioHT.put(boardAsString, score);
    return score;
  }

  public int getDirectionScore(int i, int j, int x_dir, int y_dir, int player) {
    assert(player!=0);
    ArrayList<Character> arrlist = new ArrayList<Character>();
    for (int m = 0; m <= BOARD_SIZE; m++) {
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
    int res = evaluate1DScore(s)*player;
    return res;
  }
  // TODO: convert this as for loop match and avoid StringBuilder
  public int evaluate1DScore(String s) {
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
    if (s.matches(".*0bbb0.*"))
      return -SCORE_OF_LIVE_THREE;
    if (s.matches(".*0b0bb0.*") || s.matches(".*0bb0b0.*"))
      return -SCORE_OF_LIVE_THREE_WITH_SPACE;
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
  public int calculateMove(int maxDepth, boolean isBlackTurn) {
    return miniMaxRec(maxDepth, isBlackTurn, -MAX_SCORE, MAX_SCORE)[0];
  }
  // Return a tuple of [bestMove, bestScore, branchFactor]
  private int[] miniMaxRec(int depth, boolean isBlackTurn, int alpha, int beta) {
    int bestMove=0;
    int bestScore=isBlackTurn ? -MAX_SCORE : MAX_SCORE;
    int stoneColor = isBlackTurn ? 1 : -1;
    String stoneColorText = isBlackTurn ? "BLACK" : "WHITE";
    if (depth==0) {
      int curScore = evaluateCurSituation(stoneColor);
      logger.fine(stoneColorText+"'s scenario: "+curScore);
      int[] ans = {-1, curScore, 1};
      return ans;
    }
    int curScore=0;
    int branchFactor = 0;
    int totalScenarios = 0;
    // A tuple containing (score, i, j)
    java.util.List<int[]> possibleMoves = new ArrayList<int[]>();
    for (int i=0;i<BOARD_SIZE;i++){
      for (int j=0;j<BOARD_SIZE;j++){
        if (hasStone(i,j) || (!hasStone(i-1, j-1) && !hasStone(i-1, j) && !hasStone(i, j-1) && !hasStone(i-1, j+1) &&
        !hasStone(i+1, j+1) && !hasStone(i+1, j) && !hasStone(i, j+1) && !hasStone(i+1, j-1))){
          continue;
        }
        chessBoard[i][j] = isBlackTurn ? 1 : -1;
        // positive for blackGain; negative for whiteGain;
        int scoreGain = Math.abs(evaluateScoreDelta(i, j));
        possibleMoves.add(new int[]{scoreGain, i, j});
        chessBoard[i][j] = 0;
      }
    }
    Collections.sort(possibleMoves, new Comparator<int[]>() {
      @Override
      public int compare(int[] move1, int[] move2) {
          return move2[0] - move1[0];
      }
    });

    for (int[] move: possibleMoves) {
      int i = move[1];
      int j = move[2];
      chessBoard[i][j] = isBlackTurn ? 1 : -1;
      logger.fine(depth+" - Try placing "+stoneColorText+" at: "+i+","+j);
      int[] recResult=miniMaxRec(depth-1, !isBlackTurn, alpha, beta);
      curScore = recResult[1];
      logger.fine(depth+" - score: "+curScore);
      branchFactor++;
      totalScenarios+=recResult[2];
      if (isBlackTurn && curScore>bestScore || (!isBlackTurn && curScore<bestScore)){      //try to maximize the move
        bestScore=curScore;
        bestMove=100*i+j;
        if (isBlackTurn)
          alpha = curScore;
        else
          beta = curScore;
      }
      chessBoard[i][j]=0;
      // if (isBlackTurn && bestScore>SCORE_OF_LIVE_FOUR || !isBlackTurn && bestScore<-SCORE_OF_LIVE_FOUR || move[0] > 1000) {
      if (Math.abs(move[0]) >= BLOCK_FOUR_SCORE) {
        if (depth == REC_DEPTH && isTest == false)
          System.out.println(stoneColorText+" is making easy move with "+bestMove+" and depth is "+depth+" best score is "+bestScore+"(Branch factor:"+branchFactor+"; Scenarios Evaluated:"+totalScenarios+")");
        int[] ans = {bestMove, bestScore, totalScenarios};
        return ans;
      }
      if (alpha >= beta) {
        break;
      }
    }
    String summaryTxt = stoneColorText+" best move"+bestMove+" and depth is "+depth+" best score is "+bestScore+"(Branch factor:"+branchFactor+"; Scenarios Evaluated:"+totalScenarios+")";
    logger.fine(summaryTxt);
    if (depth == REC_DEPTH && isTest == false)
      System.out.println(summaryTxt);
    int[] ans = {bestMove, bestScore, totalScenarios};
    return ans;
  }

  /************************* ABS MINIMAX ALGORITHM ENDS *******************************/
  /************************* ABS MINIMAX ALGORITHM ENDS *******************************/

  /************************* DELTA MINIMAX ALGORITHM STARTS *******************************/
  /************************* DELTA MINIMAX ALGORITHM STARTS *******************************/
  // Return a tuple of [bestMove, bestScore]
  public int[] miniMaxDeltaRec(int depth, boolean isBlackTurn) {
    int bestMove=0;
    int bestScore=isBlackTurn ? -MAX_SCORE : MAX_SCORE;
    int stoneColor = isBlackTurn ? 1 : -1;
    int curScore=0;
    if (depth==0) {
      int[] ans = {-1, 0};
      return ans;
    }
    for (int i=0;i<BOARD_SIZE;i++){
      for (int j=0;j<BOARD_SIZE;j++){
        if (hasStone(i,j) || (!hasStone(i-1, j-1) && !hasStone(i-1, j) && !hasStone(i, j-1) && !hasStone(i-1, j+1) &&
        !hasStone(i+1, j+1) && !hasStone(i+1, j) && !hasStone(i, j+1) && !hasStone(i+1, j-1))){
          continue;
        }
        chessBoard[i][j]= isBlackTurn ? 1 : -1;
        int scoreGain = evaluateScoreDelta(i, j);
        curScore=miniMaxDeltaRec(depth-1, !isBlackTurn)[1]+scoreGain;
        if (isBlackTurn && curScore>bestScore || (!isBlackTurn && curScore<bestScore)){      //try to maximize the move
          bestScore=curScore;
          bestMove=100*i+j;
        }
        chessBoard[i][j]=0;
      }
    }
    System.out.println(stoneColor+"'s best move"+bestMove+" and depth is "+depth+" best score is "+bestScore);
    int[] ans = {bestMove, bestScore};
    return ans;
  }

  public int evaluateScoreDelta(int i, int j){
    int score=0;
    score+=getDirectionDeltaScore(i,j,0,1);	//right
    score+=getDirectionDeltaScore(i,j,1,0);	//down
    score+=getDirectionDeltaScore(i,j,1,1);	//down right
    score+=getDirectionDeltaScore(i,j,-1,1);	//up right
    //score+=Math.random() * SCORE_RANDOM;
    return score;
  }

  public int getDirectionDeltaScore(int i, int j, int x_dir, int y_dir) {
    int player=chessBoard[i][j];	// Stone placed, can be 1 (black) or -1(white)
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

  public int defenceDeltaScore(String s) {
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

  public int attackDeltaScore(String s) {
    if (s.matches(".*aaaaa.*"))
      return SCORE_OF_FIVE;
    if (s.matches(".*0aaaa0.*"))
      return SCORE_OF_LIVE_FOUR;
    if (s.matches(".*0aaaab.*") || s.matches(".*baaaa0.*") || s.matches(".*a0aaa.*") || s.matches(".*aa0aa.*") || s.matches(".*aaa0a.*"))
      return SCORE_OF_DEAD_FOUR;
    if (s.matches(".*0aaa0.*"))
      return SCORE_OF_LIVE_THREE;
    if (s.matches(".*0a0aa0.*") || s.matches(".*0aa0a0.*"))
      return SCORE_OF_LIVE_THREE_WITH_SPACE;
    if (s.matches(".*00aaa.*") || s.matches(".*aaa00.*") || s.matches(".*0a0aa.*") || s.matches(".*aa0a0.*") || s.matches(".*a0aa0.*") || s.matches(".*0aa0a.*"))
      return SCORE_OF_DEAD_THREE;
    if (s.matches(".*00aa0.*") || s.matches(".*0aa00.*") || s.matches(".*0a0a0.*"))
      return SCORE_OF_LIVE_TWO;
    return 1;
  }

  /************************* DELTA MINIMAX ALGORITHM ENDS *******************************/
  /************************* DELTA MINIMAX ALGORITHM ENDS *******************************/
}
