package com.firmansyah.malangcamp.component

import android.util.Patterns
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.firmansyah.malangcamp.R

@Composable
fun emailInput(
    email: String,
    onEmailValueChange: (newValue: String) -> Unit
): Boolean {
    val isEmpty = email.isEmpty()
    val isValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
    val isError = !isValid

    TextField(
        value = email,
        onValueChange = { newValue ->
            onEmailValueChange(
                newValue
            )
        },
        trailingIcon = {
            if (isEmpty) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "error",
                    tint = MaterialTheme.colors.error
                )
            } else if (!isValid) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "error",
                    tint = MaterialTheme.colors.error
                )
            }
        },
        isError = !isValid,
        placeholder = { Text(text = stringResource(id = R.string.email)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 32.dp)
    )
    if (isEmpty) {
        Text(
            text = "Email harus diisi",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    } else if (!isValid) {
        Text(
            text = "Email tidak valid",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    }
    return isError
}

@Composable
fun passwordInput(password: String, onPasswordValueChange: (newValue: String) -> Unit): Boolean {
    var isNotEnough = password.isEmpty() || password.length < 6
    val isError = isNotEnough

    TextField(
        value = password,
        onValueChange = { newValue ->
            onPasswordValueChange(newValue)
            isNotEnough = false
        },
        trailingIcon = {
            if (isNotEnough) {
                Icon(
                    Icons.Filled.Warning,
                    contentDescription = "error",
                    tint = MaterialTheme.colors.error
                )
            }
        },
        isError = isNotEnough,
        placeholder = { Text(text = stringResource(id = R.string.password)) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, top = 8.dp),
        visualTransformation = PasswordVisualTransformation()
    )
    if (isNotEnough) {
        Text(
            text = "Password harus lebih dari 6 karakter",
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp)
        )
    }
    return isError
}