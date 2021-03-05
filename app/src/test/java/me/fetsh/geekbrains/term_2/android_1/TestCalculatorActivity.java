package me.fetsh.geekbrains.term_2.android_1;

public class TestCalculatorActivity implements CalculatorActivity {

    private String formula = "";
    private String result = "";

    @Override
    public void showFormula(String text) {
        this.formula = text;
    }

    @Override
    public void showResult(String text) {
        this.result = text;
    }

    public String getFormula() {
        return formula;
    }

    public String getResult() {
        return result;
    }
}
