package ua.scudy.server.mail.template;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Data
@Slf4j
public abstract class MailTemplate {
    private final ua.scudy.server.constants.MailTemplate mailType;

    public String buildTemplate(String... args) {
        try {
            var resourceInputStream = new ClassPathResource(String.format("templates/%s", mailType.getValue()))
                    .getInputStream();
            byte[] byteData = FileCopyUtils.copyToByteArray(resourceInputStream);
            var content = new String(byteData, StandardCharsets.UTF_8);
            return String.format(content, (Object[]) Arrays.stream(args).toArray(String[]::new));
        } catch (IOException e) {
            log.error("Error reading byte data for mail template");
        }
        return null;
    }
}
