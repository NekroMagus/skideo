package net.skideo.controller;

import net.skideo.model.User;
import net.skideo.model.enums.ServiceRole;
import net.skideo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/authenticate")
    public ResponseEntity<OAuth2AccessToken> authenticate(@RequestParam String login, @RequestParam String password, @RequestParam String clientId,
                                                          @RequestParam String clientSecret, @RequestParam String grantType, @RequestParam ServiceRole serviceRole, HttpServletRequest request) throws IllegalAccessException, HttpRequestMethodNotSupportedException {
        if(serviceRole!=ServiceRole.ADMIN) {
            User user = userService.findByLogin(login);

            if (user.getServiceRole() != serviceRole) {
                throw new IllegalAccessException();
            }
        }

        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", login);
        parameters.put("password", password);
        parameters.put("client_secret", clientSecret);
        parameters.put("grant_type", grantType);
        parameters.put("scope", "read write");

        return userService.generateToken(parameters, clientId);
    }

    @PostMapping("/token")
    public ResponseEntity<OAuth2AccessToken> generateToken(@RequestParam String login, @RequestParam String password, @RequestParam String clientId,
                                                           @RequestParam String clientSecret, @RequestParam String grantType) throws HttpRequestMethodNotSupportedException {
        Map<String, String> parameters = new HashMap<>();
        parameters.put("username", login);
        parameters.put("password", password);
        parameters.put("client_secret", clientSecret);
        parameters.put("grant_type", grantType);
        parameters.put("scope", "read write");

        return userService.generateToken(parameters, clientId);
    }

}
