package com.kmd.bmi_calculator

class BmiCalculator {

    fun calculate(
        weight: Double,
        weightUnit: String,
        height: Double,
        heightUnit: String
    ): Double {

        val weightInKg = when (weightUnit) { // weightUnit = kg or lb
            "kg" -> weight
            "lb" -> weight * 0.453592
            else -> throw IllegalArgumentException("Invalid weight unit")
        }

        val heightInMeters = when (heightUnit) { // heightUnit = cm or in
            "cm" -> height / 100
            "in" -> height * 0.0254
            else -> throw IllegalArgumentException("Invalid height unit")
        }

        return weightInKg / (heightInMeters * heightInMeters)
    }

    fun getCategory(bmi: Double): BmiCategory {

        return when {
            bmi < 18.5 -> BmiCategory.UNDERWEIGHT
            bmi < 25.0 -> BmiCategory.NORMAL
            bmi < 30.0 -> BmiCategory.OVERWEIGHT
            else -> BmiCategory.OBESE
        }
    }
}