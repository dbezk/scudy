package ua.scudy.server.data.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentCondition {

    @NotNull
    @JsonProperty("assignment_condition_text")
    private String assignmentConditionText;

    @NotNull
    @JsonProperty("assignment_condition_code")
    private String assignmentConditionCode;

//    @NotNull
    @JsonProperty("method_name")
    private String methodName;

}
