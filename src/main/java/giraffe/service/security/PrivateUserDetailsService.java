package giraffe.service.security;

import giraffe.domain.user.PrivateAccount;
import giraffe.repository.user.PrivateAccountRepository;
import giraffe.security.GiraffeUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Service
public class PrivateUserDetailsService implements UserDetailsService {

    @Autowired
    PrivateAccountRepository privateAccountRepository;


    @Override
    public GiraffeUserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
       final PrivateAccount account = privateAccountRepository.findByLogin(username);

        if (account == null)
            throw new UsernameNotFoundException("Private account with login: " + username + " not found");

        return new GiraffeUserDetails(account.getLogin(), account.getPasswordHash(), account.getAuthorities(), account.getUuid(), false, false, false, true);
    }

}
