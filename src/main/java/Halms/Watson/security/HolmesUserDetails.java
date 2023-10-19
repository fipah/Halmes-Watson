//package Halms.Watson.security;
//
//import Halms.Watson.model.entity.Users;
//import Halms.Watson.repository.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.List;
//
//@RequiredArgsConstructor
//@Service
//public class HolmesUserDetails implements UserDetailsService {
////    private final Users users;
//
//    UserRepository userRepository;
////
////    @Override
////    public Collection<? extends GrantedAuthority> getAuthorities() {
////        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(users.getRole().toString());
////        System.out.println(users.getRole());
////        return Collections.singleton(simpleGrantedAuthority);
////    }
////
////    @Override
////    public String getPassword() {
////        return users.getPassword();
////    }
////
////    @Override
////    public String getUsername() {
////        return users.getUsername();
////    }
////
////    @Override
////    public boolean isAccountNonExpired() {
////        return true;
////    }
////
////    @Override
////    public boolean isAccountNonLocked() {
////        return true;
////    }
////
////    @Override
////    public boolean isCredentialsNonExpired() {
////        return true;
////    }
////
////    @Override
////    public boolean isEnabled() {
////        return true;
////    }
//
//    @Override
//    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
//        var userFromDb = userRepository.findByUsername(s)
//                .orElseThrow(() -> new UsernameNotFoundException("No user found with this email address."));
//        List<GrantedAuthority> authorities = new java.util.ArrayList<>(Collections.emptyList());
//
//        authorities.add((GrantedAuthority) () -> userFromDb.getRole().name());
//
//        return new User(userFromDb.getUsername(), userFromDb.getPassword(), authorities);
//    }
//}
