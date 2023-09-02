package ua.scudy.server.data.assignment.solution;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ua.scudy.server.constants.SolutionResult;

@Getter
@Setter
public class FailedSolution extends SolutionType {

    @JsonProperty("test_cases_amount")
    private final int testCasesAmount;

    @JsonProperty("failed_test_case_number")
    private final int testCaseNumber;

    @JsonProperty("failed_test_case_type")
    private final String testCaseType;

    @JsonProperty("failed_test_case_is_array")
    private final boolean testCaseIsArray;

    @JsonProperty("failed_test_case_value")
    private final String testCaseValue;


    public FailedSolution(int testCasesAmount, int testCaseNumber, String testCaseType,
                          boolean testCaseIsArray, String testCaseValue) {
        super(SolutionResult.FAIL);
        this.testCasesAmount = testCasesAmount;
        this.testCaseNumber = testCaseNumber;
        this.testCaseType = testCaseType;
        this.testCaseIsArray = testCaseIsArray;
        this.testCaseValue = testCaseValue;
    }


}
