import java.util.*
import java.io.*
import java.math.*
fun main(args: Array<String>) {
    val scanner = Scanner(System.`in`)
    var rounds = 0
    while (true) {
        rounds++

        val words = scanner.nextLine().split(" ")
        val intensity = words[0].toInt()
        val spell = if (words[1].toInt() > 100) DEFEND.name else ATTACK.name

        val answer = if (rounds % 2 == 0) {
            spell.repeat(intensity)
        } else {
            "I don't know"
        }
        println(answer)
        throw java.lang.Exception("this should crash 3 ")
    }
}