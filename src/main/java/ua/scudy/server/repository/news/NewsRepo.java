package ua.scudy.server.repository.news;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.news.News;

@Repository
public interface NewsRepo extends JpaRepository<News, Long> {
}
