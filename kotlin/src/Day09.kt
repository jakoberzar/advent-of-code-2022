import kotlin.math.abs

data class Position(var row: Int, var col: Int)

class GridDay09(rows: Int, cols: Int, startPosition: Position, tailCount: Int) {
    private val headPosition = startPosition.copy()
    private val positions: List<Position> = listOf(
        headPosition,
        *(1..tailCount).map { startPosition.copy() }.toTypedArray()
    )

    private val tailVisited = Array(rows) { BooleanArray(cols) }
    init {
        tailVisited[startPosition.row][startPosition.col] = true
    }
    var countTailVisited = 1

    private fun updateVisited() {
        val tailPosition = positions.last()

        if (tailVisited[tailPosition.row][tailPosition.col]) return

        tailVisited[tailPosition.row][tailPosition.col] = true
        countTailVisited += 1

    }

    private fun positionsAdjacent(tailIdx: Int): Boolean {
        return abs(positions[tailIdx - 1].row - positions[tailIdx].row) <= 1 && abs(positions[tailIdx - 1].col - positions[tailIdx].col) <= 1
    }

    fun move(direction: Direction) {
        when (direction) {
            Direction.Left -> headPosition.col -= 1
            Direction.Right -> headPosition.col += 1
            Direction.Up -> headPosition.row -= 1
            Direction.Down -> headPosition.row += 1
        }

        for (tailIdx in 1..positions.lastIndex) {
            if (positionsAdjacent(tailIdx)) break
            val leading = positions[tailIdx - 1]
            val following = positions[tailIdx]

            if (leading.col > following.col) {
                following.col += 1
            } else if (leading.col < following.col) {
                following.col -= 1
            }

            if (leading.row > following.row) {
                following.row += 1
            } else if (leading.row < following.row) {
                following.row -= 1
            }
        }

        updateVisited()
    }
}

data class Motion(val direction: Direction, val amount: Int)

fun main() {
    fun parseInput(input: List<String>, tailCount: Int): Pair<GridDay09, List<Motion>> {
        val motions = input.map { line ->
            val direction = when (line[0]) {
                'L' -> Direction.Left
                'R' -> Direction.Right
                'U' -> Direction.Up
                'D' -> Direction.Down
                else -> throw Exception("Unknown direction!")
            }
            Motion(direction, line.substring(2).toInt())
        }
        val directionCounts = Direction.values().associateWith { direction ->
            motions.filter { it.direction == direction }.sumOf { it.amount }
        }

        val startPosition = Position(directionCounts[Direction.Up]!! + 1, directionCounts[Direction.Left]!! + 1)

        val rows = directionCounts[Direction.Down]!! + directionCounts[Direction.Up]!! + 1
        val cols = directionCounts[Direction.Left]!! + directionCounts[Direction.Right]!! + 1
        val grid = GridDay09(rows, cols, startPosition, tailCount)

        return Pair(grid, motions)
    }

    fun runSimulation(grid: GridDay09, motions: List<Motion>) {
        motions.forEach { motion ->
            for (i in 1..motion.amount) {
                grid.move(motion.direction)
            }
        }
    }

    fun part1(input: List<String>): Int {
        val (grid, motions) = parseInput(input, tailCount = 1)
        runSimulation(grid, motions)
        return grid.countTailVisited
    }

    fun part2(input: List<String>): Int {
        val (grid, motions) = parseInput(input, tailCount = 9)
        runSimulation(grid, motions)
        return grid.countTailVisited
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("day-09", "simple")
    check(part1(testInput) == 13)
    check(part2(testInput) == 1)

    val input = readLines("day-09", "full")
    check(part1(input) == 6030)
    check(part2(input) == 2545)
    println(part1(input))
    println(part2(input))
}
