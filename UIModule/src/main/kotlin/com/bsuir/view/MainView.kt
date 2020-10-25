package com.bsuir.view

//import ObjParser.getWorldCoordsFromFile
//import WorldCord
import ObjParser.getWorldCoordsFromFile
import WorldCord
import com.bsuir.util.DrawUtil
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import tornadofx.View
import kotlin.math.sign

class MainView : View() {
    override val root: BorderPane by fxml("/view/MainView.fxml")
    private val canvas: Canvas by fxid()

    private val worldcords: WorldCord
    private val width = canvas.width
    private val height = canvas.height
    private val drawUtils: DrawUtil
    init {
        drawUtils = DrawUtil(width, height, canvas);
        worldcords = getWorldCoordsFromFile(canvas.width, canvas.height)
        worldcords.polygons.forEach{
            drawUtils.drawPolygon(it, worldcords.readyVertices);
        }
        canvas.setOnScroll { scrollEvent ->
            worldcords.translateVertices(doubleArrayOf(-0.1 * sign(scrollEvent.deltaX) , 0.1 * sign(scrollEvent.deltaY), 0.0));
            drawUtils.redrawPolygons(worldcords.lastTransform())
        }
        canvas.setOnKeyPressed { keyEvent ->
            println("key: ${keyEvent.code}")
            when (keyEvent.code)  {
                KeyCode.MINUS -> {
                    worldcords.scaleVertices(doubleArrayOf(0.8, 0.8, 0.8))
                }
                KeyCode.EQUALS  -> {
                    worldcords.scaleVertices(doubleArrayOf(1.2, 1.2, 1.2))
                }
                KeyCode.PLUS  -> {
                    worldcords.scaleVertices(doubleArrayOf(1.2, 1.2, 1.2))
                }
                KeyCode.RIGHT -> {
                    worldcords.turnYVertices(0.2)
                }
                KeyCode.LEFT -> {
                    worldcords.turnYVertices(-0.2)
                }
                KeyCode.UP -> {
                    worldcords.turnXVertices(-0.2)
                }
                KeyCode.DOWN -> {
                    worldcords.turnXVertices(0.2)
                }
                KeyCode.A -> {
                    worldcords.turnZVertices(-0.2)
                }
                KeyCode.D -> {
                    worldcords.turnZVertices(0.2)
                }
            }
            drawUtils.redrawPolygons(worldcords.lastTransform())
        }
    }
}
