package ua.scudy.server.api.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import ua.scudy.server.service.token.TokensService;

@RestController
@RequestMapping("/api/control")
@RequiredArgsConstructor
public class UserControlsApi {

    private final TokensService tokensService;

    @GetMapping("/confirm")
    public String activeUserAccount(@RequestParam(name = "token") String token) {
        return tokensService.confirmEmailConfirmationToken(token);
    }

}
