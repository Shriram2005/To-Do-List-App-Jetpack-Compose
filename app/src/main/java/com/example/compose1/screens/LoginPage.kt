package com.example.compose1.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose1.R
import com.example.compose1.ui.theme.purple
import com.example.compose1.ui.theme.white
import com.google.firebase.Firebase
import com.google.firebase.database.database


//@Preview(showBackground = true)
@Composable
fun ShowLoginPage(navController: NavController) {

    val context = LocalContext.current
    var showloader by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
            .verticalScroll(rememberScrollState()),

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
            text = "Login",
            color = purple,
            fontSize = 25.sp,
            fontWeight = FontWeight.ExtraBold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)

        )


        var userEmail by remember { mutableStateOf(TextFieldValue("")) }
        var userPassword by remember { mutableStateOf(TextFieldValue("")) }
        val userPassword2 by remember { mutableStateOf(TextFieldValue("")) }


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            value = userEmail,
            label = { Text("Email") },
            placeholder = { Text(text = "") },
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Email
            ),
            onValueChange = { newValue ->
                userEmail = newValue
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = null
                )
            }
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp, 0.dp, 15.dp, 0.dp),
            value = userPassword,
            onValueChange = { a -> userPassword = a },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = null
                )
            }
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            horizontalArrangement = Arrangement.Center

        ) {
            Text(
                text = "Don't have Account? "
            )
            Text(
                text = "Register here",
                color = Color.Blue,
                modifier = Modifier
                    .clickable {
//                         Going to register page

                        navController.navigate("register")
                    }
            )
        }


        Button(
            onClick = {
                showloader = true
                // login function
                loginUser(userEmail.text, userPassword.text, navController, context)
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = purple,
                contentColor = white
            ),
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(top = 16.dp)
        ) {
            Text(text = "Login")
        }
    }

    // show or hide loader
    if (showloader) {
        Box(
            modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
        ) {
            // show loader
            CircularProgressIndicator()
            // loading complete after 2 seconds
            androidx.compose.runtime.LaunchedEffect(Unit) {
                kotlinx.coroutines.delay(2000)
                showloader = false
            }
        }
    }

}

fun loginUser(
    userEmail: String,
    userPassword: String,
    navController: NavController,
    context: Context
) {
    // database starts here
    val database = Firebase.database
    val myRef = database.getReference("users")

    myRef.child(userEmail.replace(".", ",")).get().addOnSuccessListener {
        if (it.exists()) {
            val pass = it.child("password")
            if (pass.value == userPassword) {
                Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
//                navController.navigate("home")
                navController.navigate("home/$userEmail")
            } else {
                Toast.makeText(context, "invalid password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview_ShowLoginPage() {
val navController = rememberNavController()
    ShowLoginPage(navController)
}