package com.codingame.game

import com.codingame.game.ui.*
import com.codingame.gameengine.core.AbstractPlayer
import com.codingame.gameengine.core.AbstractReferee
import com.codingame.gameengine.core.MultiplayerGameManager
import com.codingame.gameengine.module.endscreen.EndScreenModule
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.google.inject.Inject
import kotlin.random.Random

class Referee : AbstractReferee() {

    @Inject
    lateinit var gameManager: MultiplayerGameManager<Player>

    @Inject
    lateinit var graphicEntityModule: GraphicEntityModule

    @Inject
    lateinit var endScreenModule: EndScreenModule

    private lateinit var currentLevel: Level

    override fun init() {
        gameManager.maxTurns = MAX_TURNS
        gameManager.firstTurnMaxTime = FIRST_TURN_MAX_TIME
        gameManager.turnMaxTime = TURN_MAX_TIME
        gameManager.frameDuration = FRAME_DURATION

        currentLevel = Level.getLevelFromLeague(Random(gameManager.seed), gameManager.leagueLevel)

        gameManager.activePlayers.forEach { it.score = PLAYER_STARTING_SCORE }

        // draw screen
        initDraw()
        initHud()
    }

    override fun gameTurn(turn: Int) {
        val spell = currentLevel.generateTurnInput()

        sendInputToPlayers(spell)

        gameManager.activePlayers.forEachIndexed { index, player ->
            try {
                val turnOutcome = currentLevel.isPlayerOutputValid(spell, player.output())
                handleAnimation(index, turnOutcome)
                if (!turnOutcome.isValid) player.score--
            } catch (e: AbstractPlayer.TimeoutException) {
                player.deactivate(String.format("$%d timeout!", player.index))
            }
        }
    }

    private fun handleAnimation(playerIndex: Int, turnOutcome: Level.TurnOutcome) {
        val entity = if (playerIndex == 0) Entity.PLAYER_1 else Entity.PLAYER_2
        when (turnOutcome.animation) {
            Level.Animation.ATTACK -> attack(entity)
            Level.Animation.DEFEND -> shield(entity)
            Level.Animation.DIE -> println("Die animation missing")
            Level.Animation.IDLE -> println("Idle animation missing")
        }
    }

    override fun onEnd() {
        endScreenModule.setScores(gameManager.players.stream().mapToInt { p: Player -> p.score }.toArray())
    }

    private fun sendInputToPlayers(spell: String) {
        for (player in gameManager.activePlayers) {
            player.sendInputLine(spell)
            player.execute()
        }
    }

    private fun AbstractPlayer.output() = outputs.first()

    companion object {
        const val MAX_TURNS = 10
        const val FIRST_TURN_MAX_TIME = 1000
        const val TURN_MAX_TIME = 50
        const val FRAME_DURATION = 1000

        const val PLAYER_STARTING_SCORE = MAX_TURNS
    }
}