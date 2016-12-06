package giraffe.service.security;

import giraffe.domain.account.User;
import giraffe.repository.user.UserRepository;
import giraffe.security.GiraffePrivateUserDetails;
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
    UserRepository userRepository;


    @Override
    public GiraffePrivateUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
       final User account = userRepository.findByLogin(username);

        if (account == null)
            throw new UsernameNotFoundException("Private account with login: " + username + " not found");

        return new GiraffePrivateUserDetails(account.getLogin(), account.getPasswordHash(), account.getAuthorities(), account.getUuid(), false, false, false, true);
    }

}
