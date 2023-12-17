package Esteganografia;

public class PixelRGB {
    private int r;
    private int g;
    private int b;

    public PixelRGB(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public static PixelRGB blanco() {
        return new PixelRGB(255, 255, 255);
    }

    public static PixelRGB negro() {
        return new PixelRGB(0, 0, 0);
    }

    public int getR() {
        return r;
    }

    public int getG() {
        return g;
    }

    public int getB() {
        return b;
    }

    public String toString() {
        return ("<" + r + "," + g + "," + b);
    }
}
