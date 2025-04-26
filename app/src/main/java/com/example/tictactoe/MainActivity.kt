package com.example.tictactoe

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tictactoe.ui.theme.TicTacToeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TicTacToeTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    TicTacToeGame(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun TicTacToeGame(modifier: Modifier = Modifier) {
    var board by remember { mutableStateOf(Array(3) { Array(3) { "" } }) }
    var currentPlayer by remember { mutableStateOf("X") }
    var gameStatus by remember { mutableStateOf("") }
    var winningCombination by remember { mutableStateOf<List<Pair<Int, Int>>>(emptyList()) }

    fun checkWinner(): Boolean {
        // Check rows
        for (i in 0..2) {
            if (board[i][0].isNotEmpty() && board[i][0] == board[i][1] && board[i][1] == board[i][2]) {
                winningCombination = listOf(Pair(i, 0), Pair(i, 1), Pair(i, 2))
                return true
            }
        }

        // Check columns
        for (i in 0..2) {
            if (board[0][i].isNotEmpty() && board[0][i] == board[1][i] && board[1][i] == board[2][i]) {
                winningCombination = listOf(Pair(0, i), Pair(1, i), Pair(2, i))
                return true
            }
        }

        // Check diagonals
        if (board[0][0].isNotEmpty() && board[0][0] == board[1][1] && board[1][1] == board[2][2]) {
            winningCombination = listOf(Pair(0, 0), Pair(1, 1), Pair(2, 2))
            return true
        }
        if (board[0][2].isNotEmpty() && board[0][2] == board[1][1] && board[1][1] == board[2][0]) {
            winningCombination = listOf(Pair(0, 2), Pair(1, 1), Pair(2, 0))
            return true
        }

        return false
    }

    fun isBoardFull(): Boolean {
        return board.all { row -> row.all { it.isNotEmpty() } }
    }

    fun resetGame() {
        board = Array(3) { Array(3) { "" } }
        currentPlayer = "X"
        gameStatus = ""
        winningCombination = emptyList()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = if (gameStatus.isEmpty()) "Player $currentPlayer's turn" else gameStatus,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // Game Board
        Column {
            for (i in 0..2) {
                Row {
                    for (j in 0..2) {
                        val isWinningCell = winningCombination.contains(Pair(i, j))
                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .border(1.dp, Color.Black)
                                .background(
                                    if (isWinningCell) Color.Green.copy(alpha = 0.3f)
                                    else Color.White
                                )
                                .clickable(
                                    enabled = board[i][j].isEmpty() && gameStatus.isEmpty()
                                ) {
                                    board[i][j] = currentPlayer
                                    if (checkWinner()) {
                                        gameStatus = "Player $currentPlayer wins!"
                                    } else if (isBoardFull()) {
                                        gameStatus = "It's a draw!"
                                    } else {
                                        currentPlayer = if (currentPlayer == "X") "O" else "X"
                                    }
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = board[i][j],
                                fontSize = 40.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }

        Button(
            onClick = { resetGame() },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
        ) {
            Text("Reset Game")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TicTacToeGamePreview() {
    TicTacToeTheme {
        TicTacToeGame()
    }
}