package com.github.jasimvs.zealousChainsaw

import spock.lang.Specification

class UtilTest extends Specification {

    String input = """4 4
1 8 7 3
2 5 9 3
6 3 2 5
4 4 1 6
"""
    def output = [[1, 8, 7, 3],
                  [2, 5, 9, 3],
                  [6, 3, 2, 5],
                  [4, 4, 1, 6]]

    def "getArrayListFromMultiLineString"() {
        when:
        def test = new Util().getArrayListFromMultiLineString(input)

        then:
        output == test
    }
}
