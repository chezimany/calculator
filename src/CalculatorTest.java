
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

    public String testString(String str, String result, String test) throws BadInputException {
        for (int i = 0; i < str.length(); i++) {
            String token = String.valueOf(str.charAt(i));
            test = this.calculator.calculateNextState(test, token);
        }
        Map<String, String> jsonMap = new Gson().fromJson(test, Map.class);
        assertEquals("failed to operate: " + str, result, jsonMap.get("display"));
        return test;
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
        testString(intAdd, intAddRes, null);
        testString(intSub, intSubRes, null);
        testString(intMul, intMulRes, null);
        testString(intDiv, intDivRes, null);

        ///// test on negative integers /////
        testString(negIntAdd, negIntAddRes, null);
        testString(negIntSub, negIntSubRes, null);
        testString(negIntMul, negIntMulRes, null);
        testString(negIntDiv, negIntDivRes, null);

        ///// test on positive doubles /////
        testString(doubleAdd, doubleAddRes, null);
        testString(doubleSub, doubleSubRes, null);
        testString(doubleMul, doubleMulRes, null);
        testString(doubleDiv, doubleDivRes, null);

        ///// test on negative doubles /////
        testString(negDoubleAdd, negDoubleAddRes, null);
        testString(negDoubleSub, negDoubleSubRes, null);
        testString(negDoubleMul, negDoubleMulRes, null);
        testString(negDoubleDiv, negDoubleDivRes, null);
    }

    @Test(expected = BadInputException.class)
    public void testDivideByZero() throws BadInputException {
        int firstInt = ran.nextInt(1000);
        double firstDouble = ran.nextDouble() + firstInt;
        String firstTest = firstInt + Calculator.DIV + ZERO + Calculator.EQ;
        String secondTest = firstDouble + Calculator.DIV + ZERO + Calculator.EQ;
        testString(firstTest, "Should throw exception", null);
        testString(secondTest, "Should Throw exception", null);
    }
    @Test
    public void testMultipleOperatorsInARow() throws BadInputException {
        String firstTest = "2++9=";
        String firstRes = "11";
        String secondTest = "152+-1*/2+*9-+2=";
        String secondRes = "681.5";
        String thirdTest = "157.65+*-/15.38*-/+-*598.358+*-*-/*-+*25.32+-*/*-*+/569.23*-+-/-768=";
        String thirdRes = "-495.1809688428374";
        testString(firstTest, firstRes, null);
        testString(secondTest, secondRes, null);
        testString(thirdTest, thirdRes, null);
    }

    @Test
    public void testLongOperations() throws BadInputException {
        String firstTest = "20+5*9+80-19/17*23+15-25+8/6/9=";
        String firstRes = "7.128540305010893";
        String SecondTest = "200+689.35*978.57-9.01/49.32*974.56+4581.236*75/487.23=";
        String SecondRes = "2647820.550969823";
        testString(firstTest, firstRes, null);
        testString(SecondTest, SecondRes, null);
    }

    @Test
    public void testContinuousOperations() throws BadInputException {
        String test = null;
        String first = "15*54/22+68-93/3*15.47/10.56-9.345+87.60-45.23*2.23=";
        String firstRes = "86.51522027089071";
        String second = "15*98+56-48/52*28/7-6+89*4=";
        String secondRes = "786.7692307692307";
        String third = "+52+68-25*7=";
        String thirdRes = "6172.384615384615";
        String fourth = "*92.35+56-9=";
        String fourthRes = "570066.7192307692";
        test = testString(first, firstRes, test);
        test = testString(second, secondRes, test);
        test = testString(third, thirdRes, test);
        testString(fourth, fourthRes, test);
    }

}
