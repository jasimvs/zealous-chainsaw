package com.github.jasimvs.zealousChainsaw

public class ContourMatrixNode {

	Point location
	int height
	
	Direction direction
	int noOfStopsToLeafNodeOnLongestPath
	int dropToLeaf

	String toString() {
		return "$height $location.x $location.y $direction $noOfStopsToLeafNodeOnLongestPath $dropToLeaf "
	}
}

class Point {
	int x, y
	Point(int x, int y) {
		this.x = x
		this.y = y
	}
}

public enum Direction {
    End, North, East, West, South;
}
