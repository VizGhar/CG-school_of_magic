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

        gameManager.activePlayers.forEach { player ->
            try {
                if (currentLevel.isPlayerOutputValid(spell, player.output())) player.score++
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