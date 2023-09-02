package ua.scudy.server.service.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.entity.news.News;
import ua.scudy.server.repository.news.NewsRepo;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class UserSharedService {

    private NewsRepo newsRepo;

    public List<News> getNews() {
        return newsRepo.findAll();
    }

}
