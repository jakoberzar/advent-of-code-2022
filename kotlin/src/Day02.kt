enum class Move(val score: Int) {
    ROCK(1),
    PAPER(2),
    SCISSORS(3),
}

enum class Result(val score: Int) {
    LOSS(0),
    DRAW(3),
    WIN(6),
}

val enemyMoveMap = mapOf('A' to Move.ROCK, 'B' to Move.PAPER, 'C' to Move.SCISSORS)
val playerMoveMap = mapOf('X' to Move.ROCK, 'Y' to Move.PAPER, 'Z' to Move.SCISSORS)
val resultMap = mapOf('X' to Result.LOSS, 'Y' to Result.DRAW, 'Z' to Result.WIN)

val beatMap = mapOf(Move.ROCK to Move.SCISSORS, Move.PAPER to Move.ROCK, Move.SCISSORS to Move.PAPER)
val lossMap = beatMap.map { Pair(it.value, it.key) }.toMap()

// TODO: Add unit tests :)
fun scoreBattle(player: Move, enemy: Move): Result {
    if (player == enemy) return Result.DRAW
    if (beatMap[player] == enemy) return Result.WIN
    return Result.LOSS
}

// TODO: Add unit tests :)
fun determineMove(result: Result, enemy: Move): Move {
    if (result == Result.DRAW) return enemy
    if (result == Result.LOSS) return beatMap[enemy]!!
    return lossMap[enemy]!!
}

fun <T> parseInput(lines: List<String>, playerMap: Map<Char, T>): List<Pair<Move, T>> {
    return lines.map { line ->
        val enemy = line[0]
        check(enemy in listOf<Char>('A', 'B', 'C'))
        val player = line[2]
        check(player in listOf<Char>('X', 'Y', 'Z'))

        Pair(enemyMoveMap[enemy]!!, playerMap[player]!!)
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        fun scoreRound(moves: Pair<Move, Move>): Int {
            val (enemy, player) = moves
            val moveScore = player.score
            val battleScore = scoreBattle(player, enemy).score
            return moveScore + battleScore
        }

        return parseInput(input, playerMoveMap)
            .sumOf { scoreRound(it) }
    }

    fun part2(input: List<String>): Int {
        fun scoreRound(moves: Pair<Move, Result>): Int {
            val (enemy, result) = moves
            val moveScore = determineMove(result, enemy).score
            val battleScore = result.score
            return moveScore + battleScore
        }

        return parseInput(input, resultMap)
            .sumOf { scoreRound(it) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day-02", "simple")
    check(part1(testInput) == 15)
    check(part2(testInput) == 12)

    val input = readInput("day-02", "full")
    println(part1(input))
    println(part2(input))
}
