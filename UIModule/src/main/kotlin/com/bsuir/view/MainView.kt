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
        worldcords.lastTransform()
        worldcords.polygons.forEach{
            drawUtils.drawPolygon(it, worldcords.transformedVertices);
        }
        canvas.setOnScroll { scrollEvent ->
            worldcords.translateVertices(doubleArrayOf(scrollEvent.deltaX, scrollEvent.deltaY, 0.0));
            drawUtils.redrawPolygons(worldcords)
        }
        canvas.setOnKeyPressed { keyEvent ->
            println("key: ${keyEvent.code}")
            when (keyEvent.code)  {
                KeyCode.DOWN -> {
                    worldcords.scaleVertices(doubleArrayOf(0.5, 0.5, 0.0))
                }
                KeyCode.UP -> {
                    worldcords.scaleVertices(doubleArrayOf(1.5, 1.5, 0.0))
                }
                KeyCode.RIGHT -> {
//                    worldcords.turnZVertices(Math.toRadians(5.0))
                    worldcords.turnYVertices(Math.toRadians(10.0))
//                    worldcords.turnXVertices(Math.toRadians(5.0))
//                    worldcords.turnYVertices(0.2)
                }
                KeyCode.LEFT -> {
                    worldcords.turnXVertices(-0.2)
                    worldcords.turnYVertices(-0.2)
                }

            }
            drawUtils.redrawPolygons(worldcords)
        }
//        canvas.setOnMouseDragged {
//            worldcords.turnXVertices(it.sceneX)
//            worldcords.turnYVertices(it.screenY)
//            drawUtils.redrawPolygons(worldcords)
//            println("Drag: ${it.x}, ${it.y}, ${it.z}")
//        }

    }
}
