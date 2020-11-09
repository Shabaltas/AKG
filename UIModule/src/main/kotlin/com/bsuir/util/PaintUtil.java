package com.bsuir.util;

import entity.Pair;
import entity.Polygon;
import entity.ScreenVertice;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

public class PaintUtil {

    public static HashSet<Pair> getLines(Polygon polygon, List<ScreenVertice> readyVertices) {
        HashSet<Pair> pairs = new HashSet<>();
        List<ScreenVertice> sorted = new ArrayList<>();
        for (int i : polygon.getVerticesNumbers()) {
            sorted.add(readyVertices.get(i - 1));
        }
        int yMin = sorted.get(0).getY();
        int yMax = yMin;
        for (ScreenVertice v : sorted) {
            if (v.getY() < yMin) yMin = v.getY();
            if (v.getY() > yMax) yMax = v.getY();
        }
        sorted.sort(Comparator.comparingInt(ScreenVertice::getY));

        for (int yTemp = yMin; yTemp <= yMax; yTemp++) {
            List<Integer> xx = new ArrayList<>();
            for (int i = 0; i < sorted.size() - 1; i++) {
               interception(sorted.get(i), sorted.get(i+1), yTemp, xx);
            }
            interception(sorted.get(0), sorted.get(sorted.size()-1), yTemp, xx);
            xx.sort(Integer::compareTo);
            if (xx.size() > 1)
                pairs.add(new Pair(
                        new ScreenVertice(xx.get(0), yTemp, 0, 0),
                        new ScreenVertice(xx.get(1), yTemp, 0, 0)));
        }
        return pairs;
    }

    private static void interception(ScreenVertice v1, ScreenVertice v2, int yTemp, List<Integer> xx) {
        int x1 = v1.getX();
        int y1 = v1.getY();
        int x2 = v2.getX();
        int y2 = v2.getY();

        if ((y1 <= yTemp && y2 > yTemp) || (y1 > yTemp && y2 <= yTemp)) {
            xx.add(x1 + (x2 - x1) * (yTemp - y1) / (y2 - y1));
        }
    }
}
