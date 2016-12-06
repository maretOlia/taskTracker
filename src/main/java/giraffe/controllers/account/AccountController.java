package giraffe.controllers.account;

import giraffe.domain.GiraffeException;
import giraffe.domain.account.User;
import giraffe.security.GiraffePrivateUserDetails;
import giraffe.service.account.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

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


    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resource<User>> createAccount(@RequestParam final String login, @RequestParam final String password) throws GiraffeException.AccountWithCurrentLoginExistsException {
        final User account = userManagementService.createAccount(login, password);
        Resource<User> resource = new Resource<>(account);
        resource.add(linkTo(methodOn(AccountController.class).showAccount()).withSelfRel());
        resource.add(linkTo(methodOn(AccountController.class).deleteAccount()).withRel("delete"));

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resource<User>> showAccount() {
       final GiraffePrivateUserDetails userDetails = (GiraffePrivateUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        final User privateAccount = userManagementService.findPrivateAccount(userDetails.getUuid());

        Resource<User> resource = new Resource<>(privateAccount);
        resource.add(linkTo(methodOn(AccountController.class).showAccount()).withSelfRel());

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resource<User>> deleteAccount() {
        final GiraffePrivateUserDetails userDetails = (GiraffePrivateUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        final User privateAccount = userManagementService.deletePrivateAccount(userDetails.getUuid());

        Resource<User> resource = new Resource<>(privateAccount);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
