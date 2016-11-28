package giraffe.controllers.household;

import giraffe.domain.GiraffeException;
import giraffe.domain.user.PrivateAccount;
import giraffe.security.GiraffePrivateUserDetails;
import giraffe.service.account.AccountManagementService;
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
@RequestMapping("/private/account")
public class PrivateAccountController {

    @Autowired
    AccountManagementService userManagementService;


    @RequestMapping(method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resource<PrivateAccount>> createAccount(@RequestParam final String login, @RequestParam final String password) throws GiraffeException.AccountWithCurrentLoginExistsException {
        final PrivateAccount account = userManagementService.createPrivateAccount(login, password);
        Resource<PrivateAccount> resource = new Resource<>(account);
        resource.add(linkTo(methodOn(PrivateAccountController.class).showAccount()).withSelfRel());
        resource.add(linkTo(methodOn(PrivateAccountController.class).deleteAccount()).withRel("delete"));

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resource<PrivateAccount>> showAccount() {
       final GiraffePrivateUserDetails userDetails = (GiraffePrivateUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        final PrivateAccount privateAccount = userManagementService.findPrivateAccount(userDetails.getUuid());

        Resource<PrivateAccount> resource = new Resource<>(privateAccount);
        resource.add(linkTo(methodOn(PrivateAccountController.class).showAccount()).withSelfRel());

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PATCH, produces = MediaType.APPLICATION_JSON_VALUE)
    HttpEntity<Resource<PrivateAccount>> deleteAccount() {
        final GiraffePrivateUserDetails userDetails = (GiraffePrivateUserDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        final PrivateAccount privateAccount = userManagementService.deletePrivateAccount(userDetails.getUuid());

        Resource<PrivateAccount> resource = new Resource<>(privateAccount);

        return new ResponseEntity<>(resource, HttpStatus.OK);
    }

}
