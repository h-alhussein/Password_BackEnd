package com.thkoeln.passwordskey.user.application;

import com.thkoeln.passwordskey.user.domain.User;
import com.thkoeln.passwordskey.user.domain.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public User getCurrentUser() {
        if (SecurityContextHolder.getContext().getAuthentication() != null)
            return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return null;
    }


}
