package ca.mcmaster.se2aa4.mazerunner.command;

import java.util.Stack;

public class MazeMoveHistory {
    private final Stack<MazeMoveCommand> stack = new Stack<>(); //push and pop commands used from this

    public void push(MazeMoveCommand command) {
        stack.push(command);
    }

    public MazeMoveCommand pop() {
        return stack.isEmpty() ? null : stack.pop();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }
}
