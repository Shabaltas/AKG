package entity;

public class ScreenVertice {
    private int x, y, z;
    private int number;

    public ScreenVertice(int x, int y, int z, int number) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.number = number;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public int getNumber() {
        return number;
    }
}
