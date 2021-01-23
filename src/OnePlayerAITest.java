import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import java.util.*;

@RunWith(Parameterized.class)
public class OnePlayerAITest {
  private int REC_DEPTH = 1;

  // Constructor with parameters
  public OnePlayerAITest(int rec_depth) {
      this.REC_DEPTH = rec_depth;
   }

  @Parameterized.Parameters
  public static Collection generateParameters() {
    return Arrays.asList(new Object[][] {
      { 1 },
      { 2 },
      { 3 },
      { 4 },
    });
  }

  @Test
  public void testWhiteBlocksBlackLiveThree(){
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
    ai.setBoard(testingBoard);
    assertThat(ai.calculateMove(REC_DEPTH, false),
          isOneOf(100, 102, 105));
  }
  @Test
  public void testWhiteBlocksBlackEvenWithLiveFour(){
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
    ai.setBoard(testingBoard);
    assertEquals(601, ai.calculateMove(REC_DEPTH, false));
  }
  @Test
  public void testWhiteBlocksBlackFromWinning(){
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
    ai.setBoard(testingBoard);
    assertEquals(303, ai.calculateMove(REC_DEPTH, false));
  }
  @Test
  public void testWhitePlacesWinningPiece(){
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
    ai.setBoard(testingBoard);
    assertEquals(4, ai.calculateMove(REC_DEPTH, false));
  }
}
