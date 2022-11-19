package solution;

import figures.Polygon;
import figures.Rect;
import screenWork.RealPoint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Solver {
    private List<Rect> rects;

    public Solver(List<Rect> rects) {
        this.rects = rects;
    }

    public Polygon findRectsJoin() {
        Polygon resultPolygon = new Polygon();
        List<Rect> workRects = sortRects(rects, Direction.RIGHT);
        RealPoint currPoint = workRects.get(0).getBaseCorner();
        resultPolygon.addPoint(currPoint);
        Direction currDirection = Direction.RIGHT;
        Rect currRect = workRects.get(0);
        boolean complete = false;

        do {
            switch (currDirection) {
                case BOT -> {
                    boolean found = false;
                    workRects = getListStartedFromCurrRect(sortRects(rects, Direction.BOT), currRect);
                    for (Rect rect : workRects) {
                        if (rect.getBaseCorner().getY() > currRect.getRightBotCorner().getY() && rect.getBaseCorner().getY() < currPoint.getY()) {
                            if (rect.getBaseCorner().getX() < currPoint.getX() && rect.getRightUppCorner().getX() > currPoint.getX()) {
                                found = true;
                                currPoint = new RealPoint(currPoint.getX(), rect.getRightUppCorner().getY());
                                if (resultPolygon.hasPoint(currPoint)) {
                                    resultPolygon.addPoint(currPoint);
                                    complete = true;
                                    break;
                                }
                                resultPolygon.addPoint(currPoint);
                                currDirection = Direction.RIGHT;
                                currRect = rect;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        currPoint = currRect.getRightBotCorner();
                        if (resultPolygon.hasPoint(currPoint)) {
                            resultPolygon.addPoint(currPoint);
                            complete = true;
                            break;
                        }
                        resultPolygon.addPoint(currPoint);
                        currDirection = Direction.LEFT;
                    }
                }
                case TOP -> {
                    boolean found = false;
                    workRects = getListStartedFromCurrRect(sortRects(rects, Direction.TOP), currRect);
                    for (Rect rect : workRects) {
                        if (rect.getLeftBotCorner().getY() < currRect.getBaseCorner().getY() && rect.getLeftBotCorner().getY() > currPoint.getY()) {
                            if (rect.getLeftBotCorner().getX() < currPoint.getX() && rect.getRightBotCorner().getX() > currPoint.getX()) {
                                found = true;
                                currPoint = new RealPoint(currPoint.getX(), rect.getLeftBotCorner().getY());
                                if (resultPolygon.hasPoint(currPoint)) {
                                    resultPolygon.addPoint(currPoint);
                                    complete = true;
                                    break;
                                }
                                resultPolygon.addPoint(currPoint);
                                currDirection = Direction.LEFT;
                                currRect = rect;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        currPoint = currRect.getBaseCorner();
                        if (resultPolygon.hasPoint(currPoint)) {
                            resultPolygon.addPoint(currPoint);
                            complete = true;
                            break;
                        }
                        resultPolygon.addPoint(currPoint);
                        currDirection = Direction.RIGHT;
                    }
                }
                case LEFT -> {
                    boolean found = false;
                    workRects = getListStartedFromCurrRect(sortRects(rects, Direction.LEFT), currRect);
                    for (Rect rect : workRects) {
                        if (rect.getRightUppCorner().getX() > currRect.getLeftBotCorner().getX() && rect.getRightUppCorner().getX() < currPoint.getX()) {
                            if (rect.getRightUppCorner().getY() > currPoint.getY() && rect.getRightBotCorner().getY() < currPoint.getY()) {
                                found = true;
                                currPoint = new RealPoint(rect.getRightUppCorner().getX(), currPoint.getY());
                                if (resultPolygon.hasPoint(currPoint)) {
                                    resultPolygon.addPoint(currPoint);
                                    complete = true;
                                    break;
                                }
                                resultPolygon.addPoint(currPoint);
                                currDirection = Direction.BOT;
                                currRect = rect;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        currPoint = currRect.getLeftBotCorner();
                        if (resultPolygon.hasPoint(currPoint)) {
                            resultPolygon.addPoint(currPoint);
                            complete = true;
                            break;
                        }
                        resultPolygon.addPoint(currPoint);
                        currDirection = Direction.TOP;
                    }
                }
                case RIGHT -> {
                    boolean found = false;
                    workRects = getListStartedFromCurrRect(sortRects(rects, Direction.RIGHT), currRect);
                    for (Rect rect : workRects) {
                        if (rect.getBaseCorner().getX() < currRect.getRightUppCorner().getX() && rect.getBaseCorner().getX() > currPoint.getX()) {
                            if (rect.getBaseCorner().getY() > currPoint.getY() && rect.getLeftBotCorner().getY() < currPoint.getY()) {
                                found = true;
                                currPoint = new RealPoint(rect.getBaseCorner().getX(), currPoint.getY());
                                if (resultPolygon.hasPoint(currPoint)) {
                                    resultPolygon.addPoint(currPoint);
                                    complete = true;
                                    break;
                                }
                                resultPolygon.addPoint(currPoint);
                                currDirection = Direction.TOP;
                                currRect = rect;
                                break;
                            }
                        }
                    }
                    if (!found) {
                        currPoint = currRect.getRightUppCorner();
                        if (resultPolygon.hasPoint(currPoint)) {
                            resultPolygon.addPoint(currPoint);
                            complete = true;
                            break;
                        }
                        resultPolygon.addPoint(currPoint);
                        currDirection = Direction.BOT;
                    }
                }
            }
        } while (!complete);

        return resultPolygon;
    }

    private List<Rect> sortRects(List<Rect> rects, Direction direction) {
        List<Rect> sortedRects = new ArrayList<>();
        for (int i = 0; i < rects.size(); i++){
            sortedRects.add(null);
        }
        Collections.copy(sortedRects, rects);

        for (int i = 1; i < sortedRects.size(); i++) {
            for (int j = i; j > 0; j--) {
                boolean breakCondition = false;
                switch (direction) {
                    case BOT:
                        if (!(sortedRects.get(j - 1).getBaseCorner().getY() < sortedRects.get(j).getBaseCorner().getY())) {
                            breakCondition = true;
                        }
                        break;
                    case TOP:
                        if (!(sortedRects.get(j - 1).getRightBotCorner().getY() > sortedRects.get(j).getRightBotCorner().getY())) {
                            breakCondition = true;
                        }
                        break;
                    case LEFT:
                        if (!(sortedRects.get(j - 1).getRightUppCorner().getX() < sortedRects.get(j).getRightUppCorner().getX())) {
                            breakCondition = true;
                        }
                        break;
                    case RIGHT:
                        if (!(sortedRects.get(j - 1).getBaseCorner().getX() > sortedRects.get(j).getBaseCorner().getX())) {
                            breakCondition = true;
                        }
                        break;
                }
                if (breakCondition) {
                    break;
                }
                Rect temp = sortedRects.get(j - 1);
                sortedRects.set(j - 1, sortedRects.get(j));
                sortedRects.set(j, temp);
            }
        }

        return sortedRects;
    }

    private List<Rect> getListStartedFromCurrRect(List<Rect> rects, Rect targetRect) { //предполгается отсортированный лист
        List<Rect> resultRects = new ArrayList<>();
        boolean reached = false;
        for (Rect rect : rects) {
            if (reached) {
                resultRects.add(rect);
            }
            if (rect == targetRect) {
                reached = true;
            }
        }

        return resultRects;
    }
}
