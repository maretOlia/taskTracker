package giraffe.service;

import giraffe.GiraffeTrackerApplicationTestCase;
import giraffe.repository.UserRepository;
import giraffe.service.account.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class AccountManagementServiceTest extends GiraffeTrackerApplicationTestCase {

    private UserManagementService userManagementService;

    private UserRepository userRepository;

    @Autowired
    public AccountManagementServiceTest(UserManagementService userManagementService, UserRepository userRepository) {
        this.userManagementService = userManagementService;
        this.userRepository = userRepository;
    }
}
