package ua.scudy.server.data.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentTestCaseData {

    // TODO: allow only String, int, float, double in request
    @NotNull
    @JsonProperty("argument_type")
    private String argumentType;

    @JsonProperty("argument_value")
    private String argumentValue;

    @JsonProperty("argument_array_size")
    private int argumentArraySize;

    @JsonProperty("argument_array_values")
    private Object[] argumentArrayValues;

    @JsonProperty("output_string")
    private String outputString;

}
