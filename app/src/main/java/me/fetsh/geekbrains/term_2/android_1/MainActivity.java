package me.fetsh.geekbrains.term_2.android_1;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity implements CalculatorActivity {

    private Calculator calc;
    private TextView mFormulaTextView;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calc = new Calculator(this);

        mFormulaTextView = (TextView) findViewById(R.id.formula);
        mResultTextView = (TextView) findViewById(R.id.result);

        findViewById(R.id.keyboard_plus).setOnClickListener(v -> calc.handleOperator(Operator.PLUS));
        findViewById(R.id.keyboard_minus).setOnClickListener(v -> calc.handleOperator(Operator.MINUS));
        findViewById(R.id.keyboard_multiply).setOnClickListener(v -> calc.handleOperator(Operator.MULTIPLY));
        findViewById(R.id.keyboard_divide).setOnClickListener(v -> calc.handleOperator(Operator.DIVIDE));
        findViewById(R.id.keyboard_equal).setOnClickListener(v -> calc.handleEquals());

        for (int number : Arrays.asList(
                R.id.keyboard_num0,
                R.id.keyboard_num1, R.id.keyboard_num2, R.id.keyboard_num3,
                R.id.keyboard_num4, R.id.keyboard_num5, R.id.keyboard_num6,
                R.id.keyboard_num7, R.id.keyboard_num8, R.id.keyboard_num9)) {
            findViewById(number).setOnClickListener(v -> calc.handleNumber(number));
        }

        findViewById(R.id.keyboard_num_dot).setOnClickListener(v -> calc.handleDot());

        View delete_button = findViewById(R.id.keyboard_delete);
        delete_button.setOnClickListener(v -> calc.handleDelete());
        delete_button.setOnLongClickListener(v -> calc.handleClear());

    }

    @Override
    public void showFormula(String text) {
        mFormulaTextView.setText(text);
    }

    @Override
    public void showResult(String result) {
        mResultTextView.setText(result);
    }
}