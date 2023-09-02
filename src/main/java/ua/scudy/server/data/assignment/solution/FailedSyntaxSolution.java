package ua.scudy.server.data.assignment.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import ua.scudy.server.constants.SolutionResult;

import java.util.List;

@Getter
@Setter
public class FailedSyntaxSolution extends SolutionType {

    @JsonProperty("syntax_errors")
    private final List<String> syntaxErrors;

    public FailedSyntaxSolution(List<String> syntaxErrors) {
        super(SolutionResult.FAIL);
        this.syntaxErrors = syntaxErrors;
    }
}
