package ohsoontaxi.login.global.security.Auth;

import lombok.RequiredArgsConstructor;
import ohsoontaxi.login.domain.user.domain.User;
import ohsoontaxi.login.domain.user.domain.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AuthDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        User user =
                userRepository
                        .findById(Long.valueOf(id))
                        .orElseThrow(() -> new UsernameNotFoundException("not found"));
        return new AuthDetails(user.getId().toString(), user.getAccountRole().getValue());
    }
}
