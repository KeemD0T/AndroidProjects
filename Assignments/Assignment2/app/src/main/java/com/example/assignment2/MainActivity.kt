package com.example.assignment2

import androidx.compose.ui.tooling.preview.Preview
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.ExperimentalLayoutApi

lateinit var selectedButtons: MutableState<Set<Int>>

/**
 * This project covers concepts from Chapter 5 lessons:
 * - "Flow Rows and Columns" - for creating flexible grid layouts
 * - "Adding Space" - for spacing and arrangement in layouts
 * - "Adding Modifiers" - for layout modifiers and styling
 * - "Custom Modifiers" - for creating reusable modifier patterns
 * - "Click Events" - for handling user interactions
 *
 * Students should review these lessons before starting:
 * - Flow Rows and Columns lesson for grid layout implementation
 * - Adding Space lesson for spacing and arrangement
 * - Adding Modifiers lesson for layout modifiers
 * - Custom Modifiers lesson for modifier patterns
 * - Click Events lesson for user interaction handling
 */

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                InteractiveButtonGrid()
                // TODO: Call the main composable function here
                // Hint: You need to call InteractiveButtonGrid()
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun InteractiveButtonGrid() {
    var selectedButtons by remember { mutableStateOf(setOf<Int>()) }
    // TODO: Create a state variable to track which buttons are selected
    // Hint: Use remember and mutableStateOf with a SetOf<Int> type
    // The variable should be called selectedButtons
    // See "Adding Modifiers" lesson for examples of state management

    // List of button data (color, number) - already provided
    val buttonData = listOf(
        ButtonData(Color(0xFFE57373), "1"), // Red
        ButtonData(Color(0xFF81C784), "2"), // Green
        ButtonData(Color(0xFF64B5F6), "3"), // Blue
        ButtonData(Color(0xFFFFB74D), "4"), // Orange
        ButtonData(Color(0xFFBA68C8), "5"), // Purple
        ButtonData(Color(0xFF4DB6AC), "6"), // Teal
        ButtonData(Color(0xFFFF8A65), "7"), // Deep Orange
        ButtonData(Color(0xFF90A4AE), "8"), // Blue Grey
        ButtonData(Color(0xFFF06292), "9"), // Pink
        ButtonData(Color(0xFF7986CB), "10"), // Indigo
        ButtonData(Color(0xFF4DD0E1), "11"), // Cyan
        ButtonData(Color(0xFFFFD54F), "12"), // Yellow
        ButtonData(Color(0xFF8D6E63), "13"), // Brown
        ButtonData(Color(0xFF9575CD), "14"), // Deep Purple
        ButtonData(Color(0xFF4FC3F7), "15"), // Light Blue
        ButtonData(Color(0xFF66BB6A), "16"), // Light Green
        ButtonData(Color(0xFFFFCC02), "17"), // Amber
        ButtonData(Color(0xFFEC407A), "18"), // Pink
        ButtonData(Color(0xFF42A5F5), "19"), // Blue
        ButtonData(Color(0xFF26A69A), "20"), // Teal
        ButtonData(Color(0xFFFF7043), "21"), // Deep Orange
        ButtonData(Color(0xFF9CCC65), "22"), // Light Green
        ButtonData(Color(0xFF26C6DA), "23"), // Cyan
        ButtonData(Color(0xFFD4E157), "24")  // Lime
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 20.dp),
        // TODO: Add modifiers to make the Column fill the screen and add padding
        // Hint: You need fillMaxSize(), padding(16.dp), and padding(top = 20.dp)
        // See "Adding Modifiers" lesson for examples of layout modifiers

        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
        text = "Interactive Button Grid",
        style = MaterialTheme.typography.headlineMedium,
        modifier = Modifier.padding(bottom = 16.dp)
        )
        // TODO: Create the title text
        // Hint: Use Text composable with:
        // - text = "Interactive Button Grid"
        // - style = MaterialTheme.typography.headlineMedium
        // - modifier with bottom padding of 16.dp
        // See "Adding Modifiers" lesson for examples of text styling and spacing
    Text(
        text = "Selected: ${selectedButtons.size} of ${buttonData.size}",
        style = MaterialTheme.typography.bodyLarge,
        modifier = Modifier.padding(bottom = 24.dp)
    )

        // TODO: Create the selection count text
        // Hint: Use Text composable with:
        // - text = "Selected: ${selectedButtons.size} of ${buttonData.size}"
        // - style = MaterialTheme.typography.bodyLarge
        // - modifier with bottom padding of 24.dp
        // See "Adding Modifiers" lesson for examples of text styling and spacing
        FlowRow (
             horizontalArrangement = Arrangement.spacedBy(8.dp),
             verticalArrangement = Arrangement.spacedBy(8.dp),
             modifier = Modifier.fillMaxWidth()
        ){
            buttonData.forEachIndexed { index, button ->
                InteractiveButton(
                    buttonData = button,
                    isSelected = selectedButtons.contains(index),
                    onClick = {
                        selectedButtons = if (selectedButtons.contains(index)) {
                            selectedButtons.minus(index)
                            } else {
                            selectedButtons.plus(index)
                        }
                    }
                )
            }

        }
        // TODO: Create the FlowRow for the button grid
        // Hint: Use FlowRow with:
        // - horizontalArrangement = Arrangement.spacedBy(8.dp)
        // - verticalArrangement = Arrangement.spacedBy(8.dp)
        // - modifier = Modifier.fillMaxWidth()
        // See "Flow Rows and Columns" lesson for examples of FlowRow usage

        // TODO: Inside the FlowRow, create buttons using forEachIndexed
        // Hint: Use buttonData.forEachIndexed { index, button ->
        //     InteractiveButton(
        //         buttonData = button,
        //         isSelected = selectedButtons.contains(index),
        //         onClick = { /* handle selection logic */ }
        //     )
        // }
        // See "Flow Rows and Columns" lesson for examples of FlowRow content

        Spacer(modifier = Modifier.height(24.dp))
//@Composable
Button(
    onClick = { selectedButtons = setOf() },
    enabled = selectedButtons.isNotEmpty(),
    modifier = Modifier.fillMaxWidth(),

){
    Text("Clear Selection")
}
        // TODO: Create the Clear Selection button
        // Hint: Use Button composable with:
        // - onClick = { selectedButtons = setOf() }
        // - enabled = selectedButtons.isNotEmpty()
        // - modifier = Modifier.fillMaxWidth()
        // - Text("Clear Selection") inside the button
        // See "Click Events" lesson for examples of button handling
    }
}

@Composable
fun InteractiveButton(
    buttonData: ButtonData,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(80.dp)
            .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer else buttonData.color)
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                else Color.Black.copy(alpha = 0.3f),
                shape = MaterialTheme.shapes.medium
            )
            .clickable(onClick = onClick)

        // TODO: Add all the necessary modifiers:
        // 1. size(80.dp) to set the button size
        // 2. background() with color based on selection state
        // 3. border() with width and color based on selection state
        // 4. clickable() to handle taps
        // 5. shape = MaterialTheme.shapes.medium for rounded corners

        // For the background color, use if/else to choose between:
        // - MaterialTheme.colorScheme.primaryContainer (when selected)
        // - buttonData.color (when not selected)

        // For the border, use if/else to choose between:
        // - width = 3.dp, color = MaterialTheme.colorScheme.primary (when selected)
        // - width = 1.dp, color = Color.Black.copy(alpha = 0.3f) (when not selected)

        // See "Adding Modifiers" lesson for examples of layout modifiers
        // See "Custom Modifiers" lesson for examples of conditional styling
        // See "Click Events" lesson for examples of clickable modifiers
        ,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = buttonData.number,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else Color.White,
            // TODO: Set the text color based on selection state
            // Hint: Use if/else to choose between:
            // - MaterialTheme.colorScheme.onPrimaryContainer (when selected)
            // - Color.White (when not selected)
            // See "Custom Modifiers" lesson for examples of conditional styling
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

// Data class to hold button information - already provided
data class ButtonData(
    val color: Color,
    val number: String
)

/**
 * Preview for Android Studio's design view.
 */
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun InteractiveButtonGridPreview() {
    InteractiveButtonGrid()
}