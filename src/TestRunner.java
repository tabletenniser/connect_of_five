import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;

// Set up required:
// 1) Download junit-4.13.1.jar and hamcrest-all-1.3.jar
// 2) Add them to $CLASSPATH environment variable
// 3) Run javac *.java && java TestRunner
public class TestRunner {
   public static void main(String[] args) {
      Result result = JUnitCore.runClasses(OnePlayerAITest.class);

      for (Failure failure : result.getFailures()) {
         System.out.println(failure.toString());
      }

      System.out.println(result.wasSuccessful());
   }
}
