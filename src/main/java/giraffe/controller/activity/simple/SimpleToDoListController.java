package giraffe.controller.activity.simple;

import giraffe.domain.activity.simple.SimpleToDoList;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
import giraffe.service.activity.simple.SimpleToDoListManagementService;
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
@RequestMapping("/simple-list")
public class SimpleToDoListController {

    private SimpleToDoListManagementService simpleToDoListManagementService;

    private TokenStore tokenStore;

    @Autowired
    public SimpleToDoListController(SimpleToDoListManagementService simpleToDoListManagementService, TokenStore tokenStore) {
        this.simpleToDoListManagementService = simpleToDoListManagementService;
        this.tokenStore = tokenStore;
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleToDoList create(OAuth2Authentication auth, @RequestBody SimpleToDoList simpleToDoList) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return simpleToDoListManagementService.updateOrCreate(userUuid, simpleToDoList);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleToDoList update(OAuth2Authentication auth, @RequestBody SimpleToDoList simpleToDoList) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return simpleToDoListManagementService.updateOrCreate(userUuid, simpleToDoList);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(
            value = "/{uuid}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleToDoList find(OAuth2Authentication auth, @PathVariable("uuid") String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return simpleToDoListManagementService.findByUuid(userUuid, uuid);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(
            value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Iterable<SimpleToDoList> findByCreatedBy(OAuth2Authentication auth) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return simpleToDoListManagementService.findByCreatedBy(userUuid);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "/{uuid}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleToDoList delete(OAuth2Authentication auth,  @PathVariable("uuid") String uuid) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return simpleToDoListManagementService.delete(userUuid, uuid);
    }

}
