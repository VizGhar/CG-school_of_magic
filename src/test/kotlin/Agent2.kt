import java.util.*

object Agent2 {

    var rounds = 0

    @JvmStatic
    fun main(args: Array<String>?) {
        val scanner = Scanner(System.`in`)
        while (true) {
            rounds++
            var spell = scanner.nextLine()
            if (rounds % 2 == 0) {
                spell = "i dont know"
            }
            println(spell)
        }
    }
}