package com.example.assignment7

// Core Android imports
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent

// Compose UI imports
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

// ViewModel imports
import androidx.lifecycle.viewmodel.compose.viewModel

// TODO: Import MainViewModel from the same package once created

import com.example.assignment7.MainViewModel




/**
 * MainActivity is the entry point of the application.
 * It sets up the basic Compose UI.
 */
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set up the Compose UI
        setContent {
            MaterialTheme {
                Surface {
                    StudentGradeManager()
                    // TODO: Call the StudentGradeManager composable here
                }
            }
        }
    }
}

// TODO: Define the Student data class
// It should have 'name' (String) and 'grade' (Int) properties.
  data class Student(
    val name: String,
    val grade: Float
)



@Composable
fun StudentGradeManager(
    viewModel: MainViewModel = viewModel()
    // TODO: Instantiate MainViewModel using viewModel()
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 50.dp)
    ) {
        item {
            Text(
                text = "Student Grade Manager",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        // Header
        // TODO: Add a Text composable for the header "Student Grade Manager"
        // Wrap this composable in an 'item {}' block.
        // Use style MaterialTheme.typography.headlineMedium and Modifier.padding(bottom = 16.dp)
        item {
            GPADisplay(
                gpa = viewModel.calculateGPA()
            )
        }


        // TODO: Call GPADisplay composable, passing the GPA from the ViewModel
        // Wrap this composable in an 'item {}' block.
        item {
            AddStudentForm(
                name = viewModel.newStudentName,
                grade = viewModel.newStudentGrade,
                // Add the missing parameters
                onNameChange = viewModel::updateNewStudentName,
                onGradeChange = viewModel::updateNewStudentGrade,
                onAddStudent = viewModel::addStudent
            )
        }


        // TODO: Call AddStudentForm composable, passing state and event handlers from the ViewModel
        // Wrap this composable in an 'item {}' block.
        item {
            Button(
                onClick = viewModel::loadSampleData,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                Text("Load Sample Data")
            }
        }

        // TODO: Create a Button to "Load Sample Data" that calls viewModel.loadSampleData()
        // Wrap this composable in an 'item {}' block.
        // Use Modifier.padding(vertical = 8.dp)
        item {
            StudentsList(
                students = viewModel.students,
                onRemoveStudent = viewModel::removeStudent
            )
        }

        // TODO: Call StudentsList composable, passing the list of students and the remove student handler from the ViewModel
        // Wrap this composable in an 'item {}' block.
        item {
            if (viewModel.isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.padding(16.dp)
                )
            }
        }
        item {
            if(viewModel.isLoading)
                CircularProgressIndicator(
                modifier = Modifier.padding(16.dp)

            )


        }
        // TODO: Conditionally show a CircularProgressIndicator if viewModel.isLoading is true
        // Wrap this composable in an 'item {}' block.
        // Use Modifier.padding(16.dp)

    }
}

@Composable
fun GPADisplay(gpa: Float) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Class GPA",
                style = MaterialTheme.typography.titleMedium,
            )
            // TODO: Add a Text composable for "Class GPA"
            // Use style MaterialTheme.typography.titleMedium

            Text(
                text = "%.2f".format(gpa),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )
            // TODO: Add a Text composable to display the formatted GPA (e.g., "%.2f".format(gpa))
            // Use style MaterialTheme.typography.headlineLarge and color MaterialTheme.colorScheme.primary
        }
    }
}

@Composable
fun AddStudentForm(
    name: String,
    grade: String,
    onNameChange: (String) -> Unit,
    onGradeChange: (String) -> Unit,
    onAddStudent: (name: String, grade: String)-> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Add New Student",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // TODO: Add a Text composable for "Add New Student"
            // Use style MaterialTheme.typography.titleMedium and Modifier.padding(bottom = 8.dp)
    OutlinedTextField(
        value = name,
        onValueChange = onNameChange,
        label = { Text("Student Name") },
        modifier = Modifier.fillMaxWidth()
    )

            // TODO: Create an OutlinedTextField for student name input
            // Bind value to 'name', onValueChange to 'onNameChange', and set label to "Student Name"
            // Use Modifier.fillMaxWidth()
            OutlinedTextField(
                value = grade,
                onValueChange = onGradeChange,
                label = { Text("Grade (0-100)") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: Create an OutlinedTextField for student grade input
            // Bind value to 'grade', onValueChange to 'onGradeChange', and set label to "Grade (0-100)"
            // Set keyboardOptions to KeyboardOptions(keyboardType = KeyboardType.Number)
            // Use Modifier.fillMaxWidth()
    Button(
        onClick = { onAddStudent(name, grade) },
        enabled = name.isNotBlank() && grade.isNotBlank(),
        modifier = Modifier.fillMaxWidth()

    ) {
        Text("Add Student")

    }

            Spacer(modifier = Modifier.height(8.dp))

            // TODO: Create a Button to "Add Student"
            // Set onClick to 'onAddStudent' and enabled state based on 'name.isNotBlank() && grade.isNotBlank()'
            // Use Modifier.fillMaxWidth()
        }
    }
}

@Composable
fun StudentsList(
    students: List<Student>, // TODO: Change Any to Student after defining Student data class
    onRemoveStudent: (Student) -> Unit // TODO: Change Any to Student after defining Student data class
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .heightIn(max = 300.dp) // Limit height to prevent overflow
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Students (${students.size})",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            // TODO: Add a Text composable for "Students (${students.size})"
            // Use style MaterialTheme.typography.titleMedium and Modifier.padding(bottom = 8.dp)


            if (students.isEmpty()) {
                Text(
                    text = "No students added yet",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                // TODO: Add a Text composable for "No students added yet" if the list is empty
                // Use style MaterialTheme.typography.bodyMedium and color MaterialTheme.colorScheme.onSurfaceVariant

            }
            else {
                // Iterate through the list of students directly inside the Column.
                // This is efficient for small-to-medium lists inside a non-scrollable parent.
                students.forEach { student ->
                    StudentRow(
                        student = student,
                        onRemove = { onRemoveStudent(student) }
                    )
                    Divider(modifier = Modifier.padding(vertical = 4.dp))
                }
            }





            // TODO: Create a LazyColumn to display the list of students
                // Set modifier to Modifier.heightIn(max = 200.dp) and verticalArrangement to Arrangement.spacedBy(4.dp)
                // Inside items, call StudentRow for each student and a Divider if it's not the last student

            }
        }
    }


@Composable
fun StudentRow(
    student: Student, // TODO: Change Any to Student after defining Student data class
    onRemove: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {

            Text(
                text = student.name,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            // TODO: Add a Text composable to display student.name
            // Use style MaterialTheme.typography.bodyLarge
        Text(
        text = "Grade: ${student.grade}",
        style = MaterialTheme.typography.bodyMedium
        )
            // TODO: Add a Text composable to display "Grade: ${student.grade}"
            // Use style MaterialTheme.typography.bodyMedium and color MaterialTheme.colorScheme.onSurfaceVariant

        }
        IconButton(
            onClick = onRemove,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = "Remove Student"
            )

        }
        // TODO: Create an IconButton with Icons.Default.Delete for removing a student
        // Set onClick to 'onRemove'

    }
}

/**
 * Preview function for the StudentGradeManager screen
 */

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun StudentGradeManagerPreview() {
    MaterialTheme {
        StudentGradeManager()
        // TODO: Call StudentGradeManager here for preview
    }
}
