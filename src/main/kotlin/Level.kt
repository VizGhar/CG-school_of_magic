package com.codingame.game

import com.codingame.game.Level.Animation.*
import java.lang.NumberFormatException
import java.lang.RuntimeException
import kotlin.random.Random

sealed class Level(val random: Random) {

    enum class Animation { ATTACK, DEFEND, DIE, IDLE }

    data class TurnOutcome(val isValid: Boolean, val animation: Animation)

    abstract fun generateTurnInput(): String
    abstract fun isPlayerOutputValid(playerInput: String, playerOutput: String): TurnOutcome

    class Level1(random: Random) : Level(random) {
        override fun generateTurnInput(): String = random.nextInt(1000).toString()
        override fun isPlayerOutputValid(playerInput: String, playerOutput: String): TurnOutcome {
            val valid = playerInput == playerOutput
            return TurnOutcome(valid, if (valid) ATTACK else IDLE)
        }
    }

    class Level2(random: Random) : Level(random) {
        override fun generateTurnInput(): String = random.nextInt(200).toString()
        override fun isPlayerOutputValid(playerInput: String, playerOutput: String): TurnOutcome {
            return try {
                val answer = expectedAnswer(playerInput)
                val valid = playerOutput == answer
                TurnOutcome(valid, Animation.values().first { it.name == answer })
            } catch (numberFormatException: NumberFormatException) {
                TurnOutcome(false, IDLE)
            }
        }

        public fun expectedAnswer(playerInput: String): String {
            return if (playerInput.toInt() > 100) DEFEND.name else ATTACK.name
        }
    }

    class Level3(random: Random) : Level(random) {
        override fun generateTurnInput(): String = "${random.nextInt(1, 10)} ${random.nextInt(200)}"
        override fun isPlayerOutputValid(playerInput: String, playerOutput: String): TurnOutcome {
            return if (expectedAnswer(playerInput) == playerOutput) {
                val animation = if (playerOutput.contains(ATTACK.name)) ATTACK else DEFEND
                TurnOutcome(true, animation)
            } else {
                TurnOutcome(false, IDLE)
            }
        }

        public fun expectedAnswer(playerInput: String): String {
            val words = playerInput.split(" ")
            val intensity = words[0].toInt()
            val spell = if (words[1].toInt() > 100) DEFEND.name else ATTACK.name
            return spell.repeat(intensity)
        }
    }

    companion object {
        fun getLevelFromLeague(random: Random, league: Int): Level {
            return when (league) {
                1 -> Level1(random)
                2 -> Level2(random)
                3 -> Level3(random)
                else -> throw RuntimeException("Level for league: $league not implemented yet")
            }
        }
    }
}