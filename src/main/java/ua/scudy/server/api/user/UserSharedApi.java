package ua.scudy.server.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ua.scudy.server.entity.news.News;
import ua.scudy.server.service.user.UserSharedService;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserSharedApi {

    private final UserSharedService userSharedService;

    @GetMapping(value = "/news")
    public ResponseEntity<List<News>> getNews() {
        return ResponseEntity.ok().body(userSharedService.getNews());
    }

}
