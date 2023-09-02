package ua.scudy.server.data.assignment.solution;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import ua.scudy.server.constants.SolutionResult;

@Getter
@Setter
public class FailedSolutionMessage extends SolutionType {

    private final String message;

    public FailedSolutionMessage(String message) {
        super(SolutionResult.FAIL);
        this.message = message;
    }
}
