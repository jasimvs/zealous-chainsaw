package com.github.jasimvs.zealousChainsaw

class Util {

    /**
     * Verifies x > breadth and y > height
     * @param x
     * @param breadth
     * @param y
     * @param height
     * @throws IllegalArgumentException
     */
    public static boolean validateArraySize(x, breadth, y, height) {
        if (x > breadth || y > height) {
            throw new IllegalArgumentException("Error in input: expecting $breadth x $height values, but got $x x $y")
        }
        return true
    }

    /**
     * Returns points North, South, East, West of given point
     * @param currentPoint
     * @return LinkedHashMap<Direction, Point>
     */
    public static Map<Direction, Point> getPointsAround(ContourMatrixNode currentPoint) {
        [(Direction.North): getNorthPoint(currentPoint.location),
         (Direction.East) : getEastPoint(currentPoint.location),
         (Direction.West) : getWestPoint(currentPoint.location),
         (Direction.South): getSouthPoint(currentPoint.location)]
    }

    /**
     * Increments x by int 1
     * @param point
     * @return point
     */
    static Point getSouthPoint(Point point) {
        return new Point((Integer) point.x + 1, (Integer) point.y)
    }

    /**
     * Decrements y by int 1
     * @param point
     * @return point
     */
    static Point getWestPoint(Point point) {
        return new Point((Integer) point.x, (Integer) point.y - 1)
    }

    /**
     * Increments y by int 1
     * @param point
     * @return point
     */
    static Point getEastPoint(Point point) {
        return new Point((Integer) point.x, (Integer) point.y + 1)
    }

    /**
     * Decrements x by int 1
     * @param point
     * @return point
     */
    static Point getNorthPoint(Point point) {
        return new Point((Integer) point.x - 1, (Integer) point.y)
    }
}
