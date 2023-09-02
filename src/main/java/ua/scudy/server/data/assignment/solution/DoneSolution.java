package ua.scudy.server.data.assignment.solution;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import ua.scudy.server.constants.SolutionResult;

@Getter
public class DoneSolution extends SolutionType {

    private final String message;

    public DoneSolution(String message) {
        super(SolutionResult.DONE);
        this.message = message;
    }
}
