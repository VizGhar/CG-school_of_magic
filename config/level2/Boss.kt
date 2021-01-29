import java.util.*
import java.io.*
import java.math.*
fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    var rounds = 0
    while (true) {
        rounds++

        var spell = scanner.nextInt()

        val answer = if (rounds % 2 == 0) {
            "I don't know"
        } else {
            if (spell > 100) "DEFEND" else "ATTACK"
        }
        println(answer)
    }
}