package ua.scudy.server.data.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.util.List;


/*
Receive info about task (example: resources/file.xml)
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class AssignmentData {

    @NotNull
    @JsonProperty("assignment_title")
    private String assignmentTitle;

    @NotNull
    @JsonProperty("assignment_description")
    private String assignmentDescription;

    @NotNull
    @JsonProperty("assignment_condition")
    private AssignmentCondition assignmentCondition;

    @NotNull
    @JsonProperty("assignment_condition_testcases")
    @XmlElementWrapper(name="assignmentConditionTestCases")
    @XmlElement(name="assignmentConditionTestCase")
    private List<AssignmentConditionTestCaseData> assignmentConditionTestCaseDataList;

    @JsonProperty("assignment_hints")
    private AssignmentHintsData assignmentHintsDataList;

    @NotNull
    @JsonProperty("assignment_testcases")
    @XmlElementWrapper(name="assignmentTestCases")
    @XmlElement(name="assignmentTestCase")
    private List<AssignmentTestCaseData> assignmentTestCaseData;

}
