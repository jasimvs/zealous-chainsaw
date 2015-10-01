package com.github.jasimvs.zealousChainsaw

Util util = new Util()

//String input = """4 4
//4 8 7 3
//2 5 9 3
//6 3 2 5
//4 4 1 6
//"""
//ArrayList<ArrayList<Integer>> array = util.getArrayListFromMultiLineString(input)

def content = "http://s3-ap-southeast-1.amazonaws.com/geeks.redmart.com/coding-problems/map.txt".toURL()
ArrayList<ArrayList<Integer>> array = util.getArrayListFromUrl(content)

new ContourMatrixPathCalculator(array).printLongestSteepestDescendingPath()




