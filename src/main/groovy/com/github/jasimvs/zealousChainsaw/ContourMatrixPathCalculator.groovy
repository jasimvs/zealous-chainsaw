package com.github.jasimvs.zealousChainsaw

class ContourMatrixPathCalculator {

    ContourMatrixNode[][] nodeMatrix
    SortedMap<Integer, Set<ContourMatrixNode>> heightToNodesSortedMap

    private ContourMatrixNode selectedStartingPoint

    public static ContourMatrixPathCalculator getContourMatrixPathCalcFromString(String content) {
        SortedMap<Integer, Set<ContourMatrixNode>> heightToNodesSortedMap = new TreeMap<>()
        ContourMatrixNode[][] nodeMatrix

        content.eachLine { line, index ->
            if (index == 0) {
                def something = line.tokenize(" ")
                Integer breadth = something.get(0).toInteger()
                Integer height = something.get(1).toInteger()
                nodeMatrix = new ContourMatrixNode[breadth][height]
            } else {
                def x = index - 1
                ArrayList<Integer> values = line.tokenize(" ")*.toInteger()
                Util.validateArraySize(x, nodeMatrix.length, values.size(), nodeMatrix[0].length)

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
	
	private Map<Direction, Point> findLowerNodesNearby(ContourMatrixNode currentPoint) {
        Util.getPointsAround(currentPoint).findAll { node ->
            if (isProcessed(node.value) && nodeMatrix[node.value.x][node.value.y].height < currentPoint.height) {
                node
            }
        }
	}

    private updateLeafNode(ContourMatrixNode obj) {
		obj.direction = Direction.End
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
		while (next.direction != Direction.End) {
			switch (next.direction) {
				case Direction.North:
                    Point northPoint = Util.getNorthPoint(next.location)
                    next = nodeMatrix[northPoint.x][northPoint.y]
					break
				case Direction.East:
                    Point eastPoint = Util.getEastPoint(next.location)
                    next = nodeMatrix[eastPoint.x][eastPoint.y]
                    break
				case Direction.West:
                    Point westPoint = Util.getWestPoint(next.location)
                    next = nodeMatrix[westPoint.x][westPoint.y]
                    break
				case Direction.South:
                    Point southPoint = Util.getSouthPoint(next.location)
                    next = nodeMatrix[southPoint.x][southPoint.y]
                    break
                default:
                    text = text + "Error: $next.direction is not valid.\n"
                    return text
			}
            text = text + " -> $next.height [${(int)next.location.x},${(int)next.location.y}] $next.direction\n"
		}
        return text
	}

    public String getLongestSteepestDescendingPathAsString() {
        calculateLongestSteepestDescendingPath().with {
            getPathAsString(it)
        }
    }
}
