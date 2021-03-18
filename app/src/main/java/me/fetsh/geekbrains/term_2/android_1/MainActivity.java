package me.fetsh.geekbrains.term_2.android_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MainActivity extends AppActivity implements CalculatorActivity {

    private final static String receivedExpression = "calcEXP";
    private final static String savedExpressionKey = "expressionList";
    private final static String savedCalcState = "calcState";

    private Calculator calc;
    private TextView mFormulaTextView;
    private TextView mResultTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        applySavedTheme(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);

        setContentView(R.layout.activity_main);
        calc = new Calculator(this);

        mFormulaTextView = (TextView) findViewById(R.id.formula);
        mResultTextView = (TextView) findViewById(R.id.result);

        // Calculate received expression (as proof of concept, expression validation required)
        // See https://github.com/fetsh-edu/temp_calculator
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null){
            String text  = bundle.getString(receivedExpression);
            String regex = "(?<=op)|(?=op)".replace("op", "[-+*/()]");
            List<String> exp = Pattern.compile(regex)
                    .splitAsStream(text)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            calc.restore(exp, Calculator.State.Input.name());
        }

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
            findViewById(number).setOnClickListener(v -> calc.handleDigit(((Button) v).getText().toString()));
        }

        findViewById(R.id.keyboard_num_dot).setOnClickListener(v -> calc.handleDot());

        View delete_button = findViewById(R.id.keyboard_delete);
        delete_button.setOnClickListener(v -> calc.handleDelete());
        delete_button.setOnLongClickListener(v -> calc.handleClear());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            Intent runSettings = new Intent(MainActivity.this, SettingsActivity.class);
            startActivity(runSettings);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle instanceState) {
        super.onSaveInstanceState(instanceState);
        instanceState.putStringArrayList(
                savedExpressionKey,
                calc.getExpression().collect(Collectors.toCollection(ArrayList::new))
        );
        instanceState.putString(savedCalcState, calc.getState().name());
    }
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle instanceState) {
        super.onRestoreInstanceState(instanceState);
        calc = new Calculator(this);
        calc.restore(
                instanceState.getStringArrayList(savedExpressionKey),
                instanceState.getString(savedCalcState)
        );
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