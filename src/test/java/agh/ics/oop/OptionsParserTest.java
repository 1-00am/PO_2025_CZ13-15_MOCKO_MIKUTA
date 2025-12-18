package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class OptionsParserTest {
    @Test
    void parseEmptyArray() {
        var arr = new String[0];
        assertEquals(0, OptionsParser.parseMoveDirections(arr).size());
    }

    @Test
    void parseIncorrectSequence() {
        var arr = new String[] { "", " ", "fbrl", ",", "中", "brlf", "rlfb", "lfbr", "F", "B", "R", "L", "jdaoAFDNs" };
        assertThrows(IllegalArgumentException.class, () -> OptionsParser.parseMoveDirections(arr));
    }

    @Test
    void parseCorrectSequence() {
        var arr = new String[] { "f", "r", "l", "b" };
        var directions = OptionsParser.parseMoveDirections(arr).toArray();
        var expected = new MoveDirection[]{MoveDirection.FORWARD, MoveDirection.RIGHT, MoveDirection.LEFT, MoveDirection.BACKWARD};;
        assertArrayEquals(expected, directions);
    }

    @Test
    void parsePartiallyCorrectSequence() {
        var arr = new String[] { "f", "", "r", ",", "fblr", "l", "abc", "b", "be" };
        assertThrows(IllegalArgumentException.class, () -> OptionsParser.parseMoveDirections(arr).toArray());
    }
}