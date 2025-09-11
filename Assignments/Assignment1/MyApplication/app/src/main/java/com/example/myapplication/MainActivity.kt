 package com.example.myapplication

 import android.os.Bundle
 import androidx.activity.ComponentActivity
 import androidx.activity.compose.setContent
 import androidx.activity.enableEdgeToEdge
 import androidx.compose.foundation.background
 import androidx.compose.foundation.layout.*
 import androidx.compose.foundation.shape.RoundedCornerShape
 import androidx.compose.material3.*
 import androidx.compose.runtime.*
 import androidx.compose.ui.Alignment
 import androidx.compose.ui.Modifier
 import androidx.compose.ui.graphics.Color
 import androidx.compose.ui.text.font.FontWeight
 import androidx.compose.ui.text.style.TextAlign
 import androidx.compose.ui.tooling.preview.Preview
 import androidx.compose.ui.unit.dp
 import androidx.compose.ui.unit.sp

 /**
  * This project covers concepts from Chapter 4 lessons:
  * - "Box Layout" - for layered content and overlays
  * - "Column Layout" - for vertical content arrangement
  * - "Row Layout" - for horizontal content arrangement
  * - "Layout Basics" - for fundamental layout concepts and styling
  *
  * Students should review these lessons before starting:
  * - Box Layout lesson for overlay and layered content
  * - Column Layout lesson for vertical arrangement
  * - Row Layout lesson for horizontal arrangement
  * - Layout Basics lesson for fundamental concepts
  */

 class MainActivity : ComponentActivity() {
     override fun onCreate(savedInstanceState: Bundle?) {
         super.onCreate(savedInstanceState)
         enableEdgeToEdge()
         setContent {
             MaterialTheme {
                 WeatherDashboard()
             }
         }
     }
 }


 @Composable
 fun WeatherDashboard() {
     Column(
         modifier = Modifier
             .fillMaxSize()
             .padding(16.dp)
             .padding(top = 50.dp),
         horizontalAlignment = Alignment.CenterHorizontally
     ) {
         CurrentWeatherSection()
     }
 }


 @Composable
 fun CurrentWeatherSection() {
     Box(
         modifier = Modifier
             .fillMaxWidth()
             .background(
                 MaterialTheme.colorScheme.primaryContainer,
                 RoundedCornerShape(16.dp)
             )
             .padding(20.dp)

     ) {
         // TODO: Call the WeatherAlertOverlay composable function here
         // Hint: You need to call WeatherAlertOverlay()
         // See "Box Layout" lesson for examples of Box usage and content alignment
         WeatherAlertOverlay()

         // TODO: Create a Column with 3 text elements and 1 row of two text elements
         // The column will have horizontalAlignment=Alignment.CenterHorizontally
         // All text colors will be MaterialTheme.colorScheme.onPrimaryContainer
         // See "Column Layout" lesson for examples of Column usage and alignment


         Column(
             horizontalAlignment = Alignment.CenterHorizontally,
             modifier = Modifier.padding(16.dp)  // Add space around the column

         ) {
             Text("72 F", fontSize = 48.sp, fontWeight = FontWeight.Bold)  // First text element
             Text(
                 "Partly Cloudy",
                 fontSize = 18.sp
             )  // Second text element") // Second text element
             Text("Ann Arbor, MI", fontSize = 16.sp)  // Third text element")


             Row(

                 modifier = Modifier.padding(16.dp),  // Add space around the row
                 horizontalArrangement = Arrangement.spacedBy(32.dp)

             ) {
                 Text("H: 78 F", fontSize = 16.sp)
                 Text("L: 65 F", fontSize = 16.sp)

             }
         }
     }

         // TODO: First text element displaying "72 F" will have:
         // - fontSize = 48.sp
         // - fontWeight = FontWeight.Bold
         // See "Layout Basics" lesson for examples of text styling and sizing

         // TODO: Second text element displaying "Partly Cloudy" will have:
         // - fontSize = 18.sp
         // See "Layout Basics" lesson for examples of text styling

         // TODO: Third text element displaying "Ann Arbor, MI" will have:
         // - fontSize = 16.sp
         // See "Layout Basics" lesson for examples of text styling

         // TODO: Create a Row with the following properties:
         // - modifier = Modifier.padding(top = 16.dp)
         // - horizontalArrangement = Arrangement.spacedBy(32.dp)
         // See "Row Layout" lesson for examples of Row usage and arrangement

         // TODO: Add two text elements to the row:
         // - First text: "H: 78 F" with fontSize = 16.sp
         // - Second text: "L: 65 F" with fontSize = 16.sp
         // See "Row Layout" lesson for examples of adding content to rows
     }

 @Composable
 fun WeatherAlertOverlay() {
     Box(
         modifier = Modifier.fillMaxWidth(),
         contentAlignment = Alignment.TopEnd
     ) {
         Card(
             modifier = Modifier.padding(8.dp),
             colors = CardDefaults.cardColors(
                 containerColor = Color.Red
             ),
             shape = RoundedCornerShape(8.dp)
         ) {
             Text(
                 text = "Severe Weather",
                 color = Color.White,
                 fontSize = 12.sp,
                 fontWeight = FontWeight.Bold,
                 modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                 textAlign = TextAlign.Center
             )
         }
     }
 }



 /**
  * Preview for Android Studio's design view.
  */
 @Preview(showBackground = true, showSystemUi = true)
 @Composable
 fun WeatherDashboardPreview(){
     WeatherDashboard()
 }