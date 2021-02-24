
import com.google.gson.Gson;
import org.junit.*;

import java.util.*;

import static org.junit.Assert.*;
public class CalculatorTest {
    public static final String ZERO = "0";
    private Calculator calculator;
    private Random ran;

    @Before
    public void setTest(){
        this.calculator = new Calculator();
        this.ran = new Random();
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


        String intAdd = firstInt + Calculator.PLUS + secondInt + Calculator.EQ;
        String intAddRes = Calculator.stringOf(firstInt + secondInt);
        String intSub = firstInt + Calculator.MINUS + secondInt + Calculator.EQ;
        String intSubRes = Calculator.stringOf(firstInt - secondInt);
        String intMul = firstInt + Calculator.MUL + secondInt + Calculator.EQ;
        String intMulRes = Calculator.stringOf(firstInt * secondInt);
        String intDiv = firstInt + Calculator.DIV + secondInt + Calculator.EQ;
        String intDivRes = Calculator.stringOf((double) firstInt / (double) secondInt);

        String negIntAdd = -firstInt + Calculator.PLUS + secondInt + Calculator.EQ;
        String negIntAddRes = Calculator.stringOf(-firstInt + secondInt);
        String negIntSub = -firstInt + Calculator.MINUS + secondInt + Calculator.EQ;
        String negIntSubRes = Calculator.stringOf(-firstInt - secondInt);
        String negIntMul = -firstInt + Calculator.MUL + secondInt + Calculator.EQ;
        String negIntMulRes = Calculator.stringOf(-firstInt * secondInt);
        String negIntDiv = -firstInt + Calculator.DIV + secondInt + Calculator.EQ;
        String negIntDivRes = Calculator.stringOf(((double) -firstInt) / (double) secondInt);

        String doubleAdd = firstDouble + Calculator.PLUS + secondDouble + Calculator.EQ;
        String doubleAddRes = Calculator.stringOf(firstDouble + secondDouble);
        String doubleSub = firstDouble + Calculator.MINUS + secondDouble + Calculator.EQ;
        String doubleSubRes = Calculator.stringOf(firstDouble - secondDouble);
        String doubleMul = firstDouble + Calculator.MUL + secondDouble + Calculator.EQ;
        String doubleMulRes = Calculator.stringOf(firstDouble * secondDouble);
        String doubleDiv = firstDouble + Calculator.DIV + secondDouble + Calculator.EQ;
        String doubleDivRes = Calculator.stringOf(firstDouble / secondDouble);

        String negDoubleAdd = -firstDouble + Calculator.PLUS + secondDouble + Calculator.EQ;
        String negDoubleAddRes = Calculator.stringOf(-firstDouble + secondDouble);
        String negDoubleSub = -firstDouble + Calculator.MINUS + secondDouble + Calculator.EQ;
        String negDoubleSubRes = Calculator.stringOf(-firstDouble - secondDouble);
        String negDoubleMul = -firstDouble + Calculator.MUL + secondDouble + Calculator.EQ;
        String negDoubleMulRes = Calculator.stringOf(-firstDouble * secondDouble);
        String negDoubleDiv = -firstDouble + Calculator.DIV + secondDouble + Calculator.EQ;
        String negDoubleDivRes = Calculator.stringOf((-firstDouble) / secondDouble);

        ///// test on positive integers /////
        testString(intAdd, intAddRes);
        testString(intSub, intSubRes);
        testString(intMul, intMulRes);
        testString(intDiv, intDivRes);

        ///// test on negative integers /////
        testString(negIntAdd, negIntAddRes);
        testString(negIntSub, negIntSubRes);
        testString(negIntMul, negIntMulRes);
        testString(negIntDiv, negIntDivRes);

        ///// test on positive doubles /////
        testString(doubleAdd, doubleAddRes);
        testString(doubleSub, doubleSubRes);
        testString(doubleMul, doubleMulRes);
        testString(doubleDiv, doubleDivRes);

        ///// test on negative doubles /////
        testString(negDoubleAdd, negDoubleAddRes);
        testString(negDoubleSub, negDoubleSubRes);
        testString(negDoubleMul, negDoubleMulRes);
        testString(negDoubleDiv, negDoubleDivRes);
    }

    @Test(expected = BadInputException.class)
    public void testDivideByZero() throws BadInputException {
        int firstInt = ran.nextInt(1000);
        double firstDouble = ran.nextDouble() + firstInt;
        String firstTest = firstInt + Calculator.DIV + ZERO + Calculator.EQ;
        String secondTest = firstDouble + Calculator.DIV + ZERO + Calculator.EQ;
        testString(firstTest, "Should throw exception");
        testString(secondTest, "Should Throw exception");
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
