package com.github.jasimvs.zealousChainsaw

import java.awt.Point

class Util {

    public ArrayList<ArrayList<Integer>> getArrayListFromMultiLineString(String input) {
        Integer breadth
        Integer height
        ArrayList<ArrayList<Integer>> array

        input.eachLine { line, x ->
            if (x == 0) {
                def something = line.tokenize(" ")
                breadth = something.get(0).toInteger()
                height = something.get(1).toInteger()
                array = new ArrayList<ArrayList<Integer>>()
            } else {
                ArrayList<Integer> values = line.tokenize(" ")*.toInteger()
                validateArraySize(x, breadth, values.size(), height)
                array.add(values)
            }
        }
        return array
    }

    public ArrayList<ArrayList<Integer>> getArrayListFromUrl(def input) {
        Integer breadth
        Integer height
        ArrayList<ArrayList<Integer>> array

        input.eachLine(0, { line, x ->
            if (x == 0) {
                def something = line.tokenize(" ")
                breadth = something.get(0).toInteger()
                height = something.get(1).toInteger()
                array = new ArrayList<ArrayList<Integer>>()
            } else {
                ArrayList<Integer> values = line.tokenize(" ")*.toInteger()
                validateArraySize(x, breadth, values.size(), height)
                array.add(values)
            }
        })
        return array
    }

    private void validateArraySize(x, breadth, y, height) {
        if (x > breadth || y > height) {
            throw new IllegalArgumentException("Error in input: expecting $breadth x $height values, but got $x x $y")
        }
    }

    /**
     * Increments x by int 1
     * @param point
     * @return point
     */
    Point getSouthPoint(Point point) {
        return new Point((Integer) point.x + 1, (Integer) point.y)
    }

    /**
     * Decrements y by int 1
     * @param point
     * @return point
     */
    Point getWestPoint(Point point) {
        return new Point((Integer) point.x, (Integer) point.y - 1)
    }

    /**
     * Increments y by int 1
     * @param point
     * @return point
     */
    Point getEastPoint(Point point) {
        return new Point((Integer) point.x, (Integer) point.y + 1)
    }

    /**
     * Decrements x by int 1
     * @param point
     * @return point
     */
    Point getNorthPoint(Point point) {
        return new Point((Integer) point.x - 1, (Integer) point.y)
    }
}
