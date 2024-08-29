package com.example.compose1.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose1.R
import com.example.compose1.dataClasses.UserData
import com.example.compose1.ui.theme.purple
import com.example.compose1.ui.theme.white
import com.google.firebase.Firebase
import com.google.firebase.database.database

//@Preview(showBackground = true)
@Composable
fun ShowRegistrationPage(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        Image(
            painter = painterResource(id = R.drawable.register_bg),
            contentDescription = "Image",
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .height(200.dp),
            alignment = Alignment.Center
        )
        Text(
            text = "Create Account",
            color = purple,
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)

        )

        var userEmail by remember { mutableStateOf(TextFieldValue("")) }
        var userPassword by remember { mutableStateOf(TextFieldValue("")) }
        var passwordConfirmation by remember { mutableStateOf(TextFieldValue("")) }
        val context = LocalContext.current

        // email text field
        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
            value = userEmail,
            onValueChange = {
                userEmail = it
            },
            label = { Text("Email") },
            placeholder = { Text(text = "") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email, contentDescription = null
                )
            })

        // password text field
        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 0.dp, 15.dp, 15.dp),
            value = userPassword,
            onValueChange = { userPassword = it },
            label = { Text(text = "Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock, contentDescription = null
                )
            })

        // confirm password text field
        OutlinedTextField(modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp, 0.dp, 15.dp, 0.dp),
            value = passwordConfirmation,
            onValueChange = { passwordConfirmation = it },
            label = { Text(text = "Confirm Password") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Password
            ),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock, contentDescription = null
                )
            })

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have account? ")
            Text(text = "Login here", color = Color.Blue, modifier = Modifier.clickable {
//                Toast.makeText(context, "Going to login page", Toast.LENGTH_SHORT).show()
                navController.navigate("login")
            }

            )
        }

        // register button
        Button(
            onClick = {
                registerUser(
                    email = userEmail.text,
                    password = userPassword.text,
                    passwordConfirmation = passwordConfirmation.text,
                    navController,
                    context
                )


            }, colors = ButtonDefaults.buttonColors(
                containerColor = purple, contentColor = white
            ), modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Text(text = "Register")
        }


    }
}


fun registerUser(
    email: String,
    password: String,
    passwordConfirmation: String,
    navController: NavController,
    context: Context
) {

    // database starts here
    val database = Firebase.database
    val myRef = database.getReference("users")
    // using data class to store email & password
    val userDetails = UserData(email, password)

    // check if user already exists or not
    myRef.child(email.replace(".", ",")).get().addOnSuccessListener {
        Toast.makeText(context, "User Already Exists", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener {

        // check user input is valid or not
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(context, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        } else if (password != passwordConfirmation) {
            Toast.makeText(context, "Passwords do not match", Toast.LENGTH_SHORT).show()
        } else if (password.length < 6) {
            Toast.makeText(
                context,
                "Password must be at least 6 characters long",
                Toast.LENGTH_SHORT
            ).show()
        } else if (!email.contains("@")) {
            Toast.makeText(
                context,
                "Please enter a valid email address",
                Toast.LENGTH_SHORT
            ).show()
        } else if (password.contains(" ")) {
            Toast.makeText(context, "Password cannot contain spaces", Toast.LENGTH_SHORT)
                .show()
        } else {

            myRef.child(email.replace(".", ",")).setValue(userDetails)
                .addOnSuccessListener {
                    Toast.makeText(context, "Successfully Registered", Toast.LENGTH_SHORT)
                        .show()
                    // navigate to login page on successful registration
                    navController.navigate("login")
                    // show error on failure
                }.addOnFailureListener { error ->
                    Toast.makeText(
                        context, "Registration Failed ${error.message}", Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview_ShowRegistrationPage() {
    val navController = rememberNavController()
    ShowRegistrationPage(navController)
}