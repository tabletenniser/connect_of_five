import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.*;
import java.time.*;

@RunWith(Parameterized.class)
public class OnePlayerAITest {
  private int rec_depth = 1;
  private boolean isBlackTurn = true;

  // Constructor with parameters
  public OnePlayerAITest(int rec_depth, boolean isBlackTurn) {
      this.rec_depth = rec_depth;
      this.isBlackTurn = isBlackTurn;
   }

  @Parameterized.Parameters
  public static Collection generateParameters() {
    return Arrays.asList(new Object[][] {
      {6, false},
      // { 1, true },
      // { 2, true },
      // { 3, true },
      // { 4, true },
      // { 1, false },
      // { 2, false },
      // { 3, false },
      // { 4, false },
    });
  }

  public void reverseStones(int[][] gameBoard){
    for (int i = 0; i < gameBoard.length; i++) {
      for (int j = 0; j < gameBoard[i].length; j++) {
        if (gameBoard[i][j] == 1)
          gameBoard[i][j] = -1;
        else if (gameBoard[i][j] == -1)
          gameBoard[i][j] = 1;
      }
    }
  }

  @Test
  @Ignore
  public void testBlockLiveThree(){
    // try
    // {
    //     Thread.sleep(20000);
    // }
    // catch(InterruptedException ex)
    // {
    //     Thread.currentThread().interrupt();
    // }
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    System.out.print(methodName+"_"+rec_depth+"_"+isBlackTurn);
    Instant start = Instant.now();

    OnePlayerAI ai=new OnePlayerAI();
    ai.isTest = true;
    int[][] testingBoard = {
      { 0,  0,  0,  0,  0,  0,  0}, // 0
      { 0,  1,  0,  1,  1,  0,  0}, // 1
      { 0,  0,  0,  0,  0,  0,  0}, // 2
      { 0,  0,  0, -1, -1,  0,  0}, // 3
      { 0,  0,  0, -1,  0,  0,  0}, // 4
      { 0,  0,  0,  0, -1,  0,  0}, // 5
      { 0,  0,  0,  0,  0,  0,  0}, // 6
    };
    if (isBlackTurn)
      reverseStones(testingBoard);
    ai.setBoard(testingBoard);
    assertThat(ai.calculateMove(rec_depth, isBlackTurn),
          isOneOf(100, 102, 105));
    Duration timeElapsed = Duration.between(start, Instant.now());
    System.out.println("...passed!"+timeElapsed.toMillis());
  }
  @Test
  @Ignore
  public void testBlocksOpponentEvenWithLiveThree(){
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    System.out.print(methodName+"_"+rec_depth+"_"+isBlackTurn);
    Instant start = Instant.now();

    OnePlayerAI ai=new OnePlayerAI();
    ai.isTest = true;
    int[][] testingBoard = {
      { 0,  0,  0,  0,  0,  0,  0}, // 0
      { 0,  0,  0,  0,  0,  0,  0}, // 1
      { 0,  0,  0,  0,  0,  0,  0}, // 2
      { 0,  0, -1, -1, -1,  0,  0}, // 3
      { 0,  0,  0,  0,  0,  0,  0}, // 4
      { 0,  0,  0,  0,  0,  0,  0}, // 5
      { 0,  0,  1,  1,  1,  1, -1}, // 6
    };
    if (isBlackTurn)
      reverseStones(testingBoard);
    ai.setBoard(testingBoard);
    assertEquals(601, ai.calculateMove(rec_depth, isBlackTurn));

    Duration timeElapsed = Duration.between(start, Instant.now());
    System.out.println("...passed!"+timeElapsed.toMillis());
  }
  @Test
  public void testBlocksOpponentFromWinning(){
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    System.out.print(methodName+"_"+rec_depth+"_"+isBlackTurn);
    Instant start = Instant.now();

    OnePlayerAI ai=new OnePlayerAI();
    ai.isTest = true;
    int[][] testingBoard = {
      { 0,  0,  0,  0,  0,  0,  0}, // 0
      { 0,  0,  0,  0,  0,  0,  0}, // 1
      { 0,  0,  1,  0,  0,  0,  0}, // 2
      { 0,  0,  0,  0,  0,  0,  0}, // 3
      { 0,  0,  0,  0,  1,  0,  0}, // 4
      { 0,  0,  0,  0,  0,  1,  0}, // 5
      { 0,  0,  0,  0,  0,  0,  1}, // 6
    };
    if (isBlackTurn)
      reverseStones(testingBoard);
    ai.setBoard(testingBoard);
    assertEquals(303, ai.calculateMove(rec_depth, isBlackTurn));

    Duration timeElapsed = Duration.between(start, Instant.now());
    System.out.println("...passed!"+timeElapsed.toMillis());
  }
  @Test
  @Ignore
  public void testPlacesWinningPiece(){
    String methodName = new Object(){}.getClass().getEnclosingMethod().getName();
    System.out.print(methodName+"_"+rec_depth+"_"+isBlackTurn);
    Instant start = Instant.now();

    OnePlayerAI ai=new OnePlayerAI();
    ai.isTest = true;
    int[][] testingBoard = {
      {-1, -1, -1, -1,  0,  0,  0}, // 0
      { 0,  0,  0,  0,  0,  0,  0}, // 1
      { 0,  0,  0,  0,  0,  0,  0}, // 2
      { 0,  0,  0,  0,  0,  0,  0}, // 3
      { 0,  0,  0,  0,  0,  0,  0}, // 4
      { 0,  0,  0,  0,  0,  0,  0}, // 5
      { 0,  0,  0,  0,  0,  0,  0}, // 6
    };
    if (isBlackTurn)
      reverseStones(testingBoard);
    ai.setBoard(testingBoard);
    assertEquals(4, ai.calculateMove(rec_depth, isBlackTurn));

    Duration timeElapsed = Duration.between(start, Instant.now());
    System.out.println("...passed!"+timeElapsed.toMillis());
  }
}
