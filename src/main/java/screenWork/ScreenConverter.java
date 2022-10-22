package screenWork;

public class ScreenConverter {
    private double cx, cy, rw, rh;
    private int sw, sh;

    public ScreenConverter(double cx, double cy, double rw, double rh, int sw, int sh) {
        this.cx = cx;
        this.cy = cy;
        this.rw = rw;
        this.rh = rh;
        this.sw = sw;
        this.sh = sh;
    }

    public ScreenPoint r2s(RealPoint p) {
        double x = (p.getX() - cx) / rw * sw;
        double y = (cy - p.getY()) / rh * sh;
        return new ScreenPoint((int) x, (int) y);
    }

    public RealPoint s2r(ScreenPoint p) {
        double x = p.getC() * rw/ sw + cx;
        double y = cy - p.getR() * rh/ sh;
        return new RealPoint(x, y);
    }

    public int r2sForXLine(double length) {
        double newLength = length / rw * sw;
        return (int) newLength;
    }

    public int r2sForYLine(double length) {
        double newLength = length / rh * sh;
        return (int) newLength;
    }

    public double s2rForXLine(int length) {
        return (double) length / sw * rw;
    }

    public double s2rForYLine(int length) {
        return (double) length / sh * rh;
    }

    public void moveCorner(RealPoint delta) {
        cx += delta.getX();
        cy += delta.getY();
    }
    public void changeScale(double s) {
        rw *= s;
        rh *= s;
    }

    public double getCx() {
        return cx;
    }

    public void setCx(double cx) {
        this.cx = cx;
    }

    public double getCy() {
        return cy;
    }

    public void setCy(double cy) {
        this.cy = cy;
    }

    public double getRw() {
        return rw;
    }

    public void setRw(double rw) {
        this.rw = rw;
    }

    public double getRh() {
        return rh;
    }

    public void setRh(double rh) {
        this.rh = rh;
    }

    public int getSw() {
        return sw;
    }

    public void setSw(int sw) {
        this.sw = sw;
    }

    public int getSh() {
        return sh;
    }

    public void setSh(int sh) {
        this.sh = sh;
    }
}
