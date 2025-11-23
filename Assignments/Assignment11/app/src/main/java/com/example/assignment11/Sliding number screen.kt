package com.example.assignment11

//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.geometry.isEmpty
//import androidx.compose.ui.graphics.RectangleShape
//import androidx.compose.ui.layout.TestModifierUpdaterLayout
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel



@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun PhotoGallery(  slidingNumbersViewModel: SlidingNumbersViewModel = viewModel()) {



    val tiles by slidingNumbersViewModel.tiles.collectAsState()
    val moveCount by slidingNumbersViewModel.moveCount.collectAsState()
    val gameStatus by slidingNumbersViewModel.gameStatus.collectAsState()




    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Slidng Number Puzzle",
                        fontSize = 24.sp,

                        )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF8FABE7),
                    // Set the title color to dark blue
                    titleContentColor = Color(0xFF000000)
                )
            )
        }
    )

    { paddingValues -> // This lambda is the content of the Scaffold
        Card(

            // This modifier controls the Card's position and size
            modifier = Modifier,
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(0xFFE2E3ED)
            ),
            // elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)



        ) {

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally, // Center children horizontally
                verticalArrangement = Arrangement.Center // Center children vertically
            ) {
                if (gameStatus == "Solved!") { // Use "==" for comparison
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 24.dp), // Add padding
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFF475D91) // Blue color
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.Start
                        ) {
                            Text(
                                text = "Puzzle Solved!",
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,

                                color = Color(0xFFEEEEF3) // Dark Blue
                            )
                            Text(
                                text = "You solved it in $moveCount moves.",
                                fontSize = 18.sp,
                                color = Color.White,
                                modifier = Modifier.padding(top = 8.dp)
                            )
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly, // Pushes children to the ends
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Card(
                        modifier = Modifier
                            .width(120.dp) // Set a fixed width for the card
                            .height(50.dp), // Set a fixed height for the card

                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE0E1EB)
                        ),
                        // Center the text within the now larger card
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Moves: " +
                                        "$moveCount",
                                fontSize = 18.sp, // Made the font smaller
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray, // Set text to white for contrast
                            )


                        }
                    }


                    Card(
                        modifier = Modifier
                            .width(120.dp) // Set a fixed width for the card
                            .height(50.dp), // Set a fixed height for the card
                        colors = CardDefaults.cardColors(
                            containerColor = Color(0xFFE0E1EB) // Blue color
                        ),
                        // Center the text within the now larger card
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Status: " + "$gameStatus",

                                fontSize = 18.sp, // Made the font smaller
                                fontWeight = FontWeight.Bold,
                                color = Color.DarkGray, // Set text to white for contrast
                            )
                        }
                    }
                }



                Button(
                    onClick = { /* Handle button click */
                        // Shuffle the photos when the button is clicked
                        slidingNumbersViewModel.resetPuzzle()
                    },
                    modifier = Modifier

                        .width(300.dp)
                        .height(80.dp) // Set a fixed size to make it bigger and square
                        .padding(16.dp),
                    shape = RoundedCornerShape(10.dp), // Apply rounded corners
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF575E70) // Blue color
                    ), // Set the background color to gray

                ) {
                    Text(text = "New Puzzle", color = Color.White)
                }
                Card(
                    shape = RoundedCornerShape(12.dp),
                   elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                    // This modifier controls the Card's position and size
                    modifier = Modifier
                        .padding(top = 50.dp, start = 16.dp, end = 16.dp)
                        .aspectRatio(1f) // Makes the Card a perfect square
                ) {

                    LazyVerticalGrid(

                        columns = GridCells.Fixed(3),
                        modifier = Modifier,
                        contentPadding = PaddingValues(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        userScrollEnabled = true
                    ) {
                        items(tiles.size) { index ->
                            val tile = tiles[index]

                            if (tile.isEmpty()) {
                                // This Box creates the blank space
                                Box(modifier = Modifier.aspectRatio(1f))
                            } else {
                                Card(
                                    modifier = Modifier
                                        .aspectRatio(1f)
                                        .clickable { slidingNumbersViewModel.moveTile(index) },
                                    // Set the Card's background color to light blue
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color(0xFFE2F0FB)
                                    )
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = tile,
                                            // Set the text style
                                            style = MaterialTheme.typography.headlineLarge.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF1565BF) // Dark Blue
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}



