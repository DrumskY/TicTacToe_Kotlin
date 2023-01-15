package com.project.tic_tac_toe

import com.project.tic_tac_toe.models.Board
import com.project.tic_tac_toe.models.Cell
import com.project.tic_tac_toe.models.CellState
import org.junit.Assert.*
import org.junit.Before
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.junit.Test


/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class TicTacToeUnitTest {

    @Mock
    lateinit var board: Board

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

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
        // Create a new Board object
        val board = mutableMapOf<Cell, CellState>()
        val viewModel = Board()

        board[Cell.TOP_LEFT] = CellState.Star
        board[Cell.TOP_CENTER] = CellState.Star
        val state = CellState.Star

        // Assert that the returned cell is the winning move for the provided state
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

    @Test
    fun testClearBoard() {
        // Create a new Board object
        val board = Board()

        // Set the state of a cell to Circle
        board.setCell(Cell.CENTER_CENTER, CellState.Circle)

        // Verify that the cell state is Circle
        assertEquals(board.centerCenter, CellState.Circle)

        // Call the clearBoard method
        board.clearBoard()

        // Assert that the cell state is now Blank
        assertEquals(board.centerCenter, CellState.Blank)
    }

    @Test
    fun testBoardClicked() {
        val mainActivityViewModel = MainActivityViewModel()
        // Call the boardClicked method and pass in a Cell object
        mainActivityViewModel.boardClicked(Cell.TOP_LEFT)

        // Assert that the cell state in the board is now CellState.Star
        assertEquals(mainActivityViewModel.board.value?.topLeft, CellState.Star)
    }

    @Test
    fun testAiTurn() {
        val viewModel = MainActivityViewModel()
        val board = Board()
        board.setCell(Cell.TOP_LEFT, CellState.Star)
        board.setCell(Cell.TOP_CENTER, CellState.Star)

        viewModel.aiTurn()

        val expected = Cell.TOP_RIGHT
        val actual = board.findNextWinningMove(CellState.Star)
        assertEquals(expected, actual)
    }
}