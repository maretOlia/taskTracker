package giraffe.controller.activity.complex;

import giraffe.domain.activity.complex.ComplexTask;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.complex.ComplexTaskManagementService;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
import giraffe.service.activity.complex.TimeScheduledAndPeriodInconsistencyException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@RestController
@RequestMapping("/complex-task")
public class ComplexTaskController {

    private ComplexTaskManagementService complexTaskManagementService;

    private TokenStore tokenStore;

    @Autowired
    public ComplexTaskController(ComplexTaskManagementService complexTaskManagementService, TokenStore tokenStore) {
        this.complexTaskManagementService = complexTaskManagementService;
        this.tokenStore = tokenStore;
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    ComplexTask create(OAuth2Authentication auth, @RequestBody ComplexTaskWrapper wrapper) throws GiraffeAccessDeniedException, TimeScheduledAndPeriodInconsistencyException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return complexTaskManagementService.updateOrCreate(userUuid,
                wrapper.getTask(),
                wrapper.getAssignedToUuid(),
                wrapper.getParentUuid(),
                wrapper.getProjectUuid(),
                wrapper.getPeriodUuid(),
                wrapper.getProgress());
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ComplexTask update(OAuth2Authentication auth, @RequestBody ComplexTaskWrapper wrapper) throws GiraffeAccessDeniedException, TimeScheduledAndPeriodInconsistencyException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return complexTaskManagementService.updateOrCreate(userUuid,
                wrapper.getTask(),
                wrapper.getAssignedToUuid(),
                wrapper.getParentUuid(),
                wrapper.getProjectUuid(),
                wrapper.getPeriodUuid(),
                wrapper.getProgress());
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(
            value = "",
            params = {"uuid"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ComplexTask find(OAuth2Authentication auth, @PathVariable String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return complexTaskManagementService.findByUuid(userUuid, uuid);
    }

    //TODO implement filters

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    ComplexTask delete(OAuth2Authentication auth, @RequestBody String uuid) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return complexTaskManagementService.delete(userUuid, uuid);
    }

    private static class ComplexTaskWrapper implements Serializable {

        private ComplexTask task;

        private String parentUuid;

        private String assignedToUuid;

        private String projectUuid;

        private String periodUuid;

        private double progress;

        public ComplexTaskWrapper() {
        }

        public ComplexTask getTask() {
            return task;
        }

        public void setTask(ComplexTask task) {
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

        public String getProjectUuid() {
            return projectUuid;
        }

        public void setProjectUuid(String projectUuid) {
            this.projectUuid = projectUuid;
        }

        public String getPeriodUuid() {
            return periodUuid;
        }

        public void setPeriodUuid(String periodUuid) {
            this.periodUuid = periodUuid;
        }

        public double getProgress() {
            return progress;
        }

        public void setProgress(double progress) {
            this.progress = progress;
        }
    }

}