package ua.scudy.server.data.assignment;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import ua.scudy.server.entity.assignment.Assignment;
import ua.scudy.server.entity.user.ScudyTeacher;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AssignmentFullInfo {

    @JsonProperty("is_follow")
    private boolean studentFollow = false;
    @JsonProperty("is_done")
    private boolean isDone = false;
    @JsonProperty("is_rated")
    private boolean isRated = false;
    @JsonProperty("user_failed_count")
    private int userFailedSolutionsCount = 0;
    @JsonProperty("done_solutions_count")
    private int doneSolutionsCount = 0;
    @JsonProperty("failed_solutions_count")
    private int failedSolutionsCount = 0;
    @JsonProperty("rating")
    private double rating = 0.0;
    @JsonProperty("assignment_data")
    private AssignmentData assignmentData;
    @JsonProperty("teacher")
    private ScudyTeacher scudyTeacher;

}
