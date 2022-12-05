data class Instruction(val count: Int, val from: Int, val to: Int) {}
typealias Crates = MutableList<MutableList<Char>>

fun parseInput(input: String): Pair<Crates, List<Instruction>> {
    val inputParts = input.trimEnd().split("\n\n")

    val crateLines = inputParts[0].lines().toMutableList()
    val crateColumnCount = crateLines.last().trim().last().digitToInt() // Assumes there's max 9 columns
    crateLines.removeLast()

    fun getTextCol(crateColumn: Int) = (crateColumn - 1) * 4 + 1

    val crates: Crates = mutableListOf(mutableListOf())
    crates.addAll((1..crateColumnCount).map {
        val list = mutableListOf<Char>()
        val colPosition = getTextCol(it)
        var row = crateLines.lastIndex
        while (row >= 0 && crateLines[row].lastIndex >= colPosition && crateLines[row][colPosition] != ' ') {
            list.add(crateLines[row][colPosition])
            row -= 1
        }
        list
    })

    val instructions = inputParts[1].lines().map { line ->
        var split = line.substring(5).split(" from ")
        val count = split[0].toInt()

        split = split[1].split(" to ")
        val from = split[0].toInt()
        val to = split[1].toInt()

        Instruction(count, from, to)
    }

    return Pair(crates, instructions)
}

fun getTopCrateString(crates: Crates): String {
    return crates.subList(1, crates.size).map { it.last() }.joinToString("")
}

fun main() {

    fun part1(input: String): String {
        val (crates, instructions) = parseInput(input)
        instructions.forEach { instr ->
            for (i in 1..instr.count) {
                val crate = crates[instr.from].last()
                crates[instr.from].removeLast()
                crates[instr.to].add(crate)
            }
        }
        return getTopCrateString(crates)
    }

    fun part2(input: String): String {
        val (crates, instructions) = parseInput(input)
        instructions.forEach { instr ->
            val movingCrates =
                crates[instr.from].subList(crates[instr.from].size - instr.count, crates[instr.from].size)
            crates[instr.to].addAll(movingCrates)
            for (i in 1..instr.count) {
                crates[instr.from].removeLast()

            }
        }
        return getTopCrateString(crates)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day-05", "simple")
    check(part1(testInput) == "CMZ")
    check(part2(testInput) == "MCD")

    val input = readInput("day-05", "full")
    check(part1(input) == "RNZLFZSJH")
    check(part2(input) == "CNSFCGJSM")
    println(part1(input))
    println(part2(input))
}
