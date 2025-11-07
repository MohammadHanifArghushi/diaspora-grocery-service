package fi.haagahelia.diaspora_grocery_service.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fi.haagahelia.diaspora_grocery_service.domain.User;
import fi.haagahelia.diaspora_grocery_service.domain.UserRepository;

@Service 
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    //  we inject UserRepository using this constructor
    public UserDetailServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // here we throw UsernameNotFoundException if user is not found
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        
        Optional<User> user = userRepository.findByUsername(username);

        // if user is not found this will throw the exception
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        User foundUser = user.get();

        
        return org.springframework.security.core.userdetails.User.builder()
                .username(foundUser.getUsername())
                .password(foundUser.getPassword()) 
                .roles(foundUser.getRole()) 
                .build();
    }
}