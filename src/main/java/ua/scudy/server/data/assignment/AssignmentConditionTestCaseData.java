package ua.scudy.server.data.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentConditionTestCaseData {

    @NotNull
    @JsonProperty("argument_type")
    private String argType;

    @NotNull
    @JsonProperty("input_data")
    private String inputData;

    @NotNull
    @JsonProperty("output_data")
    private String outputData;

}
