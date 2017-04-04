package giraffe.controller.activity.complex;

import giraffe.domain.activity.complex.Period;
import giraffe.service.activity.NoActivityWithCurrentUuidException;
import giraffe.service.activity.complex.GiraffeAccessDeniedException;
import giraffe.service.activity.complex.PeriodManagementService;
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
@RequestMapping("/period")
public class PeriodController {

    private PeriodManagementService periodManagementService;

    private TokenStore tokenStore;

    @Autowired
    public PeriodController(PeriodManagementService periodManagementService, TokenStore tokenStore) {
        this.periodManagementService = periodManagementService;
        this.tokenStore = tokenStore;
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    Period create(OAuth2Authentication auth, @RequestBody PeriodWrapper wrapper) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return periodManagementService.updateOrCreate(userUuid, wrapper.getPeriod(), wrapper.getProjectUuid());
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "",
            method = RequestMethod.PATCH,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Period update(OAuth2Authentication auth, @RequestBody PeriodWrapper wrapper) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return periodManagementService.updateOrCreate(userUuid, wrapper.getPeriod(), wrapper.getProjectUuid());
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(
            value = "",
            params = {"uuid"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Period find(OAuth2Authentication auth, @PathVariable String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return periodManagementService.findByUuid(userUuid, uuid);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.FOUND)
    @RequestMapping(
            value = "",
            params = {"project-uuid"},
            method = RequestMethod.GET,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Iterable<Period> findByProject(OAuth2Authentication auth, @PathVariable(name = "project-uuid") String uuid) throws GiraffeAccessDeniedException, NoActivityWithCurrentUuidException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return periodManagementService.findByProject(userUuid, uuid);
    }

    @PreAuthorize("hasRole('USER')")
    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(
            value = "",
            method = RequestMethod.DELETE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    Period delete(OAuth2Authentication auth, @RequestBody String uuid) throws GiraffeAccessDeniedException {
        OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) auth.getDetails();
        String userUuid = tokenStore.readAccessToken(details.getTokenValue()).getAdditionalInformation().get("user_uuid").toString();

        return periodManagementService.delete(userUuid, uuid);
    }

    private static class PeriodWrapper {

        private String projectUuid;

        private Period period;

        public PeriodWrapper() {
        }

        public String getProjectUuid() {
            return projectUuid;
        }

        public void setProjectUuid(String projectUuid) {
            this.projectUuid = projectUuid;
        }

        public Period getPeriod() {
            return period;
        }

        public void setPeriod(Period period) {
            this.period = period;
        }
    }

}
