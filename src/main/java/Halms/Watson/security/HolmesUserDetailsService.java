package Halms.Watson.security;

import Halms.Watson.model.entity.Users;
import Halms.Watson.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HolmesUserDetailsService implements UserDetailsService {

        private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Users users = userRepository.getUserByUsername(username);
        if (users == null) {
            throw new UsernameNotFoundException("Could not find user");
        }

        return new HolmesUserDetails(users);
    }
}
