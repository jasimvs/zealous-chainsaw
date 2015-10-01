package com.github.jasimvs.zealousChainsaw

import java.awt.Point

class ContourMatrixNode {

	Point location
	int height
	
	String direction
	int noOfStopsToLeafNodeOnLongestPath
	int dropToLeaf

	String toString() {
		return "$height $location.x $location.y $direction $noOfStopsToLeafNodeOnLongestPath $dropToLeaf "
	}
}
