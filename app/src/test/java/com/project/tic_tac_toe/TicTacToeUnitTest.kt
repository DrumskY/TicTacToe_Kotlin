package com.project.tic_tac_toe

import com.project.tic_tac_toe.models.Board
import com.project.tic_tac_toe.models.Cell
import com.project.tic_tac_toe.models.CellState
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TicTacToeUnitTest {
    @Test
    fun `expected true when user click on the board where the field is empty`(){
        val viewModel = Board()
        val actualList = viewModel.setCell(Cell.TOP_RIGHT, CellState.Circle)
        assertEquals("true", "$actualList")
        println("expected true when user click on the board where the field is empty: $actualList")
    }

    @Test
    fun `expected false when user click in the same place on the board`(){
        val viewModel = Board()
        viewModel.setCell(Cell.TOP_RIGHT, CellState.Circle)
        val actualList = viewModel.setCell(Cell.TOP_RIGHT, CellState.Circle)
        assertEquals("false", "$actualList")
        println("expected false when user click in the same place on the board: $actualList")
    }

    @Test
    fun `expected null when user click on the board and nobody has won`(){
        val viewModel = Board()
        val actualList = viewModel.findNextWinningMove(CellState.Circle)
        assertEquals("null", "$actualList")
        println("expected null when user click on the board and nobody has won: $actualList")
    }

    @Test
    fun emptyBoardReturnsNull() {
        val state = CellState.Star
        val viewModel = Board()
        assertEquals(null, viewModel.findNextWinningMove(state))
        println("expected null when board is empty")
    }

    @Test
    fun winningConfigurationReturnsCorrectCell() {
        val board = mutableMapOf<Cell, CellState>()
        val viewModel = Board()
        board[Cell.TOP_LEFT] = CellState.Star
        board[Cell.TOP_CENTER] = CellState.Star

        val state = CellState.Star
        assertEquals(Cell.TOP_RIGHT, viewModel.findNextWinningMove(state))
    }

    @Test
    fun noWinningConfigurationReturnsNull() {
        val board = mutableMapOf<Cell, CellState>()
        val viewModel = Board()
        board[Cell.TOP_LEFT] = CellState.Star
        board[Cell.TOP_CENTER] = CellState.Circle
        val state = CellState.Star
        assertEquals(null, viewModel.findNextWinningMove(state))
        println("expected null when the winning move in the game is not found")
    }
}