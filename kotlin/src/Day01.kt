fun main() {
    fun calculateCalories(input: List<String>): MutableList<Int> {
        val calories = mutableListOf<Int>(0)
        for (line in input) {
            if (line.isEmpty()) {
                calories.add(0)
                continue
            }
            calories[calories.lastIndex] += line.toInt()
        }
        return calories
    }

    fun part1(input: List<String>): Int {
        return calculateCalories(input).max()
    }

    fun part2(input: List<String>): Int {
        val calories = calculateCalories(input)
        calories.sortDescending()
        return calories.subList(0, 3).sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("day-01", "simple")
    check(part1(testInput) == 24000)
    check(part2(testInput) == 45000)

    val input = readLines("day-01", "full")
    println(part1(input))
    println(part2(input))
}
