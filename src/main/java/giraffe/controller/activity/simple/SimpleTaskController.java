package giraffe.controller.activity.simple;

import giraffe.domain.activity.simple.SimpleTask;
import giraffe.service.activity.InvalidInputException;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
import giraffe.service.activity.simple.SimpleTaskManagementService;
import giraffe.utils.ValidationHelper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@RestController
@RequestMapping("/simple-task")
public class SimpleTaskController {

    private SimpleTaskManagementService simpleTaskManagementService;

    private TokenStore tokenStore;

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleTask create(OAuth2Authentication auth, @RequestBody @Valid SimpleTaskWrapper wrapper, BindingResult bindingResult) throws GiraffeAccessDeniedException, InvalidInputException {

        ValidationHelper.validateInputFields(bindingResult);

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return simpleTaskManagementService.updateOrCreate(userUuid,
                wrapper.getTask(),
                wrapper.getParentUuid(),
                wrapper.getAssignedToUuid(),
                wrapper.getSimpleToDoListUuid());
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleTask update(OAuth2Authentication auth, @RequestBody @Valid SimpleTaskWrapper wrapper, BindingResult bindingResult) throws GiraffeAccessDeniedException, InvalidInputException {

        ValidationHelper.validateInputFields(bindingResult);

        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return simpleTaskManagementService.updateOrCreate(userUuid,
                wrapper.getTask(),
                wrapper.getParentUuid(),
                wrapper.getAssignedToUuid(),
                wrapper.getSimpleToDoListUuid());
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(
            value = "/{uuid}",
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleTask find(OAuth2Authentication auth, @PathVariable("uuid") String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return simpleTaskManagementService.findByUuid(userUuid, uuid);
    }

    //TODO implement filters

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "/{uuid}",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    SimpleTask delete(OAuth2Authentication auth, @PathVariable("uuid") String uuid) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return simpleTaskManagementService.delete(userUuid, uuid);
    }

    private static class SimpleTaskWrapper implements Serializable {

        @NotNull
        @Valid
        private SimpleTask task;

        private String parentUuid;

        private String assignedToUuid;

        @NotNull
        private String simpleToDoListUuid;

        public SimpleTaskWrapper() {
        }

        public SimpleTask getTask() {
            return task;
        }

        public void setTask(SimpleTask task) {
            this.task = task;
        }

        public String getParentUuid() {
            return parentUuid;
        }

        public void setParentUuid(String parentUuid) {
            this.parentUuid = parentUuid;
        }

        public String getAssignedToUuid() {
            return assignedToUuid;
        }

        public void setAssignedToUuid(String assignedToUuid) {
            this.assignedToUuid = assignedToUuid;
        }

        public String getSimpleToDoListUuid() {
            return simpleToDoListUuid;
        }

        public void setSimpleToDoListUuid(String simpleToDoListUuid) {
            this.simpleToDoListUuid = simpleToDoListUuid;
        }
    }

}
