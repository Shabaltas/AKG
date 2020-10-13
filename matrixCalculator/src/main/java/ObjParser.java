
import entity.Polygon;
import entity.Vertice;
import org.apache.commons.math3.linear.ArrayRealVector;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class ObjParser {
    private List<Vertice> vertices = new ArrayList<Vertice>();
    private List<Polygon> polygons = new ArrayList<Polygon>();
    public void loadObjFile(String filename) throws IOException {
        File objFile = new File(filename);
        BufferedReader reader = new BufferedReader(new FileReader(objFile));
        String line;
        StringTokenizer lineTokenizer;
        int counter = 0;
        while ((line = reader.readLine()) != null) {
            if (!line.isEmpty()) {
                lineTokenizer = new StringTokenizer(line);
                String command = lineTokenizer.nextToken();
                if ("v".equals(command)) {
                    counter += 1;
                    vertices.add(new Vertice(
                            Float.parseFloat(lineTokenizer.nextToken()),
                            Float.parseFloat(lineTokenizer.nextToken()),
                            Float.parseFloat(lineTokenizer.nextToken()),
                            counter
                    ));
                }
                if ("f".equals(command)) {
                    String fToken = lineTokenizer.nextToken();
                    int v1 = Integer.parseInt(fToken.substring(0, fToken.indexOf("/")));
                    fToken = lineTokenizer.nextToken();
                    int v2 = Integer.parseInt(fToken.substring(0, fToken.indexOf("/")));
                    fToken = lineTokenizer.nextToken();
                    int v3 = Integer.parseInt(fToken.substring(0, fToken.indexOf("/")));
                    polygons.add(new Polygon(v1, v2, v3));
                }
            }
        }
    }

    public static WorldCord getWorldCoordsFromFile(double viewWidth, double viewHeght) throws IOException {
        ObjParser op = new ObjParser();
        ClassLoader classLoader = ObjParser.class.getClassLoader();
        op.loadObjFile(classLoader.getResource("head.obj").getFile());
        // call in the reverse order !!!!
        //TODO need to turnZ on 60 -> turnY on 45 -> turnX on 30 -> scale -> transform
        WorldCord wct = new WorldCord(op.vertices, op.polygons, viewWidth, viewHeght);
        return wct.createProjection(1, 10)
                .createView(new ArrayRealVector(new double[]{0, 0, 3}, false),
                        new ArrayRealVector(new double[]{0, 0, 0}, false),
                        new ArrayRealVector(new double[]{0, 1, 0}, false))
                .createViewPort(0, 0)
                .allChangesMatrix();
    }
}
