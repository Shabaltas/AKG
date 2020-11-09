package com.bsuir.view

//import ObjParser.getWorldCoordsFromFile
//import WorldCord
import ObjParser.getWorldCoordsFromFile
import WorldCord
import com.bsuir.util.DrawUtil
import com.bsuir.util.PairUtil
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
    private val step = 0.1;
    private val drawUtils: DrawUtil
    init {
        drawUtils = DrawUtil(width, height, canvas);
        worldcords = getWorldCoordsFromFile(width, height)
        worldcords.polygons.forEach{
            val intens = worldcords.needToDraw(it);
            if (intens >= 0.0) {
                drawUtils.drawPolygon(it, worldcords.readyVertices, intens);
                PairUtil.getLines(it, worldcords.readyVertices).forEach { pair -> drawUtils.drawBrezenhem(pair, intens) };
            }
        }

        canvas.setOnScroll { scrollEvent ->
            //TODO whats wrong with X
            worldcords.translateVertices(doubleArrayOf(-0.1 * sign(scrollEvent.deltaX) , 0.1 * sign(scrollEvent.deltaY), 0.0));
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
                   worldcords.turnYVertices(angleStep)
                }
                KeyCode.LEFT -> {
                    worldcords.turnYVertices(-angleStep)
                }
                KeyCode.UP -> {
                    worldcords.turnXVertices(-angleStep)
                }
                KeyCode.DOWN -> {
                     worldcords.turnXVertices(angleStep)
                }

                //camera
                KeyCode.A -> {
                    worldcords.changeCameraHorizontally(-angleStep)//сместить центр
                    worldcords.allChangesMatrix()
                }
                KeyCode.D -> {
                    worldcords.changeCameraHorizontally(angleStep)//сместить центр
                    worldcords.allChangesMatrix()
                }
                KeyCode.W -> {
                    worldcords.changeCameraVertically(angleStep)//сместить центр
                    worldcords.allChangesMatrix()
                }
                KeyCode.S -> {
                    worldcords.changeCameraVertically(-angleStep)//сместить центр
                    worldcords.allChangesMatrix()
                }

            }
            drawUtils.redrawPolygons(worldcords.lastTransform())
        }
    }
}
