package agh.ics.oop;

import agh.ics.oop.model.MoveDirection;

import java.util.ArrayList;
import java.util.List;

public class OptionsParser {
    public static List<MoveDirection> parseMoveDirections(String[] args) {
        List<MoveDirection> moveDirections = new ArrayList<>();
        for (String arg : args) {
            switch (arg) {
                case "f" -> moveDirections.add(MoveDirection.FORWARD);
                case "b" -> moveDirections.add(MoveDirection.BACKWARD);
                case "r" -> moveDirections.add(MoveDirection.RIGHT);
                case "l" -> moveDirections.add(MoveDirection.LEFT);
                default -> throw new IllegalArgumentException(arg + " is not legal move specification");
            }
        }
        return moveDirections;
    }
}
