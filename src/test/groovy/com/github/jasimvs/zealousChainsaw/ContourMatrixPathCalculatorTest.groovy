package com.github.jasimvs.zealousChainsaw

import spock.lang.Specification

class ContourMatrixPathCalculatorTest extends Specification {

    String content = """4 4
1 8 7 3
2 5 9 3
6 3 2 1
4 4 0 6
"""

    ContourMatrixNode node = new ContourMatrixNode(height: 9, location: new Point(1, 2), direction: "West", noOfStopsToLeafNodeOnLongestPath: 5, dropToLeaf: 9)

    def "CalculateLongestSteepestDescendingPath"() {
        when:
        ContourMatrixNode output = ContourMatrixPathCalculator.getContourMatrixPathCalcFromString(content).calculateLongestSteepestDescendingPath();

        then:
        //verify output
        output.toString() == node.toString()
    }

    String errorExp = """Number of stops: 5 Drop: 9
Starting point: 9 [1,2] null
Error: null is not valid."""

    def "printPathError"() {
        setup:
        def calc = ContourMatrixPathCalculator.getContourMatrixPathCalcFromString(content)
        def actualNode = calc.calculateLongestSteepestDescendingPath()
        actualNode.direction = null

        when:
        String output = calc.getPathAsString(actualNode)

        then:
        output.trim() == errorExp.trim()
    }

    def exp = """Number of stops: 5 Drop: 9
Starting point: 9 [1,2] West
 -> 5 [1,1] South
 -> 3 [2,1] East
 -> 2 [2,2] South
 -> 0 [3,2] End
 """

    def "PrintLongestSteepestDescendingPath"() {

        when:
        def output = ContourMatrixPathCalculator.getContourMatrixPathCalcFromString(content).getLongestSteepestDescendingPathAsString()

        then:
        //prtTestStr.trim().normalize() == output
        exp.trim() == output.trim()
    }
}
