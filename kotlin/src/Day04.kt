class Interval(input: String) {
    private val inputParts = input.split('-')
    private val lower = inputParts[0].toInt()
    private val upper = inputParts[1].toInt()

    fun fullyContains(other: Interval): Boolean {
        return lower <= other.lower && other.upper <= upper
    }

    fun containsPart(other: Interval): Boolean {
        return other.lower in lower..upper // This starts lower but contains start of other // 1-3 vs 2-2, 1-2 vs 2-3
                || other.upper in lower .. upper // Other starts lower but ends in this interval // 1-3 vs 2-2, 2-3 vs 1-2
                || other.fullyContains(this) // Other starts lower and ends higher: this interval is contained in it
    }
}

fun main() {
    fun parseInput(input: List<String>): List<Pair<Interval, Interval>> {
        return input.map {line ->
            val parts = line.split(',')
            Pair(Interval(parts[0]), Interval(parts[1]))
        }
    }

    fun part1(input: List<String>): Int {
        return parseInput(input).count { it.first.fullyContains(it.second) || it.second.fullyContains(it.first) }
    }

    fun part2(input: List<String>): Int {
        return parseInput(input).count { it.first.containsPart(it.second) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day-04", "simple")
    check(part1(testInput) == 2)
    check(part2(testInput) == 4)

    val input = readInput("day-04", "full")
    check(part1(input) == 464)
    check(part2(input) == 770)
    println(part1(input))
    println(part2(input))
}
