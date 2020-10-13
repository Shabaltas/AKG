package com.bsuir.view

//import ObjParser.getWorldCoordsFromFile
//import WorldCord
import ObjParser.getWorldCoordsFromFile
import WorldCord
import com.bsuir.util.DrawUtil
import entity.Pair
import entity.Polygon
import entity.Vertice
import javafx.scene.canvas.Canvas
import javafx.scene.input.KeyCode
import javafx.scene.layout.BorderPane
import javafx.scene.paint.Color
import tornadofx.View
import java.awt.event.KeyEvent


//import org.apache.c
//import java.io.File
//import org.apache.commons.math3.*
//import org.apache.commons.math3.linear.RealMatrix

class MainView : View() {
    override val root: BorderPane by fxml("/view/MainView.fxml")
    private val canvas: Canvas by fxid()

    private val worldcords: WorldCord
    private val width = canvas.width
    private val height = canvas.height
    //private val pairSet: HashSet<Pair> = HashSet()
    private val drawUtils: DrawUtil
    init {
        drawUtils = DrawUtil(width, height, canvas);
        worldcords = getWorldCoordsFromFile(canvas.width, canvas.height)
        println(worldcords)
        worldcords.lastTransform()
        worldcords.polygons.forEach{
            drawUtils.drawPolygon(it, worldcords.transformedVertices);
        }
        //canvas.addEventFilter(KeyEvent.KEY_PRESSED) { event -> println("pressed:" + event) }
        canvas.setOnScroll { scrollEvent ->
            worldcords.translateVertices(doubleArrayOf(scrollEvent.deltaX, scrollEvent.deltaY, 0.0));
            drawUtils.redrawPolygons(worldcords)
        }
        canvas.setOnKeyPressed { keyEvent ->
            println("key")
            when (keyEvent.code) {
                KeyCode.MINUS -> {
                    worldcords.scaleVertices(doubleArrayOf(-1.0, -1.0, 0.0))
                }
                KeyCode.PLUS -> {

                    worldcords.scaleVertices(doubleArrayOf(1.0, 1.0, 0.0))
                }
            }
            drawUtils.redrawPolygons(worldcords)
        }

    }



}
