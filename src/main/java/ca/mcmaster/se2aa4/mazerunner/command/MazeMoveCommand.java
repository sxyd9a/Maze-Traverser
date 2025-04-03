package ca.mcmaster.se2aa4.mazerunner.command;

//interface that includes maze ops
public interface MazeMoveCommand {
    void execute();
    void undo();
}
