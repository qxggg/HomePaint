package com.homepainter.service;

import java.awt.*;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.List;
import java.awt.geom.Area;


public class PolygonDivider {

    public static void main(String[] args) {
        List<List<Double>> polygon = new ArrayList<>();
//        polygon.add(List.of(0.0, 0.0));
//        polygon.add(List.of(0.0, 2.0));
//        polygon.add(List.of(2.0, 3.0));
//        polygon.add(List.of(2.0, 0.0));


        // 转成单个数组的
        List<double[]> room = new ArrayList<>();
        for(int i=0;i<polygon.size();i++){
            double []roomSide = new double[2];
            for(int j=0;j< polygon.get(i).size();j++){
                roomSide[j] = polygon.get(i).get(j);
            }
            room.add(roomSide);
        }

        List<List<Double>> squares = polygonToSquare(polygon);
        for (List<Double> square : squares) {
            System.out.println(square);
            List<double[]> temp = new ArrayList<>();

            for(int i=0;i<square.size();i+=2){
                double[] tempp = new double[2];
                tempp[0] = square.get(i);
                tempp[1] = square.get(i+1);
                temp.add(tempp);
            }

            boolean res = isPolygon2InsidePolygon1(room,temp);
            System.out.println(res);
        }
    }

    public static List<List<Double>> polygonToSquare(List<List<Double>> polygon) {
        List<List<Double>> result = new ArrayList<>();
        double minX = Double.MAX_VALUE, maxX = Double.MIN_VALUE, minY = Double.MAX_VALUE, maxY = Double.MIN_VALUE;
        for (List<Double> point : polygon) {
            minX = Math.min(minX, point.get(0));
            maxX = Math.max(maxX, point.get(0));
            minY = Math.min(minY, point.get(1));
            maxY = Math.max(maxY, point.get(1));
        }
        for (double y = Math.floor(minY); y <= Math.ceil(maxY); y++) {
            for (double x = Math.floor(minX); x <= Math.ceil(maxX); x++) {
                if (isInsidePolygon(x, y, polygon)) {
                    result.add(getSquareVertices(x, y));
                }
            }
        }
        return result;
    }

    private static boolean isInsidePolygon(double x, double y, List<List<Double>> polygon) {
        int count = 0;
        for (int i = 0; i < polygon.size(); i++) {
            List<Double> p1 = polygon.get(i);
            List<Double> p2 = polygon.get((i + 1) % polygon.size());
            if ((p1.get(1) > y) != (p2.get(1) > y) &&
                    (x < (p2.get(0) - p1.get(0)) * (y - p1.get(1)) / (p2.get(1) - p1.get(1)) + p1.get(0))) {
                count++;
            }
        }
        return count % 2 == 1;
    }

    private static List<Double> getSquareVertices(double x, double y) {
        List<Double> vertices = new ArrayList<>();
        vertices.add(x);
        vertices.add(y);
        vertices.add(x + 1);
        vertices.add(y);
        vertices.add(x + 1);
        vertices.add(y + 1);
        vertices.add(x);
        vertices.add(y + 1);
        return vertices;
    }

    public static List<double[]> intersect(int[] polyX ,int[] polyY,int[] squareX ,int[] squareY) {
        List<double[]> res = new ArrayList<>();

        // 创建多边形和正方形对象
        Polygon poly = new Polygon(polyX, polyY, polyX.length);
        Polygon square = new Polygon(squareX, squareY, squareX.length);

        // 获取正方形的边界矩形
        Rectangle squareBounds = square.getBounds();

        // 使用Area类完成裁剪操作
        Area polyArea = new Area(poly);
        Area squareArea = new Area(squareBounds);
        polyArea.intersect(squareArea);


        PathIterator path = polyArea.getPathIterator(null);
        double[] coords = new double[6];
        while (!path.isDone()) {
            int type = path.currentSegment(coords);
            double temp[] = new double[2];
            switch (type) {
                case PathIterator.SEG_MOVETO:
                    temp[0] = coords[0];
                    temp[1] = coords[1];
                    res.add(temp);
                    break;
                case PathIterator.SEG_LINETO:
                    temp[0] = coords[0];
                    temp[1] = coords[1];
                    res.add(temp);
                    break;
                case PathIterator.SEG_QUADTO:
                    System.out.println("Quadratic curve to (" + coords[0] + ", " + coords[1] + "), (" + coords[2] + ", " + coords[3] + ")");
                    break;
                case PathIterator.SEG_CUBICTO:
                    System.out.println("Cubic curve to (" + coords[0] + ", " + coords[1] + "), (" + coords[2] + ", " + coords[3] + "), (" + coords[4] + ", " + coords[5] + ")");
                    break;
                case PathIterator.SEG_CLOSE:
                    System.out.println("Close path");
                    break;
            }
            path.next();
        }
        return res;
    }



    /**
     * 判断一个多边形是否完全包含在另一个多边形内部
     *
     * @param polygon1 外部多边形的顶点坐标列表
     * @param polygon2 内部多边形的顶点坐标列表
     * @return true表示polygon2完全包含在polygon1内部，false表示不完全包含
     */
    public static boolean isPolygon2InsidePolygon1(List<double[]> polygon1, List<double[]> polygon2) {
        // 创建外部多边形的java.awt.Polygon对象
        int n = polygon1.size();
        Polygon awtPolygon1 = new Polygon();
        for (double[] vertex : polygon1) {
            awtPolygon1.addPoint((int) Math.round(vertex[0]), (int) Math.round(vertex[1]));
        }

        // 创建内部多边形的java.awt.Polygon对象
        Polygon awtPolygon2 = new Polygon();
        for (double[] vertex : polygon2) {
            awtPolygon2.addPoint((int) Math.round(vertex[0]), (int) Math.round(vertex[1]));
        }

        // 检查内部多边形的所有顶点是否在外部多边形内部
        for (double[] vertex : polygon2) {
            // 判断是否相等
            boolean equal = false;
            for (int i=0;i<polygon1.size();i++) {

                boolean res = isPointOnLine(vertex[0],vertex[1],polygon1.get(i)[0],polygon1.get(i)[1], polygon1.get((i+1)%n)[0], polygon1.get((i+1)%n)[1] );
                if(res){
                    equal = true;
                    break;
                }
            }

            if (!equal&&!awtPolygon1.contains((int) Math.round(vertex[0]), (int) Math.round(vertex[1]))) {
                return false;
            }
        }

        // 使用java.awt.geom.Area类判断内部多边形是否完全包含在外部多边形内部
        Area area1 = new Area(awtPolygon1);
        Area area2 = new Area(awtPolygon2);
        area1.subtract(area2);
        return true;
    }

    public static boolean isPointOnLine(double x, double y, double x1, double y1, double x2, double y2) {
        double crossProduct = (x - x1) * (y2 - y1) - (y - y1) * (x2 - x1);
        if (Math.abs(crossProduct) > 1e-6) {
            return false;
        }
        double dotProduct = (x - x1) * (x2 - x1) + (y - y1) * (y2 - y1);
        if (dotProduct < 0) {
            return false;
        }
        double squaredLength = (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
        if (dotProduct > squaredLength) {
            return false;
        }
        return true;
    }


}

