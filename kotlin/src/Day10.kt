abstract class VideoInstr(val cycles: Int)
class AddX(val amount: Int) : VideoInstr(2)
class Noop : VideoInstr(1)

fun main() {
    fun parseInstr(line: String): VideoInstr {
        return if (line == "noop") {
            Noop()
        } else {
            val split = line.split(" ")
            AddX(split[1].toInt())
        }
    }

    fun part1(input: List<String>): Int {
        val instructions = input.map { parseInstr(it) }
        var regX = 1
        var cycle = 0
        var nextSignalCycle = 20
        var signalSum = 0
        for (instruction in instructions) {
            cycle += instruction.cycles
            if (cycle >= nextSignalCycle) {
                signalSum += nextSignalCycle * regX
                nextSignalCycle += 40
            }
            if (instruction is AddX) {
                regX += instruction.amount
            }
        }
        return signalSum
    }

    fun part2(input: List<String>): Int {
        val instructions = input.map { parseInstr(it) }
        var cycle = 0
        var regX = 1
        val crtLines = mutableListOf("")
        var crtPosition = 0
        for (instruction in instructions) {
            for (i in 1..instruction.cycles) {
                cycle += 1
                if (crtPosition == 40) {
                    println(crtLines.last())
                    crtLines.add("")
                    crtPosition = 0
                }
                if (crtPosition in regX - 1..regX + 1) {
                    crtLines[crtLines.lastIndex] += "#"
                } else {
                    crtLines[crtLines.lastIndex] += "."
                }
                crtPosition += 1
            }
            if (instruction is AddX) {
                regX += instruction.amount
            }
        }
        println(crtLines.last())
        return 0
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("day-10", "simple")
    check(part1(testInput) == 13140)
    part2(testInput)

    val input = readLines("day-10", "full")
    check(part1(input) == 14160)
    println(part1(input))
    part2(input)
    println("Result should be RJERPEFC")
}
