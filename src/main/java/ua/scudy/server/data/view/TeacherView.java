package ua.scudy.server.data.view;


import lombok.Builder;
import lombok.Data;
import ua.scudy.server.entity.user.ScudyTeacher;

@Data
@Builder
public class TeacherView {

    private ScudyTeacher teacher;

    private boolean isFollow;

}
