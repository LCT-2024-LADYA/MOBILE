package ru.gozerov.presentation.shared.utils


fun isValidEmail(email: String): Boolean {
    val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    return email.matches(emailRegex.toRegex())
}

fun isValidPassword(password: String): Boolean {
    val passwordRegex =
        """^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#$%^&*()_+{}\[\]:;<>,.?/~`-]).{8,64}$"""

    return password.matches(passwordRegex.toRegex())
}

fun isValidAge(value: String): Boolean {
    return try {
        val age = value.toInt()
        age in 14..150
    } catch (e: NumberFormatException) {
        false
    }
}

fun isValidInt(value: String): Boolean {
    return try {
        value.toInt()
        return true
    } catch (e: NumberFormatException) {
        false
    }
}