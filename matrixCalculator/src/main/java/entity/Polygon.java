package entity;

import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<Integer> verticesNumbers = new ArrayList<>();
    public Polygon(int... v){
        for (int value : v) this.verticesNumbers.add(value);
    }

    public int getVerticeNumber(int number) {
        if (number < 0 || number >= verticesNumbers.size())
            throw new IllegalArgumentException("entity.Polygon has " + verticesNumbers.size() + " vertices!");
        return verticesNumbers.get(number);
    }

    public int size(){
        return verticesNumbers.size();
    }
}
