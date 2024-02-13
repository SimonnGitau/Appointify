package com.devsimon.watchers.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.devsimon.watchers.R
import com.devsimon.watchers.ui.theme.WatchersTheme
import com.google.android.material.textfield.TextInputLayout

@Composable
fun LoginScreen(navController: NavHostController) {

    WatchersTheme {
        Surface {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(modifier = Modifier
                    .wrapContentSize(),
                    shape = RoundedCornerShape(15.dp),
                    tonalElevation = 7.dp,
                    shadowElevation = 5.dp,
                ) {
                    Column(modifier = Modifier
                        .padding(5.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ){
                        var text by remember { mutableStateOf("") }

                        OutlinedTextField(
                            value = text,
                            onValueChange = { newText ->
                                text = newText.trimStart { it == '0' }
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            label = { Text("Email") }
                        )

                        Spacer(modifier = Modifier.size(10.dp))

                        var password by rememberSaveable { mutableStateOf("") }

                        TextField(
                            value = password,
                            onValueChange = { password = it },
                            label = { Text("Password") },
                            visualTransformation = PasswordVisualTransformation(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            enabled = true
                        )


                        Spacer(modifier = Modifier.size(10.dp))

                        val context:Context = LocalContext.current

                        Button(onClick = {navController.navigate("toDashboard")
                            Toast.makeText(context, text + password, Toast.LENGTH_LONG).show()},
                            border = BorderStroke(2.dp, Color.White),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .height(50.dp),
                        ){
                            Text(text = "Resume",
                                color = Color.White,
                                style = MaterialTheme.typography.titleMedium,
                            )
                        }
                    }

                }
            }
        }
    }

}

@Preview
@Composable
fun LoginScreenPreview() {

    LoginScreen(navController = NavHostController(LocalContext.current))

}