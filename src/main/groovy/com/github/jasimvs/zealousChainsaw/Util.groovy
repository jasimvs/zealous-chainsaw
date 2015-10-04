package com.github.jasimvs.zealousChainsaw

class Util {

    ContourMatrixPathCalculator getContourMatrixPathCalcFromString(String content) {
        SortedMap<Integer, Set<ContourMatrixNode>> heightToNodesSortedMap = new TreeMap<>()
        ContourMatrixNode[][] nodeMatrix

        Integer breadth
        Integer height

        content.eachLine { line, index ->
            if (index == 0) {
                def something = line.tokenize(" ")
                breadth = something.get(0).toInteger()
                height = something.get(1).toInteger()
                nodeMatrix = new ContourMatrixNode[breadth][height]
            } else {
                def x = index - 1
                ArrayList<Integer> values = line.tokenize(" ")*.toInteger()
                validateArraySize(x, breadth, values.size(), height)

                values.eachWithIndex { item, y ->
                    Point point = new Point(x, y)
                    nodeMatrix[x][y] = new ContourMatrixNode(height: item, location: point)
                    if (heightToNodesSortedMap.containsKey(item)) {
                        heightToNodesSortedMap.get(item).add(nodeMatrix[x][y])
                    } else {
                        def newSet = new HashSet<ContourMatrixNode>(1)
                        newSet.add(nodeMatrix[x][y])
                        heightToNodesSortedMap.put(item, newSet)
                    }
                }
            }
        }
        return new ContourMatrixPathCalculator(nodeMatrix: nodeMatrix, heightToNodesSortedMap: heightToNodesSortedMap)
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
