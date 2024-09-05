package com.example.compose1.screens

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.compose1.dataClasses.TaskData
import com.example.compose1.ui.theme.blue
import com.example.compose1.ui.theme.orangeRed
import com.google.firebase.Firebase
import com.google.firebase.database.database
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ShowHomePage(navController: NavController, userEmail: String) {
    var taskList by remember { mutableStateOf(listOf<TaskData>()) }
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var isEditing by remember { mutableStateOf(false) }
    var currentTaskId by remember { mutableStateOf<Int?>(null) }

    val context = LocalContext.current

//     get saved tasks from firebase database
    LaunchedEffect(Unit) {
        Log.d("ShowHomePage", "LaunchedEffect: Fetching tasks")
        fetchTasks(userEmail, { tasks ->
            taskList = tasks
        }, context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 0.dp)
    ) {
        // Input for new/edit task title
        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Task Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Input for new/edit task description
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Task Description") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Add/Edit task button
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    if (isEditing && currentTaskId != null) {
                        // Edit task
                        taskList = taskList.map { task ->
                            if (task.id == currentTaskId) {
                                task.copy(title = title, description = description)
                            } else task
                        }
                        isEditing = false
                        isEditing = false
                        currentTaskId = null

                        // save online
                        syncDataOnline(userEmail, taskList, context)
                    } else {
                        // Add new task
                        val newTask = TaskData(
                            id = taskList.size + 1,
                            title = title,
                            description = description,
                            completed = false,
                            createdAt = SimpleDateFormat(
                                "dd-MM-yyyy HH:mm:ss", Locale.getDefault()
                            ).format(Date())
                        )
                        taskList = taskList + newTask
                        // save online
                        syncDataOnline(userEmail, taskList, context)
                    }
                    title = ""
                    description = ""
                }
            }, modifier = Modifier.align(Alignment.End),
            colors = ButtonDefaults.buttonColors(
                containerColor = blue,
                contentColor = Color.White
            )
        ) {
            Text(if (isEditing) "Save Changes" else "Add Task")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Display the list of tasks
        LazyColumn {
            items(taskList) { task ->
                TaskItem(task = task, onDelete = {
                    taskList = taskList.filter { it.id != task.id }
                }, onEdit = {
                    title = task.title
                    description = task.description
                    isEditing = true
                    currentTaskId = task.id
                }, onDone = {
                    taskList = taskList.map {
                        if (it.id == task.id)
                            it.copy(completed = !it.completed)
                        else it
                    }
                    // sync data online
                    syncDataOnline(userEmail, taskList, context)
                }
                )
            }
        }
    }
}

@Composable
fun TaskItem(task: TaskData, onDelete: () -> Unit, onEdit: () -> Unit, onDone: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        border = BorderStroke(1.dp, Color.Gray),

        colors = if (task.completed) CardDefaults.cardColors(
            containerColor = Color(0xFFD1ECE4)
        ) else CardDefaults.cardColors(
            containerColor = Color(0xFFFFE4D8)
        ),
        shape = RoundedCornerShape(8.dp)



    ) {
        Column(modifier = Modifier.padding(16.dp, 8.dp)) {
            Text(text = task.title, style = MaterialTheme.typography.headlineSmall)
            Text(text = task.description, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = "Created at: ${task.createdAt}",
                style = MaterialTheme.typography.bodySmall
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
                Button(
                    onClick = {
                        onDone()
                    },
                    colors = ButtonDefaults.buttonColors(
//                        containerColor = if (task.completed) blue else orangeRed
                        containerColor = if (task.completed) Color(0xFF66BB6A) else Color(0xFFEF5350)
                    ), modifier = Modifier.weight(2f)
                ) {
                    if (task.completed) {
                        Text("Completed")
                    } else {
                        Text("Incomplete")
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onEdit, modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = blue
                    )
                ) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = "")
                }

                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    onClick = onDelete,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = orangeRed
                    )
                ) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = "")
                }
            }
        }
    }
}


// sync /save tasks online function
fun syncDataOnline(userEmail: String, taskList: List<TaskData>, context: Context) {
    val database = Firebase.database
    val myRef = database.getReference("users/${userEmail.replace(".", ",")}/tasks")

    myRef.setValue(taskList).addOnSuccessListener {
//        Toast.makeText(context, "Tasks synced successfully", Toast.LENGTH_SHORT).show()
    }.addOnFailureListener { exception ->
        Toast.makeText(
            context, "Failed to sync tasks: ${exception.message}", Toast.LENGTH_SHORT
        ).show()
    }
}


// get tasks from firebase function
fun fetchTasks(userEmail: String, onTasksFetched: (List<TaskData>) -> Unit, context: Context) {
    val database = Firebase.database
    val myRef = database.getReference("users/${userEmail.replace(".", ",")}/tasks")

    myRef.get().addOnSuccessListener { dataSnapshot ->
        try {
            val tasks = dataSnapshot.children.mapNotNull { snapshot ->
                snapshot.getValue(TaskData::class.java)
            }

            if (tasks.isEmpty()) {
                Toast.makeText(context, "No tasks found.", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Tasks fetched successfully", Toast.LENGTH_SHORT).show()
            }
            onTasksFetched(tasks)
        } catch (e: Exception) {
            Toast.makeText(context, "Error parsing tasks: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }.addOnFailureListener { exception ->
        Toast.makeText(context, "Failed to fetch tasks: ${exception.message}", Toast.LENGTH_SHORT)
            .show()
    }
}


@Preview(showBackground = true)
@Composable
private fun Preview_ShowLoginPage() {
    val navController = rememberNavController()
    val userEmail = ""
    ShowHomePage(navController, userEmail)
}
