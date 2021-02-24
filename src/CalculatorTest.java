
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
            assertEquals("failed to operate: " + strFirst + operation + strSec, jsonMap.get("display"), Calculator.stringOf(divideRes));
            return;
        }
        assertEquals("failed to operate: " + strFirst + operation + strSec, String.valueOf(res), jsonMap.get("display"));
    }

    public void testOperationDouble(double first, double second, String operation)throws BadInputException{
        String strFirst = String.valueOf(first);
        String strSec = String.valueOf(second);
        double res;
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
                res  = first / second;

        }

        String test = null;
        for (int i = 0; i < strFirst.length(); i++){
            String token = String.valueOf(strFirst.charAt(i));
            test = this.calculator.calculateNextState(test, token);
        }
        test = this.calculator.calculateNextState(test, operation);
        for (int i = 0; i < strSec.length(); i++){
            String token = String.valueOf(strSec.charAt(i));
            test = this.calculator.calculateNextState(test, token);
        }
        test = this.calculator.calculateNextState(test, "=");
        Map<String, String> jsonMap = new Gson().fromJson(test, Map.class);
        assertEquals("failed to operate: " + strFirst + operation + strSec, jsonMap.get("display"), Calculator.stringOf(res));
    }

    public void testString(String str, String result) throws BadInputException {
        String test = null;
        for (int i = 0; i < str.length(); i++) {
            String token = String.valueOf(str.charAt(i));
            test = this.calculator.calculateNextState(test, token);
        }
        Map<String, String> jsonMap = new Gson().fromJson(test, Map.class);
        assertEquals("failed to operate: " + str, result, jsonMap.get("display"));
    }

    @Test
    public void testBasicOperations() throws BadInputException {
        int firstInt = ran.nextInt(1000);
        int secondInt = ran.nextInt(1000);
        double firstDouble = ran.nextDouble() + firstInt;
        double secondDouble = ran.nextDouble() + secondInt;

        ///// test on positive integers /////
        testOperationInt(firstInt, secondInt, "+");
        testOperationInt(firstInt, secondInt, "*");
        testOperationInt(firstInt, secondInt, "-");
        testOperationInt(firstInt, secondInt, "/");

        ///// test on negative integers /////
        testOperationInt(-firstInt, secondInt, "+");
        testOperationInt(-firstInt, secondInt, "*");
        testOperationInt(-firstInt, secondInt, "-");
        testOperationInt(-firstInt, secondInt, "/");

        ///// test on positive doubles /////
        testOperationDouble(firstDouble, secondDouble, "+");
        testOperationDouble(firstDouble, secondDouble, "*");
        testOperationDouble(firstDouble, secondDouble, "-");
        testOperationDouble(firstDouble, secondDouble, "/");

        ///// test on negative doubles /////
        testOperationDouble(-firstDouble, secondDouble, "*");
        testOperationDouble(-firstDouble, secondDouble, "+");
        testOperationDouble(-firstDouble, secondDouble, "-");
        testOperationDouble(-firstDouble, secondDouble, "/");
    }

    @Test(expected = BadInputException.class)
    public void testDivideByZero() throws BadInputException {
        int firstInt = ran.nextInt(1000);
        double firstDouble = ran.nextDouble() + firstInt;
        testOperationInt(firstInt, 0, "/");
        testOperationDouble(firstDouble, 0, "/");
        testOperationDouble(firstDouble, 0.0, "/");
    }
    @Test
    public void testMultipleOperatorsInARow() throws BadInputException {
        String firstTest = "2++9=";
        String firstRes = "11";
        String secondTest = "152+-1*/2+*9-+2=";
        String secondRes = "681.5";
        String thirdTest = "157.65+*-/15.38*-/+-*598.358+*-*-/*-+*25.32+-*/*-*+/569.23*-+-/-768=";
        String thirdRes = "-495.1809688428374";
        testString(firstTest, firstRes);
        testString(secondTest, secondRes);
        testString(thirdTest, thirdRes);
    }

    @Test
    public void testLongOperations() throws BadInputException {
        String firstTest = "20+5*9+80-19/17*23+15-25+8/6/9=";
        String firstRes = "7.128540305010893";
        String SecondTest = "200+689.35*978.57-9.01/49.32*974.56+4581.236*75/487.23=";
        String SecondRes = "2647820.550969823";
        testString(firstTest, firstRes);
        testString(SecondTest, SecondRes);


    }

}
