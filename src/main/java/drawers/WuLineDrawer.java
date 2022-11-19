package drawers;

public class WuLineDrawer implements LineDrawer {
    private PixelDrawer pd;

    public WuLineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    // integer part of x
    private int ipart(double x) {
        return (int) Math.floor(x);
    }

    private int round(double x) {
        return ipart(x + 0.5);
    }

    // fractional part of x
    private double fpart(double x) {
        return x - Math.floor(x);
    }

    private double rfpart(double x) {
        return 1 - fpart(x);
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        boolean steep = Math.abs(y2 - y1) > Math.abs(x2 - x1);

        if (steep) {
            int temp = x1;
            x1 = y1;
            y1 = temp;

            temp = x2;
            x2 = y2;
            y2 = temp;
        }
        if (x1 > x2) {
            int temp = x1;
            x1 = x2;
            x2 = temp;

            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        int dx = x2 - x1;
        int dy = y2 - y1;

        int gradient;
        if (dx == 0) {
            gradient = 1;
        } else {
            gradient = dy / dx;
        }

        // handle first endpoint
        int xend = x1;
        int yend = y1 + gradient * xend;
        int xgap = x1;
        int xpxl1 = xend; // this will be used in the main loop
        int ypxl1 = yend;
        if (steep) {
            pd.drawPixel(ypxl1, xpxl1);
           pd.drawPixel(ypxl1 + 1, xpxl1);
        } else {
            pd.drawPixel(xpxl1, ypxl1);
            pd.drawPixel(xpxl1, ypxl1 + 1);
        }
        int intery = yend + gradient; // first y-intersection for the main loop

        // handle second endpoint
        xend = x2;
        yend = y2 + gradient * xend;
        xgap = x2;
        int xpxl2 = xend; //this will be used in the main loop
        int ypxl2 = ipart(yend);
        if (steep) {
            pd.drawPixel(ypxl2, xpxl2);
            pd.drawPixel(ypxl2 + 1, xpxl2);
        } else {
            pd.drawPixel(xpxl2, ypxl2);
            pd.drawPixel(xpxl2, ypxl2 + 1);
        }

        // main loop
        if (steep) {
            for (int x = xpxl1 + 1; x < xpxl2 - 1; x++) {
                pd.drawPixel(ipart(intery), x);
                pd.drawPixel(ipart(intery) + 1, x);
                intery = intery + gradient;
            }
        } else {
            for (int x = xpxl1 + 1; x < xpxl2 - 1; x++) {
                pd.drawPixel(x, ipart(intery));
                pd.drawPixel(x, ipart(intery) + 1);
                intery = intery + gradient;
            }
        }
    }
}
