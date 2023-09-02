package ua.scudy.server.api.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.scudy.server.data.news.NewsData;
import ua.scudy.server.service.admin.AdminService;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminApi {

    private final AdminService adminService;

    @PostMapping(value = "/news/create")
    public ResponseEntity<?> createNews(@RequestBody NewsData newsData) {
        adminService.createNews(newsData.getTitle(), newsData.getText());
        return ResponseEntity.ok().build();
    }

}
