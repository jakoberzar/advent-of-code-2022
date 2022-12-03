fun calculatePriority(c: Char): Int {
    return if (c.isUpperCase()) {
        c - 'A' + 27
    } else {
        c - 'a' + 1
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { backpack ->
            val compartments = backpack.chunked(backpack.length / 2) { it.toSet() }
            val shared = compartments.reduce { compartment, acc -> acc.intersect(compartment) }
            check(shared.isNotEmpty())

            calculatePriority(shared.first())
        }
    }

    fun part2(input: List<String>): Int {
        return input.chunked(3)
            .sumOf { group ->
                val shared = group.map { it.toSet() }
                    .reduce { backpack, acc -> acc.intersect(backpack) }
                check(shared.isNotEmpty())

                calculatePriority(shared.first())
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day-03", "simple")
    check(part1(testInput) == 157)
    check(part2(testInput) == 70)

    val input = readInput("day-03", "full")
    check(part1(input) == 8401)
    check(part2(input) == 2641)
    println(part1(input))
    println(part2(input))
}
