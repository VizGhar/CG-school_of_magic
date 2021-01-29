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
        private val attack = "ATTACK"
        private val defend = "DEFEND"
        override fun generateTurnInput(): String = random.nextInt(200).toString()
        override fun isPlayerOutputValid(playerInput: String, playerOutput: String): TurnOutcome {
            return try {
                val answer = if (playerInput.toInt() > 100) defend else attack
                val valid = playerOutput == answer
                TurnOutcome(valid, Animation.values().first { it.name == answer })
            } catch (numberFormatException: NumberFormatException) {
                TurnOutcome(false, IDLE)
            }
        }
    }

    class Level3(random: Random) : Level(random) {
        override fun generateTurnInput(): String = TODO("Level 3 generateTurnInput not yet implemented")
        override fun isPlayerOutputValid(playerInput: String, playerOutput: String): TurnOutcome {
            TODO("Level 3 isPlayerOutputValid not yet implemented")
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