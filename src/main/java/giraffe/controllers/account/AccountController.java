package giraffe.controllers.account;

import giraffe.AccountWithCurrentLoginExistsException;
import giraffe.domain.User;
import giraffe.service.account.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Controller
@RequestMapping("/account")
public class AccountController {

    @Autowired
    UserManagementService userManagementService;

    @Autowired
    private TokenStore tokenStore;

    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.CREATED)
    HttpEntity<Resource<User>> createAccount(@RequestParam String login, @RequestParam String password) throws AccountWithCurrentLoginExistsException {
        User account = userManagementService.createAccount(login, password);
        Resource<User> resource = new Resource<>(account);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.FOUND)
    HttpEntity<Resource<User>> showAccount(OAuth2Authentication auth) {

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String uuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        User privateAccount = userManagementService.findPrivateAccount(uuid);

        Resource<User> resource = new Resource<>(privateAccount);
        resource.add(linkTo(methodOn(AccountController.class).deleteAccount(auth)).withRel("delete"));

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @PreAuthorize("hasRole('USER')")
    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    HttpEntity<Resource<User>> deleteAccount(OAuth2Authentication auth) {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();

        String uuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        User privateAccount = userManagementService.deletePrivateAccount(uuid);

        Resource<User> resource = new Resource<>(privateAccount);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
