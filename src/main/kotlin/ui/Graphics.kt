package com.codingame.game.ui

import com.codingame.game.Referee

private const val BACKGROUND_Z = 0

open class Wizard(val idle: Array<String>)

object Wizard1 : Wizard(
        idle = arrayOf(
                "1_IDLE_000.png",
                "1_IDLE_001.png",
                "1_IDLE_002.png",
                "1_IDLE_003.png",
                "1_IDLE_004.png"
        ))

fun Referee.initDraw() {
    graphicEntityModule.createSprite()
            .setImage("background.jpg")
            .setBaseWidth(graphicEntityModule.world.width)
            .setBaseHeight(graphicEntityModule.world.height)
            .setZIndex(BACKGROUND_Z)

    graphicEntityModule.createSpriteAnimation()
            .setImages(*Wizard1.idle)
            .setX(200)
            .setY(800)
            .setDuration(400)
            .setLoop(true)
            .setPlaying(true)
}