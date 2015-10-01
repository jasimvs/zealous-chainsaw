package com.github.jasimvs.zealousChainsaw

import java.awt.Point

class ContourMatrixPathCalculator {

    public static final String END = "End"
    public static final String NORTH = "North"
    public static final String EAST = "East"
    public static final String WEST = "West"
    public static final String SOUTH = "South"

    private ArrayList<ArrayList<Integer>> heightMatrix
	private Map<Point, ContourMatrixNode> nodeMap

    private ContourMatrixNode selectedStartingPoint

    private Util util = new Util();

	ContourMatrixPathCalculator(ArrayList<ArrayList<Integer>> heightMatrix) {
		this.heightMatrix = heightMatrix
	}

	ContourMatrixNode calculateLongestSteepestDescendingPath() {
        sortDataMap(heightMatrix)
//		println heightMatrix
//		println nodeMap
        calculatePaths()
//      println nodeMap
        return selectedStartingPoint
	}
	
	private def calculatePaths() {
		nodeMap.eachWithIndex { it, index ->
//			println "$index $it "
			boolean firstIndex = index == 0
			if (firstIndex) {
				updateLeafNode(it.value)
                selectedStartingPoint = it.value
			} else {
				def validNodes = findLowerPointsNearby(it.value)
				if (validNodes) {
					def nextNode = findNextPoint(validNodes)
//					println nextNode
					updateNode(it.value, nextNode)
				} else {
					updateLeafNode(it.value)
				}
                updateSelectedStartingPoint(it.value)
			}
		}
	}

    private void updateSelectedStartingPoint(ContourMatrixNode node) {
        if (selectedStartingPoint.noOfStopsToLeafNodeOnLongestPath < node.noOfStopsToLeafNodeOnLongestPath ||
                (selectedStartingPoint.noOfStopsToLeafNodeOnLongestPath == node.noOfStopsToLeafNodeOnLongestPath && selectedStartingPoint.dropToLeaf < node.dropToLeaf)) {
            selectedStartingPoint = node
        }
    }

    private def updateNode(ContourMatrixNode currentPoint, nextNode) {
		currentPoint.direction = nextNode.key
        def nextPoint = nodeMap.get(nextNode.value)
        currentPoint.noOfStopsToLeafNodeOnLongestPath = nextPoint.noOfStopsToLeafNodeOnLongestPath + 1
		currentPoint.dropToLeaf = currentPoint.height - nextPoint.height + nextPoint.dropToLeaf
	}
	
	private def findNextPoint(validNodes) {
		validNodes.max { a, b ->
			if (nodeMap.get(a.value).noOfStopsToLeafNodeOnLongestPath == nodeMap.get(b.value).noOfStopsToLeafNodeOnLongestPath) {
				nodeMap.get(a.value).dropToLeaf <=> nodeMap.get(b.value).dropToLeaf
			} else {
				nodeMap.get(a.value).noOfStopsToLeafNodeOnLongestPath <=> nodeMap.get(b.value).noOfStopsToLeafNodeOnLongestPath
			}
		}
	}
	
	private def findLowerPointsNearby(ContourMatrixNode currentPoint) {
		def validNodes = [:]
        Map<String, Point> nodesAround = [ (NORTH) : util.getNorthPoint(currentPoint.location),
                                           (EAST) : util.getEastPoint(currentPoint.location),
                                           (WEST) : util.getWestPoint(currentPoint.location),
                                           (SOUTH) : util.getSouthPoint(currentPoint.location)]
		nodesAround.each { node ->
			if (isProcessed(node.value)) {
//				println node
				if (nodeMap.get(node.value).height < currentPoint.height) {
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
	
	private boolean isProcessed(def key) {
		if (nodeMap.get(key)?.noOfStopsToLeafNodeOnLongestPath > 0) {
			return true
		}
		return false
	}
	
	private Map<Point, ContourMatrixNode> sortDataMap(def inputArray) {
		def map = new HashMap()
		inputArray.eachWithIndex { a, x ->
			a.eachWithIndex { b, y ->
				Point point =  new Point(x, y)
				def node = new ContourMatrixNode(height: inputArray[x][y], location: point)
                map.put(point, node)
			}
		}
        nodeMap = map.sort { it.value.height }
	}
	
	def printPath(ContourMatrixNode node) {
        println "Number of stops: $node.noOfStopsToLeafNodeOnLongestPath Drop: $node.dropToLeaf"
        println "Starting point: $node.height [${(int)node.location.x},${(int)node.location.y}] $node.direction"
        def next = node
		while (next.direction != END) {
			switch (next.direction) {
				case NORTH:
                    next = nodeMap.get(util.getNorthPoint(next.location))
					break
				case EAST:
                    next = nodeMap.get(util.getEastPoint(next.location))
                    break
				case WEST:
                    next = nodeMap.get(util.getWestPoint(next.location))
                    break
				case SOUTH:
                    next = nodeMap.get(util.getSouthPoint(next.location))
                    break
                default:
                    println "Error: $next.direction is not valid "
                    return
			}
            println " -> $next.height [${(int)next.location.x},${(int)next.location.y}] $next.direction"
		}
	}

    public def printLongestSteepestDescendingPath() {
        ContourMatrixNode node = calculateLongestSteepestDescendingPath()
        printPath(node)
    }

}
