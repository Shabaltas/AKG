package com.bsuir.util;

import entity.Pair;
import entity.Polygon;
import entity.Vertice;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class PairUtil {

    public static HashSet<Pair> getLines(Polygon polygon, List<Vertice> readyVertices) {
        HashSet<Pair> pairs = new HashSet<>();
        List<Vertice> sorted = new ArrayList<>();
        for (int i : polygon.getVerticesNumbers()) {
            sorted.add(readyVertices.get(i - 1));
        }
        int yMin = (int) Math.round(sorted.get(0).getY());
        int yMax = yMin;
        for (Vertice v : sorted) {
            if (v.getY() < yMin) yMin = (int)Math.round(v.getY());
            if (v.getY() > yMax) yMax = (int)Math.round(v.getY());
        }
        sorted.sort((v1, v2) -> (int) Math.round(v1.getY() - v2.getY()));

        for (int yTemp = yMin; yTemp <= yMax; yTemp++) {
            List<Integer> xx = new ArrayList<>();
            for (int i = 0; i < sorted.size() - 1; i++) {
                int x1 = (int)Math.round(sorted.get(i).getX());
                int y1 = (int)Math.round(sorted.get(i).getY());
                int x2 = (int)Math.round(sorted.get(i + 1).getX());
                int y2 = (int)Math.round(sorted.get(i + 1).getY());

                if ((y1 <= yTemp && y2 > yTemp) || (y1 > yTemp && y2 <= yTemp)) {
                    xx.add(x1 + (x2 - x1) * (yTemp - y1) / (y2 - y1));
                }
            }

            int x1 = (int)Math.round(sorted.get(0).getX());
            int y1 = (int)Math.round(sorted.get(0).getY());
            int x2 = (int)Math.round(sorted.get(sorted.size() - 1).getX());
            int y2 = (int)Math.round(sorted.get(sorted.size() - 1).getY());

            if ((y1 <= yTemp && y2 > yTemp) || (y1 > yTemp && y2 <= yTemp)) {
                xx.add(x1 + (x2 - x1) * (yTemp - y1) / (y2 - y1));
            }

            xx.sort(Integer::compareTo);
            if (xx.size() > 1)
                pairs.add(new Pair(new Vertice(xx.get(0), yTemp, 0, 0), new Vertice(xx.get(1), yTemp, 0, 0)));
        }
        return pairs;
    }
}
