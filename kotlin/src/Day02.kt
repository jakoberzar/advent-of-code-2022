fun main() {
    fun calculateLineScore(line: String): Int {
        val enemy = line[0]
        val me = line[2]

        val shapeScores = mapOf<Char, Int>('X' to 1, 'Y' to 2, 'Z' to 3)
        val shapeScore = shapeScores[me]!!.toInt()

        val beatScores = mapOf<Char, Map<Char, Int>>('X' to mapOf<Char, Int>('A' to 3, 'B' to 0, 'C' to 6),
            'Y' to mapOf<Char, Int>('A' to 6, 'B' to 3, 'C' to 0),
            'Z' to mapOf<Char, Int>('A' to 0, 'B' to 6, 'C' to 3))
        val beatScore = beatScores[me]!![enemy]!!.toInt()

        return shapeScore + beatScore
    }

    fun calculateLineScorePart2(line: String): Int {
        val enemy = line[0]
        val result = line[2]

        val shapeScores = mapOf<Char, Map<Char, Int>>('X' to mapOf<Char, Int>('A' to 3, 'B' to 1, 'C' to 2),
            'Y' to mapOf<Char, Int>('A' to 1, 'B' to 2, 'C' to 3),
            'Z' to mapOf<Char, Int>('A' to 2, 'B' to 3, 'C' to 1))
        val shapeScore = shapeScores[result]!![enemy]!!.toInt()

        val beatScores = mapOf<Char, Int>('X' to 0, 'Y' to 3, 'Z' to 6)
        val beatScore = beatScores[result]!!.toInt()

        return beatScore + shapeScore
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { calculateLineScore(it) }
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { calculateLineScorePart2(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day-02", "simple")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("day-02", "full")
    println(part1(input))
    println(part2(input))
}
