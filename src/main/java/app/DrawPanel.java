package app;

import drawers.*;
import figures.Line;
import figures.Rect;
import screenWork.RealPoint;
import screenWork.ScreenConverter;
import screenWork.ScreenPoint;

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
    private static List<Rect> allRects = new ArrayList<>();
    private Rect editingRect = null;
    private RealPoint editPoint = null;

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
        RectDrawer rd = new RectDrawer();

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

        origG.drawImage(bi, 0, 0, null);
        g.dispose();
    }

    private static void drawLine(Graphics2D g, ScreenConverter sc, Line l) {
        ScreenPoint p1 = sc.r2s(l.getP1());
        ScreenPoint p2 = sc.r2s(l.getP2());
        g.drawLine(p1.getC(), p1.getR(), p2.getC(), p2.getR());
    }
    private static void drawLine(LineDrawer ld, ScreenConverter sc, Line l){
        ScreenPoint p1 = sc.r2s(l.getP1());
        ScreenPoint p2 = sc.r2s(l.getP2());
        ld.drawLine(p1.getC(), p1.getR(), p2.getC(), p2.getR());
    }

    private static void drawRect(Graphics2D g, ScreenConverter sc, Rect rect) {
        ScreenPoint corner = sc.r2s(rect.getCorner());
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
            if (isNear(sc, r.getCorner(), searchPoint, eps)) {
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
                editingRect = null;
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
                ScreenPoint currentCorner = sc.r2s(currentRect.getCorner());
                int width = p.getC() - currentCorner.getC();
                int height = p.getR() - currentCorner.getR();
                if (width >= 0 && height >= 0) {
                    currentRect.setWidth(sc.s2rForXLine(width));
                    currentRect.setHeight(sc.s2rForYLine(height));
                    allRects.add(currentRect);
                }
                currentRect = null;
            }
        } else if (SwingUtilities.isMiddleMouseButton(e)) {
            if (editingRect != null) {
                if (isNear(sc, editingRect.getCorner(), new ScreenPoint(e.getX(), e.getY()), 10)) {
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
                ScreenPoint currentCorner = sc.r2s(currentRect.getCorner());
                int width = p.getC() - currentCorner.getC();
                int height = p.getR() - currentCorner.getR();
                if (width >= 0 && height >= 0) {
                    currentRect.setWidth(sc.s2rForXLine(width));
                    currentRect.setHeight(sc.s2rForYLine(height));
                } else {
                    currentRect = null;
                }
            } else if (editingRect != null) {

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
}
