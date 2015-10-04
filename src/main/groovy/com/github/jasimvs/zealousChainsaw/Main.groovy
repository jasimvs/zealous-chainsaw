package com.github.jasimvs.zealousChainsaw

Util util = new Util()

//String content = """4 4
//4 8 7 3
//2 5 9 3
//6 3 2 5
//4 4 1 6
//"""

println "Downloading data..."
def content = "http://s3-ap-southeast-1.amazonaws.com/geeks.redmart.com/coding-problems/map.txt".toURL().getText();
//def content = new File("D:\\code\\jsm\\zealous-chainsaw\\map.txt").getText()

println "Processing data..."
ContourMatrixPathCalculator calc = util.getContourMatrixPathCalcFromString(content)
println "Calculating paths..."
print calc.getLongestSteepestDescendingPathAsString()




