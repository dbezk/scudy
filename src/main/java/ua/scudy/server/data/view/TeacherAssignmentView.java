package ua.scudy.server.data.view;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import ua.scudy.server.data.assignment.AssignmentData;
import ua.scudy.server.entity.assignment.AssignmentStatistics;

@Data
@Builder
public class TeacherAssignmentView {

    @JsonProperty("assignment_data")
    private AssignmentData assignmentData;

    @JsonProperty("assignment_statistics")
    private AssignmentStatistics assignmentStatistics;

    private double rating = 0.0;

}
