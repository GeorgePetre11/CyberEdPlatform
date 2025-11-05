// DESIGN PATTERN: TEMPLATE METHOD PATTERN
// Location: src/main/java/org/example/cybersecurity_platform/service/CustomUserDetailsService.java
//
// Description: Implements Spring Security's UserDetailsService interface, which defines
// a template method pattern. The interface defines the algorithm skeleton for loading
// user details, and this class provides the specific implementation.
//
// Pattern Elements:
// - Abstract Class/Interface: UserDetailsService (from Spring Security)
// - Template Method: loadUserByUsername() - defines the algorithm skeleton
// - Concrete Implementation: CustomUserDetailsService - provides specific steps
// - Algorithm Steps:
//   1. Load user from repository
//   2. Extract authorities/roles
//   3. Create UserDetails object
//   4. Return user details
//
// The Spring Security framework calls this method as part of its authentication
// template algorithm, but allows us to customize how users are loaded.

package org.example.cybersecurity_platform.service;

import org.example.cybersecurity_platform.model.Role;
import org.example.cybersecurity_platform.model.User;
import org.example.cybersecurity_platform.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

/**
 * TEMPLATE METHOD PATTERN: CustomUserDetailsService
 * 
 * This class implements UserDetailsService, which is part of Spring Security's
 * template method pattern for authentication.
 * 
 * The authentication template algorithm (defined by Spring Security):
 * 1. Extract username from login request
 * 2. Call loadUserByUsername(username) ← OUR CUSTOM IMPLEMENTATION
 * 3. Compare passwords
 * 4. Check authorities
 * 5. Create authentication token
 * 6. Store in security context
 * 
 * We only need to implement step 2 - Spring Security handles the rest.
 * This is the essence of the Template Method pattern: the framework defines
 * the algorithm, but lets us customize specific steps.
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    /**
     * TEMPLATE METHOD IMPLEMENTATION
     * 
     * This method is called by Spring Security's authentication algorithm.
     * We provide the specific implementation of how to load a user, while
     * Spring Security provides the overall authentication flow template.
     * 
     * Steps we customize:
     * 1. Load user from database
     * 2. Convert roles to Spring Security authorities
     * 3. Create and return UserDetails object
     * 
     * @param username the username to load
     * @return UserDetails object for Spring Security
     * @throws UsernameNotFoundException if user not found
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println("Loading user: " + username);
        
        // Step 1: Load user from our custom repository
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        // Step 2: Convert our Role enum to Spring Security authorities
        var authorities = user.getRoles().stream()
                .map(Role::name)
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());

        // Step 3: Return Spring Security UserDetails implementation
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
    
    /**
     * Note: If we wanted to extend this pattern, we could make this class
     * abstract and create subclasses for different user loading strategies:
     * - DatabaseUserDetailsService
     * - LdapUserDetailsService  
     * - OAuth2UserDetailsService
     * 
     * Each would implement loadUserByUsername() differently while the
     * overall authentication flow remains the same.
     */
}
