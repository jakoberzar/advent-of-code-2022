import kotlin.math.abs

fun main() {
    val MAX_POSSIBLE_HEIGHT = 9

    fun parseInput(input: List<String>): List<List<Int>> {
        return input.map { line -> line.map { c: Char -> c.digitToInt() } }
    }


    fun part1(input: List<String>): Int {
        val grid: List<List<Int>> = parseInput(input)

        val visibleTrees: MutableList<MutableList<Boolean>> =
            grid.map { row -> row.map { false }.toMutableList() }.toMutableList()
        var visibleTreeCount = grid.size * 2 + ((grid[0].size - 2) * 2) // Count the bordering trees

        fun updateVisibleTree(row: Int, col: Int) {
            if (!visibleTrees[row][col]) {
                visibleTrees[row][col] = true
                visibleTreeCount += 1
            }
        }

        for (row in 1 until grid.lastIndex) {
            // Check left -> right
            var maxLeftHeight = grid[row].first()
            for (col in 1 until grid[row].lastIndex) {
                val height = grid[row][col]
                if (height > maxLeftHeight) {
                    maxLeftHeight = height
                    updateVisibleTree(row, col)
                }
            }

            // Check right -> left
            var maxRightHeight = grid[row].last()
            for (col in grid[row].lastIndex - 1 downTo 1) {
                val height = grid[row][col]
                if (height > maxRightHeight) {
                    maxRightHeight = height
                    updateVisibleTree(row, col)
                }
                if (maxRightHeight == maxLeftHeight) {
                    // There are no more visible trees between maxLeftHeight and maxRightHeight
                    break
                }
            }
        }

        for (col in 1 until grid[0].lastIndex) {
            // Check up -> down
            var maxUpHeight = grid.first()[col]
            for (row in 1 until grid.lastIndex) {
                val height = grid[row][col]
                if (height > maxUpHeight) {
                    maxUpHeight = height
                    updateVisibleTree(row, col)
                }
            }

            // Check down -> up
            var maxDownHeight = grid.last()[col]
            for (row in grid.lastIndex - 1 downTo 1) {
                val height = grid[row][col]
                if (height > maxDownHeight) {
                    maxDownHeight = height
                    updateVisibleTree(row, col)
                }
                if (maxDownHeight == maxUpHeight) {
                    // There are no more visible trees between maxUpHeight and maxDownHeight
                    break
                }
            }
        }

        return visibleTreeCount
    }

    fun part2(input: List<String>): Int {
        val grid: List<List<Int>> = parseInput(input)
        val scenicScores: MutableList<MutableList<Int>> =
            grid.map { row -> row.map { 1 }.toMutableList() }.toMutableList()

        fun initHeightDistances(initValue: Int): MutableList<Int> =
            (0..MAX_POSSIBLE_HEIGHT).map { initValue }.toMutableList()

        fun updateHeightDistances(height: Int, index: Int, distanceMap: MutableList<Int>) {
            for (givenHeight in 0..height) {
                distanceMap[givenHeight] = index
            }
        }

        fun processTree(row: Int, col: Int, index: Int, lastHeightDistances: MutableList<Int>) {
            val height = grid[row][col]
            val score = abs(index - lastHeightDistances[height])
            scenicScores[row][col] *= score
            updateHeightDistances(height, index, lastHeightDistances)
        }

        for (row in 1 until grid.lastIndex) {
            // Check left -> right
            var lastHeightDistances = initHeightDistances(0)
            for (col in 1 until grid[row].lastIndex) {
                processTree(row, col, index = col, lastHeightDistances)
            }

            // Check right -> left
            lastHeightDistances = initHeightDistances(grid[row].lastIndex)
            for (col in grid[row].lastIndex - 1 downTo 1) {
                processTree(row, col, index = col, lastHeightDistances)
            }
        }

        for (col in 1 until grid[0].lastIndex) {
            // Check up -> down
            var lastHeightDistances = initHeightDistances(0)
            for (row in 1 until grid.lastIndex) {
                processTree(row, col, index = row, lastHeightDistances)
            }

            // Check down -> up
            lastHeightDistances = initHeightDistances(grid.lastIndex)
            for (row in grid.lastIndex - 1 downTo 1) {
                processTree(row, col, index = row, lastHeightDistances)
            }
        }

        return scenicScores.flatten().max()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("day-08", "simple")
    check(part1(testInput) == 21)
    check(part2(testInput) == 8)

    val input = readLines("day-08", "full")
    check(part1(input) == 1814)
    check(part2(input) == 330786)
    println(part1(input))
    println(part2(input))
}
