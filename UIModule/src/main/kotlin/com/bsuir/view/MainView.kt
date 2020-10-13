package com.bsuir.view

//import ObjParser.getWorldCoordsFromFile
//import WorldCord
import ObjParser.getWorldCoordsFromFile
import WorldCord
import entity.Pair
import entity.Polygon
import entity.Vertice
import javafx.scene.canvas.Canvas
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
    val canvas: Canvas by fxid()
    val gc = canvas.graphicsContext2D
    val pw = gc.pixelWriter
    val worldcords: WorldCord
    val width = canvas.width
    val height = canvas.height
    private val pairSet: HashSet<Pair> = HashSet()
    init {
        worldcords = getWorldCoordsFromFile(canvas.width, canvas.height)
        println(worldcords)
        worldcords.lastTransform();
        pairSet.clear();
        worldcords.polygons.forEach{
            drawPolygon(it, worldcords.transformedVertices);
        }
        //canvas.addEventFilter(KeyEvent.KEY_PRESSED) { event -> println("pressed:" + event) }
        canvas.setOnScroll { scrollEvent ->
            worldcords.translateVertices(doubleArrayOf(scrollEvent.deltaX, scrollEvent.deltaY, 0.0));
            redrawPolygons();
        }
        canvas.setOnKeyPressed { keyEvent ->
            println("key")
            when (keyEvent.code.code) {
                KeyEvent.VK_MINUS -> worldcords.scaleVertices(doubleArrayOf(-1.0, -1.0, 0.0));
                KeyEvent.VK_PLUS -> worldcords.scaleVertices(doubleArrayOf(1.0, 1.0, 0.0));
            }
            redrawPolygons();
        }

    }

    private fun redrawPolygons() {
        gc.clearRect(0.0, 0.0, width, height)
        pairSet.clear();
        worldcords.polygons.forEach{
            drawPolygon(it, worldcords.transformedVertices);
        }
    }

}
