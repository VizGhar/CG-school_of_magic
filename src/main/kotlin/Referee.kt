package com.codingame.game

import com.codingame.game.ui.*
import com.codingame.gameengine.core.AbstractPlayer
import com.codingame.gameengine.core.AbstractReferee
import com.codingame.gameengine.core.MultiplayerGameManager
import com.codingame.gameengine.module.entities.GraphicEntityModule
import com.google.inject.Inject
import kotlin.random.Random

class Referee : AbstractReferee() {

    @Inject
    lateinit var gameManager: MultiplayerGameManager<Player>

    @Inject
    lateinit var graphicEntityModule: GraphicEntityModule

    private lateinit var currentLevel: Level

    override fun init() {
        gameManager.maxTurns = 10
        gameManager.firstTurnMaxTime = 1000
        gameManager.turnMaxTime = 50
        gameManager.frameDuration = 1000

        currentLevel = Level.getLevelFromLeague(Random(gameManager.seed), gameManager.leagueLevel)

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
                if (turnOutcome.isValid) player.score++
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

    private fun sendInputToPlayers(spell: String) {
        for (player in gameManager.activePlayers) {
            player.sendInputLine(spell)
            player.execute()
        }
    }

    private fun AbstractPlayer.output() = outputs.first()
}