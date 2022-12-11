class Operation(private val sign: Sign, private val rightOperand: Long?) {
    // rightOperand is null if the value is "old"
    enum class Sign {
        PLUS,
        MULTIPLY
    }

    companion object {
        // Parse the "new = old * old", "new = old + 6" etc. string
        fun parse(input: String): Operation {
            val noLeftOperand = input.removePrefix("new = old ")
            val sign = when (noLeftOperand[0]) {
                '+' -> Sign.PLUS
                '*' -> Sign.MULTIPLY
                else -> throw Exception("Unknown operation sign!")
            }
            val rightOperandString = noLeftOperand.substring(2)
            val rightOperand = if (rightOperandString == "old") null else rightOperandString.toLong()
            return Operation(sign, rightOperand)
        }
    }

    fun calculate(old: Long): Long {
        val rightValue = rightOperand ?: old
        return when (sign) {
            Sign.PLUS -> old + rightValue
            Sign.MULTIPLY -> old * rightValue
        }
    }
}

class Monkey(
    private val startingItems: MutableList<Long>,
    private val operation: Operation,
    private val divTest: Int,
    private val throwTrue: Int,
    private val throwFalse: Int
) {
    var processedItemCount: Long = 0

    companion object {
        var divisor = 1L

        fun resetDivisor() {
            divisor = 1L
        }
    }

    init {
        divisor *= divTest.toLong()
    }

    private fun catchItem(item: Long) {
        startingItems.add(item)
    }

    fun processItems(otherMonkeys: List<Monkey>, divideBy3: Boolean) {
        for (item in startingItems) {
            val worryLevelHandling = operation.calculate(item)
            val worryLevel = if (divideBy3) worryLevelHandling / 3 else worryLevelHandling % divisor
            val nextMonkey = if (worryLevel % divTest == 0L) throwTrue else throwFalse
            otherMonkeys[nextMonkey].catchItem(worryLevel)
        }
        processedItemCount += startingItems.size
        startingItems.clear()
    }


}

fun main() {
    fun parseInput(input: List<String>): List<Monkey> {
        Monkey.resetDivisor()
        val monkeys = mutableListOf<Monkey>()
        var idx = 0
        while (idx < input.lastIndex) {
            idx += 1 // Skip Monkey: 0 line

            // Parse starting items
            val startingItems = input[idx].trim().removePrefix("Starting items: ")
                .split(", ")
                .map { it.toLong() }
            idx += 1
            // Parse operation
            val operation = Operation.parse(input[idx].trim().removePrefix("Operation: "))
            idx += 1
            // Parse test
            val divTest = input[idx].trim().removePrefix("Test: divisible by ").toInt()
            idx += 1
            val throwTrue = input[idx].trim().removePrefix("If true: throw to monkey ").toInt()
            idx += 1
            val throwFalse = input[idx].trim().removePrefix("If false: throw to monkey ").toInt()
            idx += 1
            monkeys.add(Monkey(startingItems.toMutableList(), operation, divTest, throwTrue, throwFalse))
            idx += 1 // Skip empty line
        }

        return monkeys
    }

    fun simulateRoundsAndGetScore(monkeys: List<Monkey>, divideBy3: Boolean, rounds: Int): Long {
        for (round in 1..rounds) {
            for (monkey in monkeys) {
                monkey.processItems(monkeys, divideBy3)
            }
        }

        val sorted = monkeys.sortedByDescending { it.processedItemCount }
        return sorted[0].processedItemCount * sorted[1].processedItemCount
    }

    fun part1(input: List<String>): Long {
        val monkeys: List<Monkey> = parseInput(input)
        return simulateRoundsAndGetScore(monkeys, divideBy3 = true, rounds = 20)
    }

    fun part2(input: List<String>): Long {
        val monkeys: List<Monkey> = parseInput(input)
        return simulateRoundsAndGetScore(monkeys, divideBy3 = false, rounds = 10000)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("day-11", "simple")
    check(part1(testInput) == 10605L)
    check(part2(testInput) == 2713310158L)

    val input = readLines("day-11", "full")
    check(part1(input) == 90882L)
    check(part2(input) == 30893109657L)
    println(part1(input))
    println(part2(input))
}
