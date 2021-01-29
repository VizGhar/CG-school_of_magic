import java.util.*
import java.io.*
import java.math.*

fun main(args : Array<String>) {
    val scanner = Scanner(System.`in`)
    var rounds = 0
    while (true) {
        rounds++
        var spell = scanner.nextLine()
        if (rounds % 2 == 0) {
            spell = "i dont know"
        }
        // Write an action using println()
        // To debug: System.err.println("Debug messages...");

        println(spell)
    }
}