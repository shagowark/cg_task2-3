package figures;

import screenWork.RealPoint;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Polygon {
    private List<RealPoint> points = new ArrayList<>();

    public void addPoint (RealPoint p){
        points.add(p);
    }

    public boolean hasPoint(RealPoint p){
        for (RealPoint point : points){
            if (point == p){
                return true;
            }
        }
        return false;
    }

    public RealPoint getPoint(int index){
        return points.get(index);
    }

    public int getSize(){
        return points.size();
    }
}
