fun findUniqueSequence(input: String, windowSize: Int): Int {
    val map = mutableMapOf<Char, Int>()
    input.substring(0, windowSize - 1).forEach {
        map[it] = map.getOrDefault(it, 0) + 1
    }
    input.withIndex().drop(windowSize - 1).forEach { (idx, c) ->
        val entry = map[c]
        if (entry == null && map.size == windowSize - 1) {
            // This entry not yet in + all other elements only have one element
            // -> This is the solution!
            return idx + 1
        }
        map[c] = map.getOrDefault(c, 0) + 1

        val toRemoveC = input[idx - (windowSize - 1)]
        val removed = map.remove(toRemoveC, 1)
        if (!removed) {
            map[toRemoveC] = map[toRemoveC]!! - 1
        }
    }
    return input.length
}

fun main() {
    fun part1(input: String): Int {
        return findUniqueSequence(input, 4)
    }

    fun part2(input: String): Int {
        return findUniqueSequence(input, 14)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day-06", "simple")
    check(part1(testInput) == 7)
    check(part1("bvwbjplbgvbhsrlpgdmjqwftvncz") == 5)
    check(part1("nppdvjthqldpwncqszvftbrmjlhg") == 6)
    check(part1("nznrnfrfntjfmvfwmzdfjlvtqnbhcprsg") == 10)
    check(part1("zcfzfwzzqfrljwzlrfnpqdbhtmscgvjw") == 11)
    check(part2(testInput) == 19)

    val input = readInput("day-06", "full")
    check(part1(input) == 1804)
    check(part2(input) == 2508)
    println(part1(input))
    println(part2(input))
}
