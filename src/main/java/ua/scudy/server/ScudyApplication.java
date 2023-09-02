package ua.scudy.server;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import ua.scudy.server.constants.RoleType;
import ua.scudy.server.data.StudentCreateData;
import ua.scudy.server.data.TeacherCreateData;
import ua.scudy.server.entity.user.ScudyRole;
import ua.scudy.server.service.admin.AdminService;
import ua.scudy.server.service.auth.AuthService;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class ScudyApplication {

	@Value("${scudy.file.assignments.path}")
	private String assignmentsPath;
	@Value("${scudy.file.assignments.solutions.path}")
	private String solutionsPath;

	public static void main(String[] args) {
		SpringApplication.run(ScudyApplication.class, args);
	}


	@Bean
	CommandLineRunner run(AdminService adminService, AuthService authService) {
		return args -> {
			var assignmentPathFolder = Paths.get(String.format("%s", assignmentsPath));
			var solutionsPathFolder = Paths.get(String.format("%s", solutionsPath));
			if(!Files.exists(assignmentPathFolder)) {
				new File(assignmentsPath).mkdir();
			}
			if(!Files.exists(solutionsPathFolder)) {
				new File(solutionsPath).mkdir();
			}

			adminService.saveRole(new ScudyRole(RoleType.TEACHER));
			adminService.saveRole(new ScudyRole(RoleType.STUDENT));
		};
	}

}
