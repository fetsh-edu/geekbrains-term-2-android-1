package me.fetsh.geekbrains.term_2.android_1;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class CalculatorTest {

    private Calculator calc;
    private TestCalculatorActivity activity;
    private String digit;
    private String dot;

    @Before
    public void setUp() throws Exception {
        activity = new TestCalculatorActivity();
        calc = new Calculator(activity);
        digit = "5";
        dot = ".";
    }

    private void input(String... s) {
        calc.restore(Arrays.asList(s), Calculator.State.Input.name());
    }

    @Test
    public void handleDigitAsMultipleDigitsIgnores() {
        calc.handleDigit("234");
        assertTrue(activity.getFormula().isEmpty());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDigitAsDotIgnores() {
        calc.handleDigit(dot);
        assertTrue(activity.getFormula().isEmpty());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleDigitAsNotDigitIgnores() {
        calc.handleDigit("A");
        assertTrue(activity.getFormula().isEmpty());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleDigitAsFirstSymbol() {
        calc.handleDigit(digit);
        assertEquals(digit, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDotAsFirstSymbolIgnores() {
        calc.handleDot();
        assertTrue(activity.getFormula().isEmpty());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleMinusOperatorAsFirstSymbolAccepts() {
        calc.handleOperator(Operator.MINUS);
        assertEquals(Operator.MINUS.getVisibleSign(), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleNotMinusOperatorAsFirstSymbolIgnores() {
        calc.handleOperator(Operator.PLUS);
        assertTrue(activity.getFormula().isEmpty());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDigitAfterDigitAccepts() {
        input(digit);
        calc.handleDigit(digit);
        assertEquals(digit + digit, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleDotAfterDigitAccepts() {
        input(digit);
        calc.handleDot();
        assertEquals(digit + dot, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleOperatorAfterDigitAccepts() {
        input(digit);
        calc.handleOperator(Operator.PLUS);
        assertEquals(digit + Operator.PLUS.getVisibleSign(), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDotAfterDotIgnores() {
        input(digit + dot);
        calc.handleDot();
        assertEquals(digit + dot, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleDotAfterNumberWithDotIgnores() {
        input(digit + dot + digit);
        calc.handleDot();
        assertEquals(digit + dot + digit, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleDotAfterEngineeringIgnores() {
        input("1.0E12");
        calc.handleDot();
        assertEquals("1.0E12", activity.getFormula());
    }

    @Test
    public void handleDigitAfterDot() {
        input(digit + dot);
        calc.handleDigit(digit);
        assertEquals(digit + dot + digit, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDigitAfterZeroIntegerReplaces() {
        input("0");
        calc.handleDigit(digit);
        assertEquals(digit, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDigitAfterEngineeringReplaces() {
        input("1.0E12");
        calc.handleDigit(digit);
        assertEquals(digit, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDigitAfterZeroDoubleAppends() {
        input("0.0");
        calc.handleDigit(digit);
        assertEquals("0.0" + digit, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDigitAfterMinusOperator() {
        input(Operator.MINUS.getRealSign());
        calc.handleDigit(digit);
        assertEquals(Operator.MINUS.getRealSign() + digit, activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleOperatorAfterOperatorReplaces() {
        input(digit, Operator.MINUS.getRealSign());
        calc.handleOperator(Operator.DIVIDE);
        assertEquals(digit + Operator.DIVIDE.getVisibleSign(), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleMinusOperatorAfterOperatorAccepts() {
        input(digit, Operator.MULTIPLY.getRealSign());
        calc.handleOperator(Operator.MINUS);
        assertEquals(digit + Operator.MULTIPLY.getVisibleSign() + Operator.MINUS.getVisibleSign(), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleOperatorAfterMultipleOperatorsReplacesAll() {
        input(digit, Operator.MULTIPLY.getRealSign(), Operator.MINUS.getRealSign());
        calc.handleOperator(Operator.DIVIDE);
        assertEquals(digit + Operator.DIVIDE.getVisibleSign(), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDotAfterOperatorIgnores() {
        calc.restore(Arrays.asList(digit, Operator.MINUS.getRealSign()), Calculator.State.Input.name());
        calc.handleDot();
        assertEquals(digit + Operator.MINUS.getVisibleSign(), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleOperatorAfterDotAccepts() {
        calc.restore(Arrays.asList(digit + dot), Calculator.State.Input.name());
        calc.handleOperator(Operator.MULTIPLY);
        assertEquals(digit + dot + Operator.MULTIPLY.getVisibleSign(), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void resultShouldBecomeVisibleAfterTwoOperands() {
        input(digit, Operator.PLUS.getRealSign(), digit);
        assertEquals(digit + Operator.PLUS.getVisibleSign() + digit, activity.getFormula());
        assertEquals(Integer.toString(Integer.parseInt(digit) + Integer.parseInt(digit)), activity.getResult());
    }

//    jshell> 13.60 - 12.47
//    ==> 1.129999999999999
    @Test
    public void resultShouldBeAccurateWithFractions() {
        input("13.6", Operator.MINUS.getRealSign(), "12.47");
        assertEquals("1.13", activity.getResult());
    }

    //    jshell> 13.60 - 12.47
    //    ==> 1.129999999999999
    @Test
    public void resultShouldBeAccurateWithFractions2() {
        input("13.60666", Operator.MINUS.getRealSign(), "12.47665");
        assertEquals("1.13001", activity.getResult());
    }

    @Test
    public void resultShouldNotBeEngineeringWhileLessThanTrillion() {
        input("1", Operator.PLUS.getRealSign(), "999,999,999,998");
        assertEquals("999,999,999,999", activity.getResult());
    }
    @Test
    public void resultShouldBeEngineeringSinceBiggerThanTrillion() {
        input("1", Operator.PLUS.getRealSign(), "999,999,999,999");
        assertEquals("1.0E12", activity.getResult());
    }

    @Test
    public void handleDelete1() {
        input( "0", Operator.PLUS.getRealSign(), "-0.05", Operator.PLUS.getRealSign(), "1.0E12");
        assertEquals("9.9999999999995E11", activity.getResult());
        calc.handleDelete();
        assertEquals("0" + Operator.PLUS.getVisibleSign() + "-0.05" + Operator.PLUS.getVisibleSign(), activity.getFormula());
        assertEquals("-0.05", activity.getResult());
    }

    @Test
    public void handleDelete2() {
        input( "0", Operator.PLUS.getRealSign(), "-0.05", Operator.PLUS.getRealSign());
        calc.handleDelete();
        assertEquals("0" + Operator.PLUS.getVisibleSign() + "-0.05", activity.getFormula());
        assertEquals("-0.05", activity.getResult());
    }

    @Test
    public void handleDelete3() {
        input( "0", Operator.PLUS.getRealSign(), "-0.05");
        calc.handleDelete();
        assertEquals("0" + Operator.PLUS.getVisibleSign() + "-0.0", activity.getFormula());
        assertEquals("0", activity.getResult());
    }
    @Test
    public void handleDelete4() {
        input( "0", Operator.PLUS.getRealSign(), "-0.0");
        calc.handleDelete();
        assertEquals("0" + Operator.PLUS.getVisibleSign() + "-0.", activity.getFormula());
        assertEquals("0", activity.getResult());
    }
    @Test
    public void handleDelete5() {
        input( "0", Operator.PLUS.getRealSign(), "-0.");
        calc.handleDelete();
        assertEquals("0" + Operator.PLUS.getVisibleSign() + "-0", activity.getFormula());
        assertEquals("0", activity.getResult());
    }
    @Test
    public void handleDelete6() {
        input( "0", Operator.PLUS.getRealSign(), "-0");
        calc.handleDelete();
        assertEquals("0" + Operator.PLUS.getVisibleSign() + Operator.MINUS.getVisibleSign(), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleDelete7() {
        input( "0", Operator.PLUS.getRealSign(), Operator.MINUS.getRealSign());
        calc.handleDelete();
        assertEquals("0" + Operator.PLUS.getVisibleSign(), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleDelete8() {
        input( "0", Operator.PLUS.getRealSign());
        calc.handleDelete();
        assertEquals("0", activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleDelete9() {
        input( "0");
        calc.handleDelete();
        assertTrue(activity.getFormula().isEmpty());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleDelete10() {
        calc.handleDelete();
        assertTrue(activity.getFormula().isEmpty());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleClear() {
        input(digit, Operator.PLUS.getRealSign(), digit);
        calc.handleClear();
        assertTrue(activity.getFormula().isEmpty());
        assertTrue(activity.getResult().isEmpty());
    }
    @Test
    public void handleClearEmpty() {
        calc.handleClear();
        assertTrue(activity.getFormula().isEmpty());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleZeroDivision() {
        input("1", Operator.PLUS.getRealSign(), "1", Operator.DIVIDE.getRealSign(), "0");
        assertEquals("1" + Operator.PLUS.getVisibleSign() + "1" + Operator.DIVIDE.getVisibleSign() + "0", activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
    }

    @Test
    public void handleEqualWithZeroDivision() {
        input("1", Operator.PLUS.getRealSign(), "1", Operator.DIVIDE.getRealSign(), "0");
        calc.handleEquals();
        assertEquals("1" + Operator.PLUS.getVisibleSign() + "1" + Operator.DIVIDE.getVisibleSign() + "0", activity.getFormula());
        assertEquals("Division by zero", activity.getResult());
        assertEquals(Calculator.State.Result, calc.getState());
    }

    @Test
    public void handleEqual() {
        input(digit, Operator.PLUS.getRealSign(), digit);
        calc.handleEquals();
        assertEquals(Integer.toString(Integer.parseInt(digit) + Integer.parseInt(digit)), activity.getFormula());
        assertTrue(activity.getResult().isEmpty());
        assertEquals(Calculator.State.Result, calc.getState());
    }
}