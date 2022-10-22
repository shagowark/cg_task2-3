package figures;

import screenWork.RealPoint;

public class Rect {
    private RealPoint corner; //left, upper
    private double width;
    private double height;

    public Rect(RealPoint corner, double width, double height) {
        this.corner = corner;
        this.width = width;
        this.height = height;
    }

    public RealPoint getCorner() {
        return corner;
    }

    public void setCorner(RealPoint corner) {
        this.corner = corner;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public double getHeight() {
        return height;
    }

    public void setHeight(double height) {
        this.height = height;
    }
}
