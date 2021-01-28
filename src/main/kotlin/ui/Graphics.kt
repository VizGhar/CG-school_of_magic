package com.codingame.game.ui

import com.codingame.game.Referee
import com.codingame.gameengine.module.entities.Rectangle
import com.codingame.gameengine.module.entities.SpriteAnimation

private const val HEALTH_BAR_WIDTH = 486
private const val HEALTH_BAR_HEIGHT = 197

private const val BACKGROUND_Z = 0
private const val HUD_BACKGROUND_Z = 1
private const val HUD_HEALTH_BAR_Z = 2
private const val HUD_HEALTH_BAR_STATUS_Z = 3
private const val HUD_HEALTH_BAR_FOREGROUND_Z = 4

private const val WIZARD_1_Z = 10
private const val WIZARD_2_Z = 20
private const val WIZARD_3_Z = 30
private const val FORCE_FIELD_1_Z = 11
private const val FORCE_FIELD_2_Z = 21

private val shieldImages = arrayOf(
        "forcefield_0.png",
        "forcefield_1.png",
        "forcefield_2.png",
        "forcefield_3.png"
)

open class Wizard(private val prefix: String) {
    val idle = (0..4).map { "${prefix}_IDLE_00$it.png" }.toTypedArray()
    val attack = (0..4).map { "${prefix}_ATTACK_00$it.png" }.toTypedArray()
}

object Wizard1 : Wizard("1")
object Wizard2 : Wizard("2")
object Wizard3 : Wizard("3")

private var player1: SpriteAnimation? = null
private var player2: SpriteAnimation? = null
private var enemy: SpriteAnimation? = null
private var shieldPlayer1: SpriteAnimation? = null
private var shieldPlayer2: SpriteAnimation? = null
private var hitPointsRectanglePlayer1: Rectangle? = null
private var hitPointsRectanglePlayer2: Rectangle? = null

enum class Entity { PLAYER_1, PLAYER_2, ENEMY }

fun Referee.attack(entity: Entity) {
    val (sprite, entity) = when (entity) {
        Entity.PLAYER_1 -> player1 to Wizard1
        Entity.PLAYER_2 -> player2 to Wizard2
        Entity.ENEMY -> enemy to Wizard3
    }

    if (sprite == null) throw IllegalStateException("Did you forget to call initDraw() ?")

    sprite.setImages(*entity.attack)
    graphicEntityModule.commitEntityState(0.0, sprite)
    sprite.setImages(*entity.idle).setLoop(true)
    graphicEntityModule.commitEntityState(0.5, sprite)
}

fun Referee.hit(entity: Entity) {  }

fun Referee.shield(entity: Entity) {
    val sprite = when (entity) {
        Entity.PLAYER_1 -> shieldPlayer1
        Entity.PLAYER_2 -> shieldPlayer2
        else -> throw IllegalStateException("Shield only supported for player_1 and player_2")
    } ?: throw IllegalStateException("Did you forget to call initDraw() ?")

    sprite.setPlaying(true)
            .setVisible(true)
    graphicEntityModule.commitEntityState(0.0, sprite)
    sprite.setPlaying(false)
            .setVisible(false)
    graphicEntityModule.commitEntityState(0.5, sprite)
}

fun Referee.initDraw() {
    graphicEntityModule.createSprite()
            .setImage("background_2.jpg")
            .setBaseWidth(graphicEntityModule.world.width)
            .setBaseHeight(graphicEntityModule.world.height)
            .setZIndex(BACKGROUND_Z)

    player1 = graphicEntityModule.createSpriteAnimation()
            .setImages(*Wizard1.idle)
            .setX(350)
            .setY(700)
            .setDuration(500)
            .setLoop(true)
            .setPlaying(true)
            .setZIndex(WIZARD_1_Z)

    shieldPlayer1 = graphicEntityModule.createSpriteAnimation()
            .setScale(0.25)
            .setImages(*shieldImages)
            .setX(350)
            .setY(700)
            .setDuration(500)
            .setLoop(true)
            .setPlaying(false)
            .setVisible(false)
            .setZIndex(FORCE_FIELD_1_Z)

    player2 = graphicEntityModule.createSpriteAnimation()
            .setImages(*Wizard2.idle)
            .setX(250)
            .setY(800)
            .setDuration(500)
            .setLoop(true)
            .setPlaying(true)
            .setZIndex(WIZARD_2_Z)

    shieldPlayer2 = graphicEntityModule.createSpriteAnimation()
            .setImages(*shieldImages)
            .setScale(0.25)
            .setX(250)
            .setY(800)
            .setDuration(500)
            .setLoop(true)
            .setPlaying(false)
            .setVisible(false)
            .setZIndex(FORCE_FIELD_2_Z)

    enemy = graphicEntityModule.createSpriteAnimation()
            .setImages(*Wizard3.idle)
            .setX(1000)
            .setY(600)
            .setDuration(500)
            .setLoop(true)
            .setPlaying(true)
            .setZIndex(WIZARD_3_Z)
}

fun Referee.initHud() {
    graphicEntityModule.createSprite()
            .setImage("1_hp_background.png")
            .setBaseWidth(HEALTH_BAR_WIDTH)
            .setBaseHeight(HEALTH_BAR_HEIGHT)
            .setX(40)
            .setY(40)
            .setAnchorX(0.0)
            .setAnchorY(0.0)
            .setZIndex(HUD_BACKGROUND_Z)

    graphicEntityModule.createSprite()
            .setImage("2_hp_background.png")
            .setBaseWidth(HEALTH_BAR_WIDTH)
            .setBaseHeight(HEALTH_BAR_HEIGHT)
            .setX(40)
            .setY(240)
            .setAnchorX(0.0)
            .setAnchorY(0.0)
            .setZIndex(HUD_BACKGROUND_Z)

    graphicEntityModule.createSprite()
            .setImage("1_hp_bar.png")
            .setBaseWidth(HEALTH_BAR_WIDTH)
            .setBaseHeight(HEALTH_BAR_HEIGHT)
            .setX(40)
            .setY(40)
            .setAnchorX(0.0)
            .setAnchorY(0.0)
            .setZIndex(HUD_HEALTH_BAR_Z)

    graphicEntityModule.createSprite()
            .setImage("2_hp_bar.png")
            .setBaseWidth(HEALTH_BAR_WIDTH)
            .setBaseHeight(HEALTH_BAR_HEIGHT)
            .setX(40)
            .setY(240)
            .setAnchorX(0.0)
            .setAnchorY(0.0)
            .setZIndex(HUD_HEALTH_BAR_Z)

    graphicEntityModule.createSprite()
            .setImage("1_hp_foreground.png")
            .setBaseWidth(HEALTH_BAR_WIDTH)
            .setBaseHeight(HEALTH_BAR_HEIGHT)
            .setX(40)
            .setY(40)
            .setAnchorX(0.0)
            .setAnchorY(0.0)
            .setZIndex(HUD_HEALTH_BAR_FOREGROUND_Z)

    graphicEntityModule.createSprite()
            .setImage("2_hp_foreground.png")
            .setBaseWidth(HEALTH_BAR_WIDTH)
            .setBaseHeight(HEALTH_BAR_HEIGHT)
            .setX(40)
            .setY(240)
            .setAnchorX(0.0)
            .setAnchorY(0.0)
            .setZIndex(HUD_HEALTH_BAR_FOREGROUND_Z)

    val name1 = gameManager.players[0].nicknameToken
    val name2 = gameManager.players[1].nicknameToken

    graphicEntityModule.createText(name1)
            .setX(230)
            .setY(50)
            .setZIndex(20)
            .setFontSize(40)
            .setFillColor(0xffffff)

    graphicEntityModule.createText(name2)
            .setX(230)
            .setY(250)
            .setZIndex(20)
            .setFontSize(40)
            .setFillColor(0xffffff)
}