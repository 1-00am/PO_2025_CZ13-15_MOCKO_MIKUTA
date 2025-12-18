package agh.ics.oop.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Vector2dTest {
    @Test
    void equalsItself() {
        var vec = new Vector2d(1, 2);
        assertTrue(vec.equals(vec));
    }

    @Test
    void notEqualsDifferent() {
        var vec1 = new Vector2d(-1, 2);
        var vec2 = new Vector2d(100, 0);
        assertFalse(vec1.equals(vec2));
    }

    @Test
    void notEqualsNull() {
        var vec = new Vector2d(1, 1);
        assertFalse(vec.equals(null));
    }

    @Test
    void EqualsSame() {
        var vec1 = new Vector2d(1, 1);
        var vec2 = new Vector2d(1, 1);
        assertTrue(vec1.equals(vec2));
    }

    @Test
    void toStringTest() {
        assertEquals("(-1,0)", new Vector2d(-1, 0).toString());
    }

    @Test
    void precedesTrue() {
        assertTrue(new Vector2d(1, 1).precedes(new Vector2d(2, 2))); // upper right
        assertTrue(new Vector2d(1, 1).precedes(new Vector2d(1, 1))); // same
    }

    @Test
    void precedesFalse() {
        assertFalse(new Vector2d(-1, 0).precedes(new Vector2d(2, -1))); // lower right
        assertFalse(new Vector2d(0, 1).precedes(new Vector2d(-1, -2))); // lower left
        assertFalse(new Vector2d(1, 0).precedes(new Vector2d(0, 1))); // upper left
    }

    @Test
    void followsTrue() {
        assertTrue(new Vector2d(1, 1).follows(new Vector2d(0, 0))); // lower left
        assertTrue(new Vector2d(1, 1).follows(new Vector2d(1, 1))); // same
    }

    @Test
    void followFalse() {
        assertFalse(new Vector2d(-1, 0).follows(new Vector2d(-2, 2))); // upper left
        assertFalse(new Vector2d(0, 1).follows(new Vector2d(1, 2))); // upper right
        assertFalse(new Vector2d(1, 0).follows(new Vector2d(3, -3))); // lower right
    }

    @Test
    void upperRight() {
        assertEquals(new Vector2d(1, 1), new Vector2d(1,1).upperRight(new Vector2d(1,1)));
        assertEquals(new Vector2d(2, 1), new Vector2d(-1,1).upperRight(new Vector2d(2,-1)));
        assertEquals(new Vector2d(1, 2), new Vector2d(1,-1).upperRight(new Vector2d(-1, 2)));
    }

    @Test
    void lowerLeft() {
        assertEquals(new Vector2d(1, 1), new Vector2d(1,1).lowerLeft(new Vector2d(1,1)));
        assertEquals(new Vector2d(-1, -1), new Vector2d(-1,1).lowerLeft(new Vector2d(2,-1)));
        assertEquals(new Vector2d(-1, -1), new Vector2d(1,-1).lowerLeft(new Vector2d(-1, 2)));
    }

    @Test
    void add() {
        var vec1 = new Vector2d(1, -1);
        var vec2 = new Vector2d(2, 3);
        assertEquals(new Vector2d(3, 2), vec1.add(vec2));
    }

    @Test
    void subtract() {
        var vec1 = new Vector2d(1, -1);
        var vec2 = new Vector2d(2, 3);
        assertEquals(new Vector2d(-1, -4), vec1.subtract(vec2));
    }

    @Test
    void opposite() {
        assertEquals(new Vector2d(1, -2), new Vector2d(-1, 2).opposite());
        assertEquals(new Vector2d(0, 0), new Vector2d(0, 0).opposite());
    }
}