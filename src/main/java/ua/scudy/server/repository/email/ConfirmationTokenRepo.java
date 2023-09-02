package ua.scudy.server.repository.email;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.scudy.server.entity.email.EmailConfirmationToken;

@Repository
public interface ConfirmationTokenRepo extends JpaRepository<EmailConfirmationToken, Long> {

    EmailConfirmationToken findByToken(String token);

}
