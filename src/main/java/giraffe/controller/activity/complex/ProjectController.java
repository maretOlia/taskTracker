package giraffe.controller.activity.complex;

import giraffe.domain.activity.complex.Project;
import giraffe.domain.activity.complex.security.ProjectUserRights;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
import giraffe.service.activity.complex.ProjectManagementService;
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
@RequestMapping("/project")
public class ProjectController {

    private ProjectManagementService projectManagementService;

    private TokenStore tokenStore;

    @Autowired
    public ProjectController(ProjectManagementService projectManagementService, TokenStore tokenStore) {
        this.projectManagementService = projectManagementService;
        this.tokenStore = tokenStore;
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    Project create(OAuth2Authentication auth, @RequestBody Project project) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return projectManagementService.updateOrCreate(userUuid, project);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Project update(OAuth2Authentication auth, @RequestBody Project project) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return projectManagementService.updateOrCreate(userUuid, project);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(
            value = "",
            params = {"uuid"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Project find(OAuth2Authentication auth, @PathVariable String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return projectManagementService.findByUuid(userUuid, uuid);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(
            value = "",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Iterable<Project> findByCreatedBy(OAuth2Authentication auth) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return projectManagementService.findByCreatedBy(userUuid);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "",
            params = {"uuid", "user-uuid"},
            method = RequestMethod.POST,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Project addRights(OAuth2Authentication auth, @RequestBody UserRightsWrapper wrapper) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return projectManagementService.addUserWithRights(userUuid, wrapper.getUserToAddUuid(), wrapper.getProjectUuid(), wrapper.getRights());
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Project delete(OAuth2Authentication auth, @RequestBody String uuid) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return projectManagementService.delete(userUuid, uuid);
    }

    private static class UserRightsWrapper {

        private String projectUuid;

        private String userToAddUuid;

        private ProjectUserRights.Rights rights;

        public UserRightsWrapper() {
        }

        public String getProjectUuid() {
            return projectUuid;
        }

        public void setProjectUuid(String projectUuid) {
            this.projectUuid = projectUuid;
        }

        public String getUserToAddUuid() {
            return userToAddUuid;
        }

        public void setUserToAddUuid(String userToAddUuid) {
            this.userToAddUuid = userToAddUuid;
        }

        public ProjectUserRights.Rights getRights() {
            return rights;
        }

        public void setRights(ProjectUserRights.Rights rights) {
            this.rights = rights;
        }
    }

}
