package entity;

import java.util.*;

public class Polygon {
    private List<Integer> verticesNumbers = new ArrayList<>();
    private List<Vertice> vertices = new ArrayList<>();
    public double yMin, yMax;
    public Polygon(List<Vertice> vertices, List<Integer> numbers){
        for (int i: numbers) {
            this.vertices.add(vertices.get(i - 1));
        }
        this.verticesNumbers.addAll(numbers);
        setyMinMax();
    }

    public List<Integer> getVerticesNumbers() {
        return verticesNumbers;
    }

    public List<Vertice> getVertices() {
        return vertices;
    }

    public Vertice getVerticeByIndex(int index) {
        return vertices.get(index);
    }

    public Vertice getVerticeByNumber(int number) {
        int index = 0;
        for (; index < verticesNumbers.size(); index++) {
            if (verticesNumbers.get(index).compareTo(number) == 0) break;
        }
        return vertices.get(index);
    }

    public int getVerticeNumber(int index) {
        return verticesNumbers.get(index);
    }

    private void setyMinMax() {
        yMin = vertices.get(0).getY();
        yMax = yMin;
        double yTemp;
        for (int i = 1; i < vertices.size(); i++) {
            yTemp = vertices.get(i).getY();
            if (yMin > yTemp) yMin = yTemp;
            if (yMax < yTemp) yMax = yTemp;
        }
    }

    public int size(){
        return vertices.size();
    }
}
