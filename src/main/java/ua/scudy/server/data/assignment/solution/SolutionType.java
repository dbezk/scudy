package ua.scudy.server.data.assignment.solution;

import ua.scudy.server.constants.SolutionResult;

public abstract class SolutionType {

    private final SolutionResult solutionResult;

    protected SolutionType(SolutionResult solutionResult) {
        this.solutionResult = solutionResult;
    }
}
