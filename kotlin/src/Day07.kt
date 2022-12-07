abstract class Node(val name: String, val parent: Directory?) {
    abstract fun size(): Int
}

class File(name: String, private val size: Int, parent: Directory) : Node(name, parent) {
    override fun size(): Int = size
}

class Directory(name: String, parent: Directory?) : Node(name, parent) {
    private val children = mutableListOf<Node>()
    private var cachedSize: Int? = null

    fun addChild(node: Node) {
        cachedSize = null
        children.add(node)
    }

    fun childrenDirs(): List<Directory> = children.filterIsInstance<Directory>()

    override fun size(): Int {
        if (cachedSize == null) {
            cachedSize = children.sumOf { it.size() }
        }
        return cachedSize!!
    }
}

fun getListRecursiveDirs(parent: Directory, found: MutableList<Directory>): MutableList<Directory> {
    parent.childrenDirs().forEach { getListRecursiveDirs(it, found) }
    found.add(parent)
    return found
}

fun parseNode(line: String, parent: Directory): Node {
    return if (line.startsWith("dir ")) {
        Directory(line.removePrefix("dir "), parent)
    } else {
        val split = line.split(' ')
        File(split[1], split[0].toInt(), parent)
    }
}

fun parseInput(input: List<String>): Directory {
    val root = Directory("/", null)
    var currentDir = root
    var idx = 1
    while (idx <= input.lastIndex) {
        check(input[idx].startsWith("$ "))
        val command = input[idx].removePrefix("$ ")
        when (command.substring(0, 2)) {
            "cd" -> {
                val arg = command.substring(3)
                currentDir = when (arg) {
                    "/" -> root
                    ".." -> currentDir.parent!!
                    else -> currentDir.childrenDirs().find { it.name == arg }!!
                }
            }
            "ls" -> {
                var readIdx = idx + 1
                while (readIdx <= input.lastIndex && !input[readIdx].startsWith("$")) {
                    val node = parseNode(input[readIdx], currentDir)
                    currentDir.addChild(node)
                    readIdx += 1
                }
                idx = readIdx - 1
            }
        }
        idx += 1
    }
    return root
}

fun main() {
    fun part1(input: List<String>): Int {
        val root = parseInput(input)
        val dirList = getListRecursiveDirs(root, mutableListOf())
        return dirList.map { it.size() }.filter { it <= 100000 }.sum()
    }

    fun part2(input: List<String>): Int {
        val root = parseInput(input)
        val maxRootSize = 70000000 - 30000000
        val minDeleteSize = root.size() - maxRootSize
        val dirList = getListRecursiveDirs(root, mutableListOf())
        return dirList.map { it.size() }.filter { it >= minDeleteSize }.minOf { it }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readLines("day-07", "simple")
    check(part1(testInput) == 95437)
    check(part2(testInput) == 24933642)

    val input = readLines("day-07", "full")
    check(part1(input) == 1749646)
    check(part2(input) == 1498966)
    println(part1(input))
    println(part2(input))
}
