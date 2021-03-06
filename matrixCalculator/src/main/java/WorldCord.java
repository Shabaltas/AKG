import entity.Polygon;
import entity.Vertice;
import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.linear.RealVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WorldCord {

    private RealMatrix translateMatrix = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}});
    private RealMatrix scaleMatrix = MatrixUtils.createRealDiagonalMatrix(new double[]{0, 0, 0, 1});
    private RealMatrix turnXMatrix = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}});
    private RealMatrix turnYMatrix = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}});
    private RealMatrix turnZMatrix = MatrixUtils.createRealMatrix(new double[][]{
            {1, 0, 0, 0},
            {0, 1, 0, 0},
            {0, 0, 1, 0},
            {0, 0, 0, 1}});
    private RealMatrix viewport, view, projection, all;

    private List<Vertice> readyVertices;
    private List<Polygon> polygons;

    public List<Polygon> getPolygons() {
        return polygons;
    }

    private List<Vertice> transformedVertices;
    private RealVector eye, target, up;
    private double width, height;

    public WorldCord(List<Vertice> vertices,List<Polygon> polygons, double viewWidth, double viewHeight) {
        transformedVertices = new ArrayList<>(vertices.size());
        readyVertices = new ArrayList<>(vertices.size());
        vertices.forEach(vertice -> {
            transformedVertices.add(new Vertice(vertice.getX(), vertice.getY(), vertice.getZ(), vertice.getW(), vertice.getNumber()));
        });
        width = viewWidth;
        height = viewHeight;
        this.polygons = polygons;
    }

    public List<Vertice> getReadyVertices() {
        return readyVertices;
    }

    public List<Vertice> getTransformedVertices() {
        return transformedVertices;
    }

    public RealMatrix getProjection() {
        return projection;
    }

    public RealMatrix getViewport() {
        return viewport;
    }

    public RealMatrix getView() {
        return view;
    }

    public WorldCord translateVertices(double[] translation) {
        //translateMatrix.setColumn(3, new double[]{translation[0], translation[1], translation[2], 1});
        changeView(translation);
        allChangesMatrix();
        //transform(translateMatrix);
        return this;
    }

    public WorldCord scaleVertices(double[] scale) {
        for (int i = 0; i < scale.length; i++) {
            scaleMatrix.setEntry(i, i, scale[i]);
        }
        transform(scaleMatrix);
        return this;
    }

    private void transform(RealMatrix matrix) {
        transformedVertices.forEach(vertice -> {
            double[] newCord = matrix.operate(vertice.getVector());
            vertice.setX(newCord[0]/newCord[3]);
            vertice.setY(newCord[1]/newCord[3]);
            vertice.setZ(newCord[2]/newCord[3]);
            vertice.setW(newCord[3]/newCord[3]);
        });
    }

    public WorldCord turnXVertices(double xAngleRadian) {
        turnXMatrix.setEntry(1, 1, Math.cos(xAngleRadian));
        turnXMatrix.setEntry(1, 2, -Math.sin(xAngleRadian));
        turnXMatrix.setEntry(2, 2, Math.cos(xAngleRadian));
        turnXMatrix.setEntry(2, 1, Math.sin(xAngleRadian));
        transform(turnXMatrix);
        return this;
    }

    public WorldCord turnYVertices(double yAngleRadian) {
        turnYMatrix.setEntry(0,0, Math.cos(yAngleRadian));
        turnYMatrix.setEntry(0, 2, Math.sin(yAngleRadian));
        turnYMatrix.setEntry(2, 0, -Math.sin(yAngleRadian));
        turnYMatrix.setEntry(2, 2, Math.cos(yAngleRadian));
//        all = all.multiply(turnYMatrix);
        //multiplyMatrix(turnYMatrix);
        transform(turnYMatrix);
        return this;
    }

    public WorldCord turnZVertices(double zAngleRadian) {
        turnZMatrix.setEntry(0,0, Math.cos(zAngleRadian));
        turnZMatrix.setEntry(0, 1, -Math.sin(zAngleRadian));
        turnZMatrix.setEntry(1, 0, Math.sin(zAngleRadian));
        turnZMatrix.setEntry(1, 1, Math.cos(zAngleRadian));
        transform(turnZMatrix);
        return this;
    }

    public WorldCord createViewPort(double xMin, double yMin) {
        viewport = MatrixUtils.createRealMatrix(new double[][]{
                {width / 2, 0, 0, xMin + width / 2},
                {0, -height / 2, 0, yMin + height / 2},
                {0, 0, 1, 0},
                {0, 0, 0, 1}
        });
        return this;
    }

    public WorldCord createView(RealVector eye, RealVector target, RealVector up) {
        this.eye = eye;
        this.target = target;
        this.up = up;
        updateViewMatrix(eye, target, up);
        return this;
    }

    private void changeView(double[] d) {
        eye.setEntry(0, eye.getEntry(0) + d[0]);
        eye.setEntry(1, eye.getEntry(1) + d[1]);
        eye.setEntry(2, eye.getEntry(2) + d[2]);

        target.setEntry(0, target.getEntry(0) + d[0]);
        target.setEntry(1, target.getEntry(1) + d[1]);
        target.setEntry(2, target.getEntry(2) + d[2]);

        updateViewMatrix(eye, target, up);
    }

    private void updateViewMatrix(RealVector eye, RealVector target, RealVector up) {
        RealVector ZAxis = eye.subtract(target).unitVector();
        RealVector XAxis = cross(up, ZAxis).unitVector();
        RealVector YAxis = up;//cross(ZAxis, XAxis).unitVector();
        view = MatrixUtils.createRealMatrix(new double[][]{
                {XAxis.getEntry(0), XAxis.getEntry(1), XAxis.getEntry(2), -scalarMultiply(XAxis, eye)},
                {YAxis.getEntry(0), YAxis.getEntry(1), YAxis.getEntry(2), -scalarMultiply(YAxis, eye)},
                {ZAxis.getEntry(0), ZAxis.getEntry(1), ZAxis.getEntry(2), -scalarMultiply(ZAxis, eye)},
                {0, 0, 0, 1}
        });
    }
    public WorldCord createProjection(double zNear, double zfar) {
        projection = MatrixUtils.createRealMatrix(new double[][]{
                /*{2 * zNear / width, 0, 0, 0},
                {0, 2 * zNear / height, 0, 0},*/
                {1 / (width / height * Math.tan((Math.PI/ 2) / 2)), 0, 0, 0},
                {0, 1 / Math.tan((Math.PI / 2) / 2), 0, 0},
                {0, 0, zfar / (zNear - zfar), zNear * zfar / (zNear - zfar)},
                {0, 0, -1, 0}
        });
        return this;
    }

    public WorldCord allChangesMatrix() {
        all = viewport.multiply(projection).multiply(view);
        return this;
    }

    public WorldCord lastTransform() {
        readyVertices.clear();
        transformedVertices.forEach(vertice -> {
            double[] newCord = all.operate(vertice.getVector());
            readyVertices.add(new Vertice(
                    newCord[0]/newCord[3],
                    newCord[1]/newCord[3],
                    newCord[2]/newCord[3],
                    newCord[3]/newCord[3],
                    vertice.getNumber()));
        });
        return this;
    }
    private double scalarMultiply(RealVector v1, RealVector v2) {
        return v1.getEntry(0) * v2.getEntry(0)
                + v1.getEntry(1) * v2.getEntry(1)
                + v1.getEntry(2) * v2.getEntry(2);
    }
    public static RealVector cross(RealVector vector1, RealVector vector2) {
        double x = vector1.getEntry(1) * vector2.getEntry(2) - vector1.getEntry(2) * vector2.getEntry(1);
        double y = vector1.getEntry(2) * vector2.getEntry(0) - vector1.getEntry(0) * vector2.getEntry(2);
        double z = vector1.getEntry(0) * vector2.getEntry(1) - vector1.getEntry(1) * vector2.getEntry(0);
        return new ArrayRealVector(new double[]{ x, y, z}, false);
    }
}
