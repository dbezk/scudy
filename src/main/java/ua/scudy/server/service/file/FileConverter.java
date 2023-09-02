package ua.scudy.server.service.file;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.data.assignment.AssignmentData;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileConverter {

    public AssignmentData convertAssignmentFileToObject(File assignmentFile) {
        AssignmentData assignmentData = null;
        try
        {
            JAXBContext jaxbContext = JAXBContext.newInstance(AssignmentData.class);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            assignmentData = (AssignmentData) jaxbUnmarshaller.unmarshal(assignmentFile);

            log.info("converted file object = {}", assignmentData);
        }
        catch (JAXBException e)
        {
            e.printStackTrace();
        }
        return assignmentData;
    }



}
