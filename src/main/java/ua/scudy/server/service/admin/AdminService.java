package ua.scudy.server.service.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.scudy.server.entity.news.News;
import ua.scudy.server.entity.user.ScudyRole;
import ua.scudy.server.repository.news.NewsRepo;
import ua.scudy.server.repository.user.RoleRepo;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AdminService {
    private final RoleRepo scudyRoleRepo;
    private final NewsRepo newsRepo;

    public ScudyRole saveRole(ScudyRole scudyRole) {
        return scudyRoleRepo.save(scudyRole);
    }

    public void createNews(String title, String text) {
        newsRepo.save(new News(title, text));
    }

}
