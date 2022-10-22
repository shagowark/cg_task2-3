package drawers;

import java.awt.*;
import screenWork.ScreenConverter;
import screenWork.ScreenPoint;
import figures.Rect;

public class RectDrawer {
    public void drawRect(Rect rect, LineDrawer ld, ScreenConverter sc){
        int cornerX = sc.r2s(rect.getCorner()).getC();
        int cornerY = sc.r2s(rect.getCorner()).getR();
        int width = sc.r2sForXLine(rect.getWidth());
        int height = sc.r2sForYLine(rect.getHeight());
        ld.drawLine(cornerX, cornerY, cornerX + width, cornerY);
        ld.drawLine(cornerX + width, cornerY, cornerX + width, cornerY + height);
        ld.drawLine(cornerX + width, cornerY + height, cornerX, cornerY + height);
        ld.drawLine(cornerX, cornerY, cornerX, cornerY + height);
    }
}
