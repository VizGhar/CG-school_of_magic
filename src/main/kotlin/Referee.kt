package com.codingame.game

import com.codingame.game.ui.initDraw
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

    lateinit var random: Random

    override fun init() {
        random = Random(gameManager.seed)
        gameManager.maxTurns = 10
        gameManager.firstTurnMaxTime = 1000
        gameManager.turnMaxTime = 50
        gameManager.frameDuration = 1000

        // draw screen
        initDraw()
    }

    override fun gameTurn(turn: Int) {
        val spell = random.nextLong(1000).toString()

        sendInputToPlayers(spell)

        handlePlayerOutput(spell)
    }

    private fun handlePlayerOutput(spell: String) {
        for (player in gameManager.activePlayers) {
            try {
                if (player.output() == spell) {
                    println("Player {${player.nicknameToken} scored!")
                    player.score++
                }
                // Check validity of the player output and compute the new game state
            } catch (e: AbstractPlayer.TimeoutException) {
                player.deactivate(String.format("$%d timeout!", player.index))
            }
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