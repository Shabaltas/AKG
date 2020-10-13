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
            gc.clearRect(0.0, 0.0, width, height)
            worldcords.translateVertices(doubleArrayOf(scrollEvent.deltaX, scrollEvent.deltaY, 0.0));
            pairSet.clear();
            worldcords.polygons.forEach{
                drawPolygon(it, worldcords.transformedVertices);
            }
        }
        canvas.setOnKeyPressed { keyEvent ->
            println("key")
            gc.clearRect(0.0, 0.0, width, height)
            when (keyEvent.code.code) {
                KeyEvent.VK_MINUS -> worldcords.scaleVertices(doubleArrayOf(-1.0, -1.0, 0.0));
                KeyEvent.VK_PLUS -> worldcords.scaleVertices(doubleArrayOf(1.0, 1.0, 0.0));
            }
            pairSet.clear();
            worldcords.polygons.forEach{
                drawPolygon(it, worldcords.transformedVertices);
            }
        }

    }

    private fun drawPolygon(polygon: Polygon, vertices: List<Vertice>) {
        for (i in 1 until polygon.size()) {
            val num1 = polygon.getVerticeNumber(i - 1) - 1
            val num2 = polygon.getVerticeNumber(i) - 1
            val pair = createPair(num1, num2, vertices)
            drawBrezenhem(pair)
        }
        val first = polygon.getVerticeNumber(0) - 1
        val last = polygon.getVerticeNumber(polygon.size() - 1) - 1
        val pair = createPair(first, last, vertices)
        drawBrezenhem(pair)
    }

    private fun createPair(num1: Int, num2: Int, vertices: List<Vertice>): Pair {
        val point1 = vertices[num1]
        val point2 = vertices[num2]
        return if (num1 < num2) Pair(point1, point2) else Pair(point2, point1)
    }

    private fun drawBrezenhem(pair: Pair) {
        if (pairSet.add(pair)) {
            drawBresenhamLine(round(pair.pos1.x), round(pair.pos1.y), round(pair.pos2.x), round(pair.pos2.y))
        }
    }

    private fun sign(x: Int): Int {
        return Integer.compare(x, 0)
        //возвращает 0, если аргумент (x) равен нулю; -1, если x < 0 и 1, если x > 0.
    }

    fun drawBresenhamLine(xstart: Int, ystart: Int, xend: Int, yend: Int)
            /**
             * xstart, ystart - начало;
             * xend, yend - конец;
             * "g.drawLine (x, y, x, y);" используем в качестве "setPixel (x, y);"
             * Можно писать что-нибудь вроде g.fillRect (x, y, 1, 1);
             */
    {
        //println("x: ${xend}, y: ${yend} ")
        if (xstart > width && xend > width || xstart < 0 && xend < 0 || ystart > height && yend > height || ystart < 0 && yend < 0) {
            return
        }
        var x: Int
        var y: Int
        var dx: Int
        var dy: Int
        val incx: Int
        val incy: Int
        val pdx: Int
        val pdy: Int
        val es: Int
        val el: Int
        var err: Int
        dx = xend - xstart //проекция на ось икс
        dy = yend - ystart //проекция на ось игрек
        incx = sign(dx)
        /*
         * Определяем, в какую сторону нужно будет сдвигаться. Если dx < 0, т.е. отрезок идёт
         * справа налево по иксу, то incx будет равен -1.
         * Это будет использоваться в цикле постороения.
         */incy = sign(dy)
        /*
         * Аналогично. Если рисуем отрезок снизу вверх -
         * это будет отрицательный сдвиг для y (иначе - положительный).
         */if (dx < 0) dx = -dx //далее мы будем сравнивать: "if (dx < dy)"
        if (dy < 0) dy = -dy //поэтому необходимо сделать dx = |dx|; dy = |dy|
        //эти две строчки можно записать и так: dx = Math.abs(dx); dy = Math.abs(dy);
        if (dx > dy) //определяем наклон отрезка:
        { /*
             * Если dx > dy, то значит отрезок "вытянут" вдоль оси икс, т.е. он скорее длинный, чем высокий.
             * Значит в цикле нужно будет идти по икс (строчка el = dx;), значит "протягивать" прямую по иксу
             * надо в соответствии с тем, слева направо и справа налево она идёт (pdx = incx;), при этом
             * по y сдвиг такой отсутствует.
             */
            pdx = incx
            pdy = 0
            es = dy
            el = dx
        } else  //случай, когда прямая скорее "высокая", чем длинная, т.е. вытянута по оси y
        {
            pdx = 0
            pdy = incy
            es = dx
            el = dy //тогда в цикле будем двигаться по y
        }
        x = xstart
        y = ystart
        err = el / 2
        drawPixel(x, y) //ставим первую точку
        //все последующие точки возможно надо сдвигать, поэтому первую ставим вне цикла
        for (t in 0 until el)  //идём по всем точкам, начиная со второй и до последней
        {
            err -= es
            if (err < 0) {
                err += el
                x += incx //сдвинуть прямую (сместить вверх или вниз, если цикл проходит по иксам)
                y += incy //или сместить влево-вправо, если цикл проходит по y
            } else {
                x += pdx //продолжить тянуть прямую дальше, т.е. сдвинуть влево или вправо, если
                y += pdy //цикл идёт по иксу; сдвинуть вверх или вниз, если по y
            }
            drawPixel(x, y)
        }
    }

    private fun drawPixel(x: Int, y: Int) {
        if (height < y || y < 0 || width < x || x < 0) {
            return
        }
        pw.setColor(x, y, Color.BLACK)
    }

    private fun round(x: Double): Int {
        return Math.round(x).toInt()
    }
}
