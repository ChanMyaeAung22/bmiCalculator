package com.kmd.bmi_calculator

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.kmd.bmi_calculator.databinding.ActivityMainBinding
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // create calculator object
    private val bmiCalculator = BmiCalculator()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Weight unit dropdown
        val weightUnits = arrayOf("kg", "lb")

        val weightAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            weightUnits
        )

        binding.weightUnitSpinner.setAdapter(weightAdapter)

        // Height unit dropdown
        val heightUnits = arrayOf("cm", "in")

        val heightAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_dropdown_item_1line,
            heightUnits
        )

        binding.heightUnitSpinner.setAdapter(heightAdapter)

        binding.weightUnitSpinner.setOnItemClickListener { _, _, _, _ ->
            binding.weightInputLayout.error = null
        }

        binding.heightUnitSpinner.setOnItemClickListener { _, _, _, _ ->
            binding.heightInputLayout.error = null
        }

        // Calculate button
        binding.calculateButton.setOnClickListener {

            val weight = binding.weightInput.text.toString().trim()
            val height = binding.heightInput.text.toString().trim()

            val weightUnit = binding.weightUnitSpinner.text.toString()
            val heightUnit = binding.heightUnitSpinner.text.toString()

            val weightValid = validateWeight(weight, weightUnit)
            val heightValid = validateHeight(height, heightUnit)

            if (!weightValid || !heightValid) {
                return@setOnClickListener
            }

            val weightValue = weight.toDouble()
            val heightValue = height.toDouble()

            val bmi = bmiCalculator.calculate(
                weightValue,
                weightUnit,
                heightValue,
                heightUnit
            )

            val category = bmiCalculator.getCategory(bmi)

            binding.bmiValueText.text = String.format("%.2f", bmi)

            binding.categoryText.text = when (category) {
                BmiCategory.UNDERWEIGHT -> getString(R.string.category_underweight)
                BmiCategory.NORMAL -> getString(R.string.category_normalweight)
                BmiCategory.OVERWEIGHT -> getString(R.string.category_overwight)
                BmiCategory.OBESE -> getString(R.string.category_obese)
            }

            val categoryColor = when (category) {
                BmiCategory.UNDERWEIGHT ->
                    R.color.bmi_underweight

                BmiCategory.NORMAL ->
                    R.color.bmi_normal

                BmiCategory.OVERWEIGHT ->
                    R.color.bmi_overweight

                BmiCategory.OBESE ->
                    R.color.bmi_obese
            }

            binding.resultCard.setCardBackgroundColor(
                ContextCompat.getColor(this, categoryColor)
            )

            binding.resultCard.visibility = View.VISIBLE
        }
    }

    // Validate weight
    private fun validateWeight(weight: String, unit: String): Boolean {

        val value = weight.toDoubleOrNull()  // "70" → 70.0 or "abc" → null

        if (value == null) {
            binding.weightInputLayout.error =
                getString(R.string.error_invalid_number)
            return false
        }

        val isValid = when (unit) {
            "kg" -> value in 20.0..300.0
            "lb" -> value in 44.0..660.0
            else -> false
        }

        if (!isValid) {
            binding.weightInputLayout.error =
                if (unit == "kg") {
                   getString(R.string.error_weight_kg)
                } else {
                    getString(R.string.error_weight_lb)
                }

            return false
        }

        binding.weightInputLayout.error = null
        return true
    }

    // Validate height
    private fun validateHeight(height: String, unit: String): Boolean {

        val value = height.toDoubleOrNull()

        if (value == null) {
            binding.heightInputLayout.error =
                getString(R.string.error_invalid_number)
            return false
        }

        val isValid = when (unit) {
            "cm" -> value in 50.0..250.0
            "in" -> value in 20.0..100.0
            else -> false
        }

        if (!isValid) {
            binding.heightInputLayout.error =
                if (unit == "cm") {
                    getString(R.string.error_height_cm)
                } else {
                    getString(R.string.error_height_in)
                }

            return false
        }

        binding.heightInputLayout.error = null
        return true
    }
}