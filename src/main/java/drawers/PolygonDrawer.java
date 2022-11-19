package drawers;

import figures.Polygon;
import screenWork.ScreenConverter;
import screenWork.ScreenPoint;

public class PolygonDrawer {
    public void drawPolygon(Polygon polygon, LineDrawer ld, ScreenConverter sc) {
        for (int i = 0; i < polygon.getSize() - 1; i ++){
            ScreenPoint p1 = sc.r2s(polygon.getPoint(i));
            ScreenPoint p2 = sc.r2s(polygon.getPoint(i+1));
            ld.drawLine(p1.getC(), p1.getR(), p2.getC(), p2.getR());
        }
    }
}
