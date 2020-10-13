package entity;

public class Vertice {
    private double x, y, z, w;
    private int number;

    public Vertice(double x, double y, double z, int number) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
        this.number = number;
    }

    public Vertice(double x, double y, double z, double w, int number) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        this.number = number;
    }

    public double[] getVector() {
        return new double[] {x, y, z, w};
    }
    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    public double getW() {
        return w;
    }

    public void setW(double w) {
        this.w = w;
    }

    public void setX(double x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    public void setZ(double z) {
        this.z = z;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
