
import com.google.gson.Gson;
import org.junit.*;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.junit.Assert.*;
public class CalculatorTest {
    private Calculator calculator;
    private Random ran;

    @Before
    public void setTest(){
        this.calculator = new Calculator();
        this.ran = new Random();
    }


    public void testAdd(int first, int second) throws BadInputException {
        String strFirst = String.valueOf(first);
        String strSec = String.valueOf(second);
        int res = first + second;
        String test = null;
        for (int i = 0; i < strFirst.length(); i++){
            String digit = String.valueOf(strFirst.charAt(i));
            test = this.calculator.calculateNextState(test, digit);
        }
        test = this.calculator.calculateNextState(test, "+");
        for (int i = 0; i < strSec.length(); i++){
            String digit = String.valueOf(strSec.charAt(i));
            test = this.calculator.calculateNextState(test, digit);
        }
        test = this.calculator.calculateNextState(test, "=");
        Map<String, String> jsonMap = new Gson().fromJson(test, Map.class);
        assertEquals("failed to add " + strFirst + "to " + strSec, String.valueOf(res), jsonMap.get("display"));
    }

    @Test
    public void testBasicOperations() throws BadInputException {
        int first = ran.nextInt(1000);
        int second = ran.nextInt(1000);
        int addition = first + second;
        int subtraction = first - second;
        int mul = first * second;
        double divide = (double) first / (double) second;
        testAdd(first, second);


    }

}
