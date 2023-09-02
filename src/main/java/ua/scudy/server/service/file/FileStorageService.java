package ua.scudy.server.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.data.assignment.AssignmentData;
import ua.scudy.server.entity.assignment.AssignmentFile;
import ua.scudy.server.repository.assignment.AssignmentFileRepo;
import ua.scudy.server.repository.assignment.AssignmentRepo;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.io.IOException;

import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class FileStorageService {

    @Value("${scudy.file.assignments.path}")
    private String assignmentsPath;

    @Value("${scudy.file.assignments.solutions.path}")
    private String solutionsPath;
    private final AssignmentRepo assignmentRepo;

    public void createAssignmentFile(AssignmentData assignmentData, String fileName) throws IOException, JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(AssignmentData.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
        File file = new File(String.format("%s%s", assignmentsPath, fileName));
        jaxbMarshaller.marshal(assignmentData, file);
    }

    public File createSolutionFile(String fileContent) throws IOException {
        // TODO: get id from DB and set as package and folder
        var tempFolderName = new StringBuilder(UUID.randomUUID().toString())
                .insert(0, "f").toString()
                .replace("-", "");

        var solutionFile = new File(String.format("%s%s/Task.java", solutionsPath, tempFolderName));
        solutionFile.getParentFile().mkdir();
        log.info("not update file content = {}", fileContent);
        var newFileContent = new StringBuilder(fileContent)
                .insert(0, String.format("package %s;", tempFolderName));
        log.info("new file content = {}", newFileContent);
        Files.writeString(solutionFile.toPath(), newFileContent);
        return solutionFile;
    }

    public AssignmentFile getAssignmentFileByAssignmentId(Long id) {
        var assignment = assignmentRepo.findById(id)
                .orElseThrow(() -> {
                    throw new UsernameNotFoundException("not found");
                });
        return assignment.getAssignmentFile();
    }

    public File getAssignmentFile(Long id) {
        var file = getAssignmentFileByAssignmentId(id);
        var assignmentFile = new File(String.format("%s%s", assignmentsPath, file.getName()));
        return assignmentFile;
    }

}
