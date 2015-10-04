package com.github.jasimvs.zealousChainsaw

class Point {
    int x, y
    Point(int x, int y) {
        this.x = x
        this.y = y
    }
}

class ContourMatrixPathCalculator {

    public static final String END = "End"
    public static final String NORTH = "North"
    public static final String EAST = "East"
    public static final String WEST = "West"
    public static final String SOUTH = "South"

    ContourMatrixNode[][] nodeMatrix
    SortedMap<Integer, Set<ContourMatrixNode>> heightToNodesSortedMap

    private ContourMatrixNode selectedStartingPoint

    private Util util = new Util();

	ContourMatrixNode calculateLongestSteepestDescendingPath() {
        calculatePaths()
        return selectedStartingPoint
	}
	
	private def calculatePaths() {
        heightToNodesSortedMap.eachWithIndex { set, index ->
            boolean firstIndex = index == 0
            if (firstIndex) {
                set.value.each { node ->
                    updateLeafNode(node)
                    selectedStartingPoint = node
                }
            } else {
                set.value.each { node ->
                    def validNodes = findLowerNodesNearby(node)
                    if (validNodes) {
                        def nextNode = selectNextNode(validNodes)
                        updateNode(node, nextNode)
                    } else {
                        updateLeafNode(node)
                    }
                    updateSelectedStartingPoint(node)
                }
            }
        }
	}

    private void updateSelectedStartingPoint(ContourMatrixNode node) {
        if (selectedStartingPoint.noOfStopsToLeafNodeOnLongestPath < node.noOfStopsToLeafNodeOnLongestPath ||
                (selectedStartingPoint.noOfStopsToLeafNodeOnLongestPath == node.noOfStopsToLeafNodeOnLongestPath
                        && selectedStartingPoint.dropToLeaf < node.dropToLeaf)) {
            selectedStartingPoint = node
        }
    }

    private def updateNode(ContourMatrixNode currentPoint, def nextNode) {
		currentPoint.direction = nextNode.key
        ContourMatrixNode nextPoint = nodeMatrix[nextNode.value.x][nextNode.value.y]
        currentPoint.noOfStopsToLeafNodeOnLongestPath = nextPoint.noOfStopsToLeafNodeOnLongestPath + 1
		currentPoint.dropToLeaf = currentPoint.height - nextPoint.height + nextPoint.dropToLeaf
	}
	
	private def selectNextNode(validNodes) {
		validNodes.max { a, b ->
            ContourMatrixNode node1 = nodeMatrix[a.value.x][a.value.y]
            ContourMatrixNode node2 = nodeMatrix[b.value.x][b.value.y]
			if (node1.noOfStopsToLeafNodeOnLongestPath == node2.noOfStopsToLeafNodeOnLongestPath) {
				if (node1.dropToLeaf == node2.dropToLeaf) {
                    node2.height <=> node1.height
                } else {
                    node1.dropToLeaf <=> node2.dropToLeaf
                }
			} else {
				node1.noOfStopsToLeafNodeOnLongestPath <=> node2.noOfStopsToLeafNodeOnLongestPath
			}
		}
	}
	
	private def findLowerNodesNearby(ContourMatrixNode currentPoint) {
		def validNodes = [:]
        Map<String, Point> nodesAround = [ (NORTH) : util.getNorthPoint(currentPoint.location),
                                           (EAST) : util.getEastPoint(currentPoint.location),
                                           (WEST) : util.getWestPoint(currentPoint.location),
                                           (SOUTH) : util.getSouthPoint(currentPoint.location)]
		nodesAround.each { node ->
			if (isProcessed(node.value)) {
				if (nodeMatrix[node.value.x][node.value.y].height < currentPoint.height) {
					validNodes.put(node.key, node.value)
				}
			}
		}
		return validNodes
	}

    private updateLeafNode(ContourMatrixNode obj) {
		obj.direction = END
		obj.noOfStopsToLeafNodeOnLongestPath = 1
		obj.dropToLeaf = 0
	}
	
	private boolean isProcessed(Point point) {
		try {
            if (nodeMatrix[point.x][point.y].noOfStopsToLeafNodeOnLongestPath > 0) {
                return true
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            // Node does not exist, so ignore and return false
        }
		return false
	}
	
	String getPathAsString(ContourMatrixNode node) {
        String text = "Number of stops: $node.noOfStopsToLeafNodeOnLongestPath Drop: $node.dropToLeaf\n"
        text = text + "Starting point: $node.height [${(int)node.location.x},${(int)node.location.y}] $node.direction\n"
        ContourMatrixNode next = node
		while (next.direction != END) {
			switch (next.direction) {
				case NORTH:
                    Point northPoint = util.getNorthPoint(next.location)
                    next = nodeMatrix[northPoint.x][northPoint.y]
					break
				case EAST:
                    Point eastPoint = util.getEastPoint(next.location)
                    next = nodeMatrix[eastPoint.x][eastPoint.y]
                    break
				case WEST:
                    Point westPoint = util.getWestPoint(next.location)
                    next = nodeMatrix[westPoint.x][westPoint.y]
                    break
				case SOUTH:
                    Point southPoint = util.getSouthPoint(next.location)
                    next = nodeMatrix[southPoint.x][southPoint.y]
                    break
                default:
                    text = text + "Error: $next.direction is not valid.\n"
                    return
			}
            text = text + " -> $next.height [${(int)next.location.x},${(int)next.location.y}] $next.direction\n"
		}
        return text
	}

    public String getLongestSteepestDescendingPathAsString() {
        ContourMatrixNode node = calculateLongestSteepestDescendingPath()
        getPathAsString(node)
    }
}
