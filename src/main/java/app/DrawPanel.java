package app;

import drawers.*;
import figures.Line;
import figures.Polygon;
import figures.Rect;
import screenWork.RealPoint;
import screenWork.ScreenConverter;
import screenWork.ScreenPoint;
import solution.Solver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class DrawPanel extends JPanel implements MouseListener, MouseMotionListener, MouseWheelListener {
    private ScreenConverter sc;
    private Line ox, oy;
    private Rect currentRect = null;
    private List<Rect> allRects = new ArrayList<>();
    private Rect editingRect = null;
    private boolean editingRightCorner = false; //bottom
    private boolean editingLeftCorner = false; //upper

    public DrawPanel() {
        sc = new ScreenConverter(-2, 2, 4, 4, 800, 600);
        ox = new Line(new RealPoint(-1, 0), new RealPoint(1, 0));
        oy = new Line(new RealPoint(0, -1), new RealPoint(0, 1));
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addMouseWheelListener(this);
    }

    @Override
    public void paintComponent(Graphics origG) {
        sc.setSw(getWidth());
        sc.setSh(getHeight());

        BufferedImage bi = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bi.createGraphics();
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, getWidth(), getHeight());

        PixelDrawer pd = new GraphicsPixelDrawer(g);
        LineDrawer ldDDA = new DDALineDrawer(pd);
        LineDrawer ldGr = new GraphicsLineDrawer(g);
        LineDrawer ldBres = new BresenhamLineDrawer(pd);
        LineDrawer ldWu = new WuLineDrawer(pd);
        RectDrawer rd = new RectDrawer();
        PolygonDrawer polyD = new PolygonDrawer();

        g.setColor(Color.BLUE);
        drawLine(ldDDA, sc, ox);
        drawLine(ldDDA, sc, oy);

        g.setColor(Color.BLACK);

        for (Rect rect : allRects) {
            rd.drawRect(rect, ldDDA, sc);
        }
        if (currentRect != null) {
            g.setColor(Color.red);
            rd.drawRect(currentRect, ldBres, sc);
        }

        if (editingRect != null) {
            g.setColor(Color.green);
            rd.drawRect(editingRect, ldGr, sc);
        }

        if (modeIntersect) {
            Solver solver = new Solver(allRects);
            Polygon resultPolygon = solver.findRectsJoin();
            g.setColor(Color.MAGENTA);
            polyD.drawPolygon(resultPolygon, ldDDA, sc);
        }

        origG.drawImage(bi, 0, 0, null);
        g.dispose();
    }

//    private static List<Line> findRectJoin(List<Rect> rects){
//        List<Line> resultLines = new ArrayList<>();
//        //rects = sortRects(rects);
//        sortRects(rects); // todo ?????????? ????????, ???????????? ???? ????????????
//        if (rects.size() == 1){
//            Rect currRect = rects.get(0);
//            resultLines.add(new Line(currRect.getBaseCorner(), currRect.getRightUppCorner()));
//            resultLines.add(new Line(currRect.getRightUppCorner(), currRect.getRightBotCorner()));
//            resultLines.add(new Line(currRect.getRightBotCorner(), currRect.getLeftBotCorner()));
//            resultLines.add(new Line(currRect.getLeftBotCorner(), currRect.getBaseCorner()));
//        } else {
//            crossRects(0, rects.get(0).getBaseCorner(), resultLines, rects);
//        }
//
//        return resultLines;
//    }
//
//    private static RealPoint crossRects(int curr, RealPoint prevPoint, List<Line> resultLines, List<Rect> rects){
//        if (rects.size() == curr + 1){
//            Rect currRect = rects.get(curr);
//            resultLines.add(new Line(prevPoint, currRect.getRightUppCorner()));
//            resultLines.add(new Line(currRect.getRightUppCorner(), currRect.getRightBotCorner()));
//            Rect prevRect = rects.get(curr - 1);
//            if (prevRect.getBaseCorner().getY() < currRect.getBaseCorner().getY()){
//                resultLines.add(new Line(currRect.getRightBotCorner(),
//                        new RealPoint(prevRect.getRightUppCorner().getX(), currRect.getRightBotCorner().getY())));
//                return new RealPoint(prevRect.getRightUppCorner().getX(), currRect.getRightBotCorner().getY());
//            } else {
//                resultLines.add(new Line(currRect.getRightBotCorner(), currRect.getLeftBotCorner()));
//                resultLines.add(new Line(currRect.getLeftBotCorner(),
//                        new RealPoint(currRect.getLeftBotCorner().getX(), prevRect.getLeftBotCorner().getY())));
//                return new RealPoint(currRect.getLeftBotCorner().getX(), prevRect.getLeftBotCorner().getY());
//            }
//
//        }
//        Rect currRect = rects.get(curr);
//        Rect nextRect = rects.get(curr+1);
//        RealPoint currBaseCorner = currRect.getBaseCorner();
//        RealPoint nextBaseCorner = nextRect.getBaseCorner();
//        double currCornerX = currBaseCorner.getX();
//        double currCornerY = currBaseCorner.getY();
//        double nextCornerX = nextBaseCorner.getX();
//        double nextCornerY = nextBaseCorner.getY();
//        if (currCornerX + currRect.getWidth() > nextCornerX){
//            if (currCornerY < nextCornerY){
//                RealPoint crossPoint = new RealPoint(nextCornerX, currCornerY);
//                resultLines.add(new Line(prevPoint, crossPoint));
//                resultLines.add(new Line(crossPoint, nextBaseCorner));
//                prevPoint = crossRects(curr+1, nextBaseCorner, resultLines, rects);
//                resultLines.add(new Line(prevPoint, currRect.getRightBotCorner()));
//
//                if (curr - 1 >= 0) {
//                    Rect prevRect = rects.get(curr - 1);
//                    if (prevRect.getBaseCorner().getY() < currRect.getBaseCorner().getY()) {
//                        resultLines.add(new Line(currRect.getRightBotCorner(),
//                                new RealPoint(prevRect.getRightUppCorner().getX(), currRect.getRightBotCorner().getY())));
//                        return new RealPoint(prevRect.getRightUppCorner().getX(), currRect.getRightBotCorner().getY());
//                    } else {
//                        resultLines.add(new Line(currRect.getRightBotCorner(), currRect.getLeftBotCorner()));
//                        resultLines.add(new Line(currRect.getLeftBotCorner(),
//                                new RealPoint(currRect.getLeftBotCorner().getX(), prevRect.getLeftBotCorner().getY())));
//                        return new RealPoint(currRect.getLeftBotCorner().getX(), prevRect.getLeftBotCorner().getY());
//                    }
//                } else {
//                    resultLines.add(new Line(currRect.getRightBotCorner(), currRect.getLeftBotCorner()));
//                    resultLines.add(new Line(currRect.getLeftBotCorner(), currRect.getBaseCorner()));
//                    return currBaseCorner;
//                }
//            } else {
//                RealPoint crossPoint = new RealPoint(currRect.getRightUppCorner().getX(), nextCornerY);
//                resultLines.add(new Line(prevPoint, currRect.getRightUppCorner()));
//                resultLines.add(new Line(currRect.getRightUppCorner(), crossPoint));
//                prevPoint = crossRects(curr+1, crossPoint, resultLines, rects);
//
//                if (curr - 1 >= 0) {
//                    Rect prevRect = rects.get(curr - 1);
//                    if (prevRect.getBaseCorner().getY() < currRect.getBaseCorner().getY()) {
//                        resultLines.add(new Line(prevPoint,
//                                new RealPoint(prevRect.getRightUppCorner().getX(), currRect.getLeftBotCorner().getY())));
//                        return new RealPoint(prevRect.getRightUppCorner().getX(), currRect.getLeftBotCorner().getY());
//                    } else {
//                        resultLines.add(new Line(prevPoint, currRect.getLeftBotCorner()));
//                        resultLines.add(new Line(currRect.getLeftBotCorner(),
//                                new RealPoint(currRect.getLeftBotCorner().getX(), prevRect.getLeftBotCorner().getY())));
//                        return new RealPoint(currRect.getLeftBotCorner().getX(), prevRect.getLeftBotCorner().getY());
//                    }
//                } else {
//                    resultLines.add(new Line(prevPoint, currRect.getLeftBotCorner()));
//                    resultLines.add(new Line(currRect.getLeftBotCorner(), currRect.getBaseCorner()));
//                    return currBaseCorner;
//                }
//            }
//        } else {
//            resultLines.add(new Line(currRect.getBaseCorner(), currRect.getRightUppCorner()));
//            resultLines.add(new Line(currRect.getRightUppCorner(), currRect.getRightBotCorner()));
//            resultLines.add(new Line(currRect.getRightBotCorner(), currRect.getLeftBotCorner()));
//            resultLines.add(new Line(currRect.getLeftBotCorner(), currRect.getBaseCorner()));
//            crossRects(curr+1, nextBaseCorner, resultLines, rects);
//            return currBaseCorner;
//        }
//    }
//    public static List<Rect> sortRects(List<Rect> rects){
//        for (int i = 1; i < rects.size(); i++){
//            for (int j = i; j > 0 && rects.get(j-1).getBaseCorner().getX() > rects.get(j).getBaseCorner().getX(); j--){
//                Rect temp = rects.get(j-1);
//                rects.set(j-1, rects.get(j));
//                rects.set(j, temp);
//            }
//        }

//        for (int i = 1; i < rects.size(); i++){
//            for (int j = i; j > 0 && rects.get(j-1).getBaseCorner().getX() > rects.get(j).getBaseCorner().getX(); j--){
//                Rect temp = rects.get(j-1);
//                rects.set(j-1, rects.get(j));
//                rects.set(j, temp);
//            }
//        }
//        return rects;
//    }

    private static void drawLine(Graphics2D g, ScreenConverter sc, Line l) {
        ScreenPoint p1 = sc.r2s(l.getP1());
        ScreenPoint p2 = sc.r2s(l.getP2());
        g.drawLine(p1.getC(), p1.getR(), p2.getC(), p2.getR());
    }

    private static void drawLine(LineDrawer ld, ScreenConverter sc, Line l) {
        ScreenPoint p1 = sc.r2s(l.getP1());
        ScreenPoint p2 = sc.r2s(l.getP2());
        ld.drawLine(p1.getC(), p1.getR(), p2.getC(), p2.getR());
    }


    private static void drawRect(Graphics2D g, ScreenConverter sc, Rect rect) {
        ScreenPoint corner = sc.r2s(rect.getBaseCorner());
        int width = sc.r2sForXLine(rect.getWidth());
        int height = sc.r2sForYLine(rect.getHeight());
        g.drawRect(corner.getC(), corner.getR(), width, height);
    }

    private static boolean isNear(ScreenConverter sc, RealPoint rp, ScreenPoint sp, int eps) {
        ScreenPoint p = sc.r2s(rp);
        return eps * eps > (p.getR() - sp.getR()) * (p.getR() - sp.getR()) + (p.getC() - sp.getC()) * (p.getC() - sp.getC());
    }

    private static double distanceToLine(ScreenPoint lp1, ScreenPoint lp2, ScreenPoint cp) {
        double a = lp2.getR() - lp1.getR();
        double b = -(lp2.getC() - lp1.getC());
        //b*x-a*y + cp.getC()*b + cp.getR()*a = 0
        //a*x+b*y + a*lp1.getC() - b*lp1.getR() = 0
        double e = cp.getC() * b + cp.getR() * a;
        double f = a * lp1.getC() - b * lp1.getR();
        double y = (a * e - b * f) / (a * a + b * b);
        double x = (a * y - e) / b;
        return Math.sqrt((cp.getC() - x) * (cp.getC() - x) + (cp.getR() - y) * (cp.getR() - y));
    }

    private static boolean isPointInRect(ScreenPoint pr1, ScreenPoint pr2, ScreenPoint cp) {
        return cp.getC() >= Math.min(pr1.getC(), pr2.getC()) &&
                cp.getC() <= Math.max(pr1.getC(), pr2.getC()) &&
                cp.getR() >= Math.min(pr1.getR(), pr2.getR()) &&
                cp.getR() <= Math.max(pr1.getR(), pr2.getR());
    }

    private static boolean closeToLine(ScreenConverter sc, Line l, ScreenPoint p, int eps) {
        ScreenPoint a = sc.r2s(l.getP1());
        ScreenPoint b = sc.r2s(l.getP2());
        RealPoint ra = l.getP1();
        RealPoint rb = l.getP2();
        return isNear(sc, ra, p, eps) || isNear(sc, rb, p, eps) || (distanceToLine(a, b, p) < eps && isPointInRect(a, b, p));
    }

    private static Line findLine(ScreenConverter sc, List<Line> lines, ScreenPoint searchPoint, int eps) {
        for (Line l : lines) {
            if (closeToLine(sc, l, searchPoint, eps)) {
                return l;
            }
        }
        return null;
    }

    private static Rect findRect(ScreenConverter sc, List<Rect> rects, ScreenPoint searchPoint, int eps) {
        for (Rect r : rects) {
            if (isNear(sc, r.getBaseCorner(), searchPoint, eps)) {
                return r;
            }
        }
        return null;
    }


    @Override
    public void mouseClicked(MouseEvent e) {

    }

    private ScreenPoint prevPoint = null;

    @Override
    public void mousePressed(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            prevPoint = new ScreenPoint(e.getX(), e.getY());
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (editingRect == null) {
                Rect x = findRect(sc, allRects, new ScreenPoint(e.getX(), e.getY()), 10);
                if (x != null) {
                    editingRect = x;
                } else {
                    RealPoint p = sc.s2r(new ScreenPoint(e.getX(), e.getY()));
                    currentRect = new Rect(p, 0, 0);
                }
            } else {
                ScreenPoint p = new ScreenPoint(e.getX(), e.getY());
                RealPoint leftCorner = editingRect.getBaseCorner();
                ScreenPoint rc = sc.r2s(editingRect.getRightBotCorner());
                RealPoint rightCorner = sc.s2r(rc);
                if (isNear(sc, leftCorner, p, 10)) {
                    editingLeftCorner = true;
                } else if (isNear(sc, rightCorner, p, 10)) {
                    editingRightCorner = true;
                } else {
                    editingRect = null;
                    editingLeftCorner = false;
                    editingRightCorner = false;
                }
            }
        }

        repaint();

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            prevPoint = null;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentRect != null) {
                ScreenPoint p = new ScreenPoint(e.getX(), e.getY());
                ScreenPoint currentCorner = sc.r2s(currentRect.getBaseCorner());
                int width = p.getC() - currentCorner.getC();
                int height = p.getR() - currentCorner.getR();
                if (width >= 0 && height >= 0) {
                    currentRect.setWidth(sc.s2rForXLine(width));
                    currentRect.setHeight(sc.s2rForYLine(height));
                    allRects.add(currentRect);
                }
                currentRect = null;
            } else if (editingRect != null) {
                ScreenPoint p = new ScreenPoint(e.getX(), e.getY());
                if (editingRightCorner) {
                    RealPoint leftCorner = editingRect.getBaseCorner();
                    ScreenPoint lc = sc.r2s(leftCorner);
                    ScreenPoint rc = sc.r2s(editingRect.getRightBotCorner());
                    double width = sc.s2rForXLine(rc.getC() - lc.getC());
                    double height = sc.s2rForYLine(rc.getR() - lc.getR());
                    editingRect.setWidth(width);
                    editingRect.setHeight(height);
                    editingRect = null;
                } else if (editingLeftCorner) {
                    editingRect.setBaseCorner(sc.s2r(p));
                    editingRect = null;
                }
                editingRightCorner = false;
                editingLeftCorner = false;
            }
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            if (editingRect != null) {
                if (isNear(sc, editingRect.getBaseCorner(), new ScreenPoint(e.getX(), e.getY()), 10)) {
                    allRects.remove(editingRect);
                    editingRect = null;
                }
            }
        }
        repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            ScreenPoint curPoint = new ScreenPoint(e.getX(), e.getY());
            RealPoint p1 = sc.s2r(curPoint);
            RealPoint p2 = sc.s2r(prevPoint);
            RealPoint delta = p2.minus(p1);
            sc.moveCorner(delta);
            prevPoint = curPoint;
        } else if (SwingUtilities.isLeftMouseButton(e)) {
            if (currentRect != null) {
                ScreenPoint p = new ScreenPoint(e.getX(), e.getY());
                ScreenPoint currentCorner = sc.r2s(currentRect.getBaseCorner());
                int width = Math.abs(p.getC() - currentCorner.getC());
                int height = Math.abs(p.getR() - currentCorner.getR());
                currentRect.setWidth(sc.s2rForXLine(width));
                currentRect.setHeight(sc.s2rForYLine(height));
            } else if (editingRect != null) {
                ScreenPoint p = new ScreenPoint(e.getX(), e.getY());
                if (editingRightCorner) {
                    RealPoint leftCorner = editingRect.getBaseCorner();
                    ScreenPoint lc = sc.r2s(leftCorner);
                    double width = Math.abs(sc.s2rForXLine(p.getC() - lc.getC()));
                    double height = Math.abs(sc.s2rForYLine(p.getR() - lc.getR()));
                    editingRect.setWidth(width);
                    editingRect.setHeight(height);
                } else if (editingLeftCorner) {
                    editingRect.setBaseCorner(sc.s2r(p));
                }
            }
        }
        repaint();

    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    private static final double SCALE_STEP = 0.1;

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int clicks = e.getWheelRotation();
        double coef = 1 + SCALE_STEP * (clicks < 0 ? -1 : 1);
        double scale = 1;
        for (int i = Math.abs(clicks); i > 0; i--) {
            scale *= coef;
        }
        sc.changeScale(scale);
        repaint();
    }

    private boolean modeIntersect = false;
    public void setMode(boolean intersect) {
        modeIntersect = intersect;
        repaint();
    }
}
