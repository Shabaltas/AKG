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
    private val angleStep = 0.1;
    private val step = 0.02;
    private val drawUtils: DrawUtil
    init {
        drawUtils = DrawUtil(width, height, canvas);
        worldcords = getWorldCoordsFromFile(width, height)
        worldcords.polygons.forEach{
            drawUtils.drawPolygon(it, worldcords.readyVertices);
        }
        canvas.setOnScroll { scrollEvent ->
            //TODO whats wrong with X
            worldcords.translateVertices(doubleArrayOf(0.1 * sign(scrollEvent.deltaX) , -0.1 * sign(scrollEvent.deltaY), 0.0));
            drawUtils.redrawPolygons(worldcords.lastTransform())
        }
        canvas.setOnKeyPressed { keyEvent ->
            println("key: ${keyEvent.code}")
            when (keyEvent.code)  {
                KeyCode.MINUS -> {
                    worldcords.scaleVertices(doubleArrayOf(0.8, 0.8, 0.8))
                }

                //object
                KeyCode.EQUALS , KeyCode.PLUS -> {
                    worldcords.scaleVertices(doubleArrayOf(1.2, 1.2, 1.2))
                }
                KeyCode.RIGHT -> {
                    if (keyEvent.isAltDown) {
                        worldcords.turnWorldYVertices(angleStep)
                    };
                    else worldcords.turnYVertices(angleStep)
                }
                KeyCode.LEFT -> {
                    if (keyEvent.isAltDown) {
                        worldcords.turnWorldYVertices(-angleStep)
                    };
                    else worldcords.turnYVertices(-angleStep)
                }
                KeyCode.UP -> {
                   // if (keyEvent.isAltDown) worldcords.turnXVertices(angleStep);
                    //else
                    worldcords.turnXVertices(-angleStep)
                }
                KeyCode.DOWN -> {
                    //if (keyEvent.isAltDown) worldcords.turnXVertices(-angleStep);
                    //else
                    // worldcords.turnXVertices(angleStep)
                }

                //camera
                /*KeyCode.A -> {
                    worldcords.translateVerticesCamera(doubleArrayOf(-step, 0.0, 0.0))//сместить центр
                    //worldcords.translateVerticesCamera(doubleArrayOf(step, 0.0, 0.0)) //TODO whats wrong with X
                }*/
               /* KeyCode.D -> {
                    worldcords.translateVertices(doubleArrayOf(step, 0.0, 0.0))
                }
                KeyCode.W -> {
                    worldcords.translateVertices(doubleArrayOf(0.0, step, 0.0))
                }
                KeyCode.S -> {
                    worldcords.translateVertices(doubleArrayOf(0.0, -step, 0.0))
                }*/

            }
            drawUtils.redrawPolygons(worldcords.lastTransform())
        }
    }
}
