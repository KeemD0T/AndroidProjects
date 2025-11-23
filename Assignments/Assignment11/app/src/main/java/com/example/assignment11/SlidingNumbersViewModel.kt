package com.example.assignment11

// No need for mutableStateOf here
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SlidingNumbersViewModel : ViewModel() {

    private val _tiles = MutableStateFlow<List<String>>(emptyList())
    val tiles: StateFlow<List<String>> = _tiles.asStateFlow()

    private val _moveCount = MutableStateFlow(0)
    val moveCount: StateFlow<Int> = _moveCount.asStateFlow()


    private val _gameStatus = MutableStateFlow("Playing")
    val gameStatus: StateFlow<String> = _gameStatus.asStateFlow()

    private var blankIndex: Int = -1

    init {
        resetPuzzle()
    }

    fun resetPuzzle() {
        val newTiles = (1..8).map { it.toString() }.shuffled().toMutableList()
        newTiles.add("")
        blankIndex = 8
        _tiles.value = newTiles
        _moveCount.value = 0
        _gameStatus.value = "Playing" // Reset the status on new puzzle
    }

    fun moveTile(tileIndex: Int) {
        if (canMove(tileIndex)) {
            val newTiles = _tiles.value.toMutableList()
            newTiles[blankIndex] = newTiles[tileIndex]
            newTiles[tileIndex] = ""
            _tiles.value = newTiles
            blankIndex = tileIndex
            incrementMoveCount()


            checkWinCondition()
        }
    }

    private fun incrementMoveCount() {
        _moveCount.update { currentCount -> currentCount + 1 }
    }

    // --- This is your complete and correct function ---
    fun checkWinCondition() {
        // The winning state is numbers 1 through 8, followed by a blank tile.
        val winningState = (1..8).map { it.toString() } + ""
        if (_tiles.value == winningState) {
            _gameStatus.value = "Solved!"
        }
    }

    private fun canMove(tileIndex: Int): Boolean {
        if (tileIndex < 0 || tileIndex > 8) return false
        val row = tileIndex / 3
        val col = tileIndex % 3
        val blankRow = blankIndex / 3
        val blankCol = blankIndex % 3
        val isHorizontalMove = (row == blankRow && (col == blankCol + 1 || col == blankCol - 1))
        val isVerticalMove = (col == blankCol && (row == blankRow + 1 || row == blankRow - 1))
        return isHorizontalMove || isVerticalMove
    }
}
