package giraffe.service;

import giraffe.GiraffeApplicationTestCase;
import giraffe.repository.user.AccountRepository;
import giraffe.service.account.AccountManagementService;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
public class AccountManagementServiceTest extends GiraffeApplicationTestCase {

    @Autowired
    AccountManagementService userManagementService;

    @Autowired
    AccountRepository accountRepository;

}
