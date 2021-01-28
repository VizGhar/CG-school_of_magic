package com.codingame.game.ui

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