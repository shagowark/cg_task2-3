package drawers;

import screenWork.ScreenConverter;
import figures.Rect;

public class RectDrawer {
    public void drawRect(Rect rect, LineDrawer ld, ScreenConverter sc){
        int cornerX = sc.r2s(rect.getBaseCorner()).getC();
        int cornerY = sc.r2s(rect.getBaseCorner()).getR();
        int width = sc.r2sForXLine(rect.getWidth());
        int height = sc.r2sForYLine(rect.getHeight());
        ld.drawLine(cornerX, cornerY, cornerX + width, cornerY);
        ld.drawLine(cornerX + width, cornerY, cornerX + width, cornerY + height);
        ld.drawLine(cornerX + width, cornerY + height, cornerX, cornerY + height);
        ld.drawLine(cornerX, cornerY, cornerX, cornerY + height);
    }
}
