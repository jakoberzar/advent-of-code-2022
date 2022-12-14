import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

/**
 * Reads lines from the given input txt file.
 */
fun readLines(day: String, name: String) = File("./../inputs/$day", "$name.txt")
    .readLines()

fun readInput(day: String, name: String) = File("./../inputs/$day", "$name.txt")
    .readText()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')


/**
 * The 4 different moving directions
 */
enum class Direction {
    Left, Right, Down, Up,
}