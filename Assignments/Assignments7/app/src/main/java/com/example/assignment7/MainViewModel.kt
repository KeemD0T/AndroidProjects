package com.example.assignment7;
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch



class MainViewModel : ViewModel() {

    var students by mutableStateOf<List<Student>>(emptyList())
        private set // 'private set' means the 'students' list can be read externally but only modified within this ViewModel.

    var isLoading by mutableStateOf(false)
        private set

    var newStudentName by mutableStateOf("")
        private set

    var newStudentGrade by mutableStateOf("")
        private set

    fun addStudent(name: String, grade: String) {
        val gradeValue = grade.toFloatOrNull()
        if (name.isNotBlank() && gradeValue != null) {
            val newStudent = Student(name, gradeValue)
            students = students + newStudent
            updateNewStudentName("")
            updateNewStudentGrade("")
            newStudentName = ""
            newStudentGrade = ""
        }
    }
    // TODO: Implement the addStudent function
    // Convert grade to Float, create a Student object, add it to the students list,
    // and clear the input fields (newStudentName and newStudentGrade).


    fun removeStudent(student: Student) {
        val newStudentsList = students.toMutableList()
        newStudentsList.remove(student)
        students = newStudentsList

        // TODO: Implement the removeStudent function
        // It should take a 'Student' object as a parameter and remove it from the students list.
    }

    fun calculateGPA(): Float {
        if (students.isEmpty()) {
            return 0f
        }
        val totalOfGrades = students.sumOf { it.grade.toDouble() }.toFloat()

        return totalOfGrades / students.size
    }


    // TODO: Implement the calculateGPA function
    // It should return a Float, calculate the average grade of all students, and return 0f if the list is empty.
    //  return 0f // Placeholder


    fun loadSampleData() {
        viewModelScope.launch {
            isLoading = true
            delay(1500)
            students = listOf(
                    Student("Alice Johnson", 95f),
                    Student("Bob Smith", 87f),
                    Student("Carol Davis", 92f)
            )
            isLoading = false

        }
    }
        // TODO: Implement the loadSampleData function using coroutines (viewModelScope.launch)
        // Set isLoading to true, simulate a delay (e.g., 1500ms), populate students with sample data,
        //The sample data will be students = listOf(
        //    Student("Alice Johnson", 95f),
        //    Student("Bob Smith", 87f),
        //    Student("Carol Davis", 92f)
        //)
        // and set isLoading back to false.


    fun updateNewStudentName(name: String) {
        newStudentName = name

        // TODO: Implement the updateNewStudentName function
        // It should take a 'name' (String) as a parameter and update newStudentName.
    }

    fun updateNewStudentGrade(grade: String) {
        newStudentGrade = grade

        // TODO: Implement the updateNewStudentGrade function
        // It should take a 'grade' (String) as a parameter and update newStudentGrade.
    }



}
