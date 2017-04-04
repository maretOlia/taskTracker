package giraffe.controller.user;

import giraffe.AccountWithCurrentLoginExistsException;
import giraffe.domain.User;
import giraffe.service.user.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@RestController
@RequestMapping("/user")
public class UserController {

    private UserManagementService userManagementService;

    private TokenStore tokenStore;

    @Autowired
    public UserController(UserManagementService userManagementService, TokenStore tokenStore) {
        this.userManagementService = userManagementService;
        this.tokenStore = tokenStore;
    }

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    User create(@RequestParam String login, @RequestParam String password) throws AccountWithCurrentLoginExistsException {

        return userManagementService.create(login, password);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.FOUND)
    User find(OAuth2Authentication auth) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String uuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();
        return userManagementService.find(uuid);
    }

}
