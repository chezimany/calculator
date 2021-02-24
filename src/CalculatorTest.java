
import com.google.gson.Gson;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;
public class CalculatorTest {
    private Calculator calculator;
    private Random ran;

    @Before
    public void setTest(){
        this.calculator = new Calculator();
        this.ran = new Random();
    }


    public void testAddInt(int first, int second) throws BadInputException {
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

    public void testSubInt(int first, int second)throws BadInputException{
        String strFirst = String.valueOf(first);
        String strSec = String.valueOf(second);
        int res = first - second;
        String test = null;
        for (int i = 0; i < strFirst.length(); i++){
            String digit = String.valueOf(strFirst.charAt(i));
            test = this.calculator.calculateNextState(test, digit);
        }
        test = this.calculator.calculateNextState(test, "-");
        for (int i = 0; i < strSec.length(); i++){
            String digit = String.valueOf(strSec.charAt(i));
            test = this.calculator.calculateNextState(test, digit);
        }
        test = this.calculator.calculateNextState(test, "=");
        Map<String, String> jsonMap = new Gson().fromJson(test, Map.class);
        assertEquals("failed to sub " + strFirst + "to " + strSec, String.valueOf(res), jsonMap.get("display"));
    }

    public void testOperationInt(int first, int second, String operation)throws BadInputException{
        String strFirst = String.valueOf(first);
        String strSec = String.valueOf(second);
        int res;
        double divideRes = 0;
        switch (operation){
            case "+":
                res = first + second;
                break;
            case "-":
                res = first - second;
                break;
            case "*":
                res = first * second;
                break;
            default:
                divideRes  = (double) first / (double) second;
                res = 0;

        }

        String test = null;
        for (int i = 0; i < strFirst.length(); i++){
            String digit = String.valueOf(strFirst.charAt(i));
            test = this.calculator.calculateNextState(test, digit);
        }
        test = this.calculator.calculateNextState(test, operation);
        for (int i = 0; i < strSec.length(); i++){
            String digit = String.valueOf(strSec.charAt(i));
            test = this.calculator.calculateNextState(test, digit);
        }
        test = this.calculator.calculateNextState(test, "=");
        Map<String, String> jsonMap = new Gson().fromJson(test, Map.class);
        if (operation.equals("/")){
            assertEquals("failed to operate: " + strFirst + operation + strSec, String.valueOf(divideRes), jsonMap.get("display"));
            return;
        }
        assertEquals("failed to operate: " + strFirst + operation + strSec, String.valueOf(res), jsonMap.get("display"));
    }

    @Test
    public void testBasicOperations() throws BadInputException {
        int first = ran.nextInt(1000);
        int second = ran.nextInt(1000);

        ///// test on positive integers /////
        testOperationInt(first, second, "+");
        testOperationInt(first, second, "*");
        testOperationInt(first, second, "-");
        testOperationInt(first, second, "/");

        ///// test on negative integers /////
        testOperationInt(-first, second, "+");
        testOperationInt(first, -second, "+");
        testOperationInt(first, -second, "*");
        testOperationInt(-first, second, "*");
        testOperationInt(-first, second, "-");
        testOperationInt(first, -second, "-");
        testOperationInt(first, -second, "/");
        testOperationInt(-first, second, "/");

    }

}
