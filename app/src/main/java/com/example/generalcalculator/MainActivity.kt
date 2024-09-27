package com.example.generalcalculator

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private lateinit var display: TextView
    private lateinit var historyDisplay: TextView

    private var currentNumber: String = ""
    private var firstValue: Double = 0.0
    private var secondValue: Double = 0.0
    private var operator: String = ""
    private var isDecimalUsed = false
    private var lastInputWasOperator = false
    private var isResultDisplayed = false
    private var history: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        display = findViewById(R.id.textView)
        historyDisplay = findViewById(R.id.textHistory)

        setNumberButtonListeners()
        setOperationButtonListeners()
    }

    private fun setNumberButtonListeners() {
        val buttons = listOf<Button>(
            findViewById(R.id.btn_zero),
            findViewById(R.id.btn_one),
            findViewById(R.id.btn_two),
            findViewById(R.id.btn_three),
            findViewById(R.id.btn_four),
            findViewById(R.id.btn_five),
            findViewById(R.id.btn_six),
            findViewById(R.id.btn_seven),
            findViewById(R.id.btn_eight),
            findViewById(R.id.btn_nine),
            findViewById(R.id.btn_dot)
        )

        buttons.forEach { button ->
            button.setOnClickListener {
                if (isResultDisplayed) {
                    currentNumber = ""
                    isResultDisplayed = false
                }

                if (button.text == "." && isDecimalUsed) return@setOnClickListener
                if (button.text == "." && currentNumber.isEmpty()) {
                    currentNumber = "0"
                }

                currentNumber += button.text
                display.text = currentNumber
                isDecimalUsed = currentNumber.contains(".")
                lastInputWasOperator = false
            }
        }
    }

    private fun setOperationButtonListeners() {
        val operators = listOf<Button>(
            findViewById(R.id.btn_adition),
            findViewById(R.id.btn_subtrack),
            findViewById(R.id.btn_multiply),
            findViewById(R.id.btn_divide)
        )

        operators.forEach { button ->
            button.setOnClickListener {
                if (currentNumber.isNotEmpty() || isResultDisplayed) {
                    if (isResultDisplayed) {
                        firstValue = display.text.toString().toDouble()
                        currentNumber = ""
                        isResultDisplayed = false
                    }

                    if (!lastInputWasOperator) {
                        operator = button.text.toString()
                        firstValue = currentNumber.toDoubleOrNull() ?: firstValue
                        currentNumber = ""
                    } else {
                        operator = button.text.toString()
                    }

                    updateHistory("$firstValue $operator")
                    lastInputWasOperator = true
                    isDecimalUsed = false
                }
            }
        }

        // Handle Equal button
        findViewById<Button>(R.id.btn_equal).setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                secondValue = currentNumber.toDouble()
                val result = calculateResult()

                display.text = result
                updateHistory(" $secondValue = $result")
                currentNumber = result

                isResultDisplayed = true
                lastInputWasOperator = false
            }
        }

        // Handle Clear button
        findViewById<Button>(R.id.btn_clear).setOnClickListener {
            clearCalculator()
        }

        // Handle Backspace button
        findViewById<Button>(R.id.btn_backspace).setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                currentNumber = currentNumber.dropLast(1)
                if (!currentNumber.contains(".")) {
                    isDecimalUsed = false
                }
                display.text = if (currentNumber.isEmpty()) "0" else currentNumber
            }
        }

        // Handle Positive/Negative Toggle button (+/-)
        findViewById<Button>(R.id.btn_plus_minus).setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                currentNumber = if (currentNumber.startsWith("-")) {
                    currentNumber.drop(1)
                } else {
                    "-$currentNumber"
                }
                display.text = currentNumber
            }
        }

        // Handle Percentage button (%)
        findViewById<Button>(R.id.btn_percentage).setOnClickListener {
            if (currentNumber.isNotEmpty()) {
                val value = currentNumber.toDouble() / 100
                currentNumber = value.toString()
                display.text = currentNumber
            }
        }
    }

    private fun calculateResult(): String {
        return when (operator) {
            "+" -> (firstValue + secondValue).toString()
            "-" -> (firstValue - secondValue).toString()
            "×" -> (firstValue * secondValue).toString()
            "÷" -> if (secondValue != 0.0) (firstValue / secondValue).toString() else "Error"
            else -> "Error"
        }
    }

    private fun updateHistory(newEntry: String) {
        history += newEntry
        historyDisplay.text = history
    }

    private fun clearCalculator() {
        currentNumber = ""
        firstValue = 0.0
        secondValue = 0.0
        operator = ""
        isDecimalUsed = false
        lastInputWasOperator = false
        isResultDisplayed = false
        display.text = "0"
        history = ""
        historyDisplay.text = ""
    }
}