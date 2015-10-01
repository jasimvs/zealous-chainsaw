package com.github.jasimvs.zealousChainsaw

import spock.lang.Specification

import java.awt.Point

class ContourMatrixPathCalculatorTest extends Specification {

    def testInput =
            [[1, 8, 7, 3],
             [2, 5, 9, 3],
             [6, 3, 2, 5],
             [4, 4, 1, 6]]

    ContourMatrixNode node = new ContourMatrixNode(height: 9, location: new Point(1, 2), direction: "West", noOfStopsToLeafNodeOnLongestPath: 5, dropToLeaf: 8)

    def "CalculateLongestSteepestDescendingPath"() {
        when:
        ContourMatrixNode output = new ContourMatrixPathCalculator(testInput).calculateLongestSteepestDescendingPath()

        then:
        //verify output
        output.toString() == node.toString()
    }

    String errorOut = """Number of stops: 5 Drop: 8
Starting point: 9 [1,2] XYZ
Error: XYZ is not valid"""

    def "printPathError"() {
        setup:
        def oldStdOut = System.out;
        def bufStr = new ByteArrayOutputStream()
        System.out = new PrintStream(bufStr)
        node.direction = "XYZ"

        when :
        new ContourMatrixPathCalculator(testInput).printPath(node)
        System.out = oldStdOut
        String prtTestStr = bufStr.toString()

        then:
        prtTestStr.trim().normalize() == errorOut
    }

    def out = """Number of stops: 5 Drop: 8
Starting point: 9 [1,2] West
 -> 5 [1,1] South
 -> 3 [2,1] East
 -> 2 [2,2] South
 -> 1 [3,2] End"""

    def "PrintLongestSteepestDescendingPath"() {
        setup:
        def oldStdOut = System.out;
        def bufStr = new ByteArrayOutputStream()
        System.out = new PrintStream(bufStr)

        when:
        new ContourMatrixPathCalculator(testInput).printLongestSteepestDescendingPath()
        //cleanup: but cannot clean before then!
        System.out = oldStdOut
        String prtTestStr = bufStr.toString()

        then:
        prtTestStr.trim().normalize() == out
    }
}
