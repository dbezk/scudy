package ua.scudy.server.entity.email;

import lombok.Data;
import lombok.NoArgsConstructor;
import ua.scudy.server.constants.RoleType;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity @Data @NoArgsConstructor
@Table(name = "confirmation_tokens")
public class EmailConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String token;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "expires_at", nullable = false)
    private LocalDateTime expiresAt;

    @Column(name = "confirmed_at")
    private LocalDateTime confirmedAt;

    @Column(name = "email")
    private String email;

    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private RoleType userRole;

    public EmailConfirmationToken(String token, LocalDateTime createdAt, LocalDateTime expiresAt, String email,
                                  RoleType userRole) {
        this.token = token;
        this.createdAt = createdAt;
        this.expiresAt = expiresAt;
        this.email = email;
        this.userRole = userRole;
    }
}
