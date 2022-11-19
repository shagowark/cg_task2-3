package figures;

import screenWork.RealPoint;

public class Rect {
    private RealPoint baseCorner; //left, upper
    private double width;
    private double height;

    public Rect(RealPoint baseCorner, double width, double height) {
        this.baseCorner = baseCorner;
        this.width = width;
        this.height = height;
    }

    public RealPoint getBaseCorner() {
        return baseCorner;
    }

    public void setBaseCorner(RealPoint baseCorner) {
        this.baseCorner = baseCorner;
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

    public RealPoint getRightBotCorner(){
        return new RealPoint(baseCorner.getX() + width, baseCorner.getY() - height);
    }

    public RealPoint getLeftBotCorner(){
        return new RealPoint(baseCorner.getX(), baseCorner.getY() - height);
    }

    public RealPoint getRightUppCorner(){
        return new RealPoint(baseCorner.getX() + width, baseCorner.getY());
    }
}
