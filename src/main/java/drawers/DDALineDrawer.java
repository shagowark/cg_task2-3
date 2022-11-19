package drawers;

import java.awt.*;

public class DDALineDrawer implements LineDrawer {
    private PixelDrawer pd;

    public DDALineDrawer(PixelDrawer pd) {
        this.pd = pd;
    }

    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {

        // Длина и высота линии
        int deltaX = Math.abs(x1 - x2);
        int deltaY = Math.abs(y1 - y2);

        // Считаем минимальное количество итераций, необходимое
        // для отрисовки отрезка. Выбирая максимум из длины и высоты
        // линии, обеспечиваем связность линии
        int length = Math.max(deltaX, deltaY);

        // особый случай, на экране закрашивается ровно один пиксель
        if (length == 0) {
            pd.drawPixel(x1, y1);
            return;
        }

        // Вычисляем приращения на каждом шаге по осям абсцисс и ординат
        double dX = (double) (x2 - x1) / length;
        double dY = (double) (y2 - y1) / length;

        // Начальные значения
        double x = x1;
        double y = y1;

        // Основной цикл
        length++;
        while (length > 0) {
            pd.drawPixel((int) x, (int) y);
            x += dX;
            y += dY;

            length--;
        }
    }
}
