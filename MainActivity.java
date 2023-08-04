package com.example.calculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView display;
    private StringBuilder currentInput;
    private StringBuilder completeExpression;
    private double firstNumber;
    private String operator;
    private boolean isNewCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        display = findViewById(R.id.display_area);
        currentInput = new StringBuilder();
        completeExpression = new StringBuilder();
        operator = "";

        isNewCalculation = true;

        setButtonListeners();

    }

    private void setButtonListeners() {
        int[] numberButtonIds = {
                R.id.button_0, R.id.button_1, R.id.button_2, R.id.button_3,
                R.id.button_4, R.id.button_5, R.id.button_6, R.id.button_7,
                R.id.button_8, R.id.button_9, R.id.button_decimal
        };

        for (int numberButtonId : numberButtonIds) {
           findViewById(numberButtonId).setOnClickListener(this);
        }

        int[] operatorButtonIds = {
                R.id.button_plus, R.id.button_minus, R.id.button_multiply, R.id.button_divide
        };

        for (int operatorButtonId : operatorButtonIds) {
            findViewById(operatorButtonId).setOnClickListener(this);
        }

        findViewById(R.id.button_equals).setOnClickListener(this);
        findViewById(R.id.button_clear).setOnClickListener(this);
        findViewById(R.id.button_backspace).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        String buttonValue = ((Button) view).getText().toString();

        if (view.getId() == R.id.button_clear) {
            clearDisplay();
        } else if (view.getId() == R.id.button_backspace) {
            handleBackspace();
        } else if (view.getId() == R.id.button_equals) {
            calculateResult();
        } else if (view.getId() == R.id.button_plus ||
                view.getId() == R.id.button_minus ||
                view.getId() == R.id.button_multiply ||
                view.getId() == R.id.button_divide) {
            handleOperatorButtonClick(buttonValue);
        } else {
            appendToInput(buttonValue);
        }
    }

    private void handleBackspace() {
        // Check if there is something to remove
        if (completeExpression.length() > 0) {
            // Remove the last character from completeExpression
            completeExpression.deleteCharAt(completeExpression.length() - 1);

            // Check if the last character was a number or operator to update currentInput accordingly
            if (!currentInput.toString().isEmpty()) {
                char lastChar = currentInput.charAt(currentInput.length() - 1);
                if (!Character.isDigit(lastChar) && lastChar != '.') {
                    operator = "";
                }
                currentInput.deleteCharAt(currentInput.length() - 1);
            }

            // Update the display with the updated expression
            updateDisplay();
        }
    }


    private void updateDisplay() {
        display.setText(completeExpression.toString());
    }

    private void clearDisplay() {
        currentInput.setLength(0);
        completeExpression.setLength(0);
        display.setText("0");
        operator = "";
        isNewCalculation = true;
    }

    private void appendToInput(String value) {
        // Check if the user is entering a new number after calculation
        if (isNewCalculation) {
            currentInput.setLength(0);
            isNewCalculation = false;
        }
        currentInput.append(value);
        completeExpression.append(value);
        display.setText(completeExpression.toString());
    }

    private void handleOperatorButtonClick(String value) {
        if (!currentInput.toString().isEmpty()) {
            firstNumber = Double.parseDouble(currentInput.toString());
            operator = value;
            completeExpression.append(" ").append(operator).append(" ");
            isNewCalculation = true;
        }
    }

    private void calculateResult() {
        if (!currentInput.toString().isEmpty() && !operator.isEmpty()) {
            double secondNumber = Double.parseDouble(currentInput.toString());
            double result = 0;

            switch (operator) {
                case "+":
                    result = firstNumber + secondNumber;
                    break;
                case "-":
                    result = firstNumber - secondNumber;
                    break;
                case "*":
                    result = firstNumber * secondNumber;
                    break;
                case "/":
                    if (secondNumber != 0) {
                        result = firstNumber / secondNumber;
                    } else {
                        display.setText("Error: Cannot divide by zero");
                        return;
                    }
                    break;
            }

            // Display the result
            completeExpression.append("=").append(formatResult(result));
            display.setText(completeExpression.toString());
            isNewCalculation = true;
        }
    }

    private String formatResult(double result) {
        // Display the result with up to 8 decimal places and remove trailing zeros
        return String.format("%.8f", result).replaceAll("0*$", "").replaceAll("\\.$", "");
    }
}
