package com.pedroprojects.service;

import com.pedroprojects.model.User;
import com.pedroprojects.model.Role;
import com.pedroprojects.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleService roleService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        // Gets the role 'ROLE_USER'
        Role userRole = roleService.getRoleByName("ROLE_USER");
        user.setRoles(List.of(userRole)); 

        return userRepository.save(user);
    }

    public User updateUser(Long id, User update, Authentication authentication) {
        User currentUser = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            // The admin can update data, but not password
            if (update.getEmail() != null) {
                currentUser.setEmail(update.getEmail());
            }
            if (update.getFirstname() != null) {
                currentUser.setFirstname(update.getFirstname());
            }
            if (update.getLastname() != null) {
                currentUser.setLastname(update.getLastname());
            }
            if (update.getBirthday() != null) {
                currentUser.setBirthday(update.getBirthday());
            }
            if (update.getGender() != null) {
                currentUser.setGender(update.getGender());
            }
            if (update.getPhoneNumber() != null) {
                currentUser.setPhoneNumber(update.getPhoneNumber());
            }
            if (update.getAddress() != null) {
                currentUser.setAddress(update.getAddress());
            }
            currentUser.setUpdatedAt(LocalDateTime.now());
        } else {
            // Nod-admin users can update password and other details
            if (update.getPassword() != null) {
                currentUser.setPassword(passwordEncoder.encode(update.getPassword()));
            }
            if (update.getEmail() != null) {
                currentUser.setEmail(update.getEmail());
            }
            if (update.getFirstname() != null) {
                currentUser.setFirstname(update.getFirstname());
            }
            if (update.getLastname() != null) {
                currentUser.setLastname(update.getLastname());
            }
            if (update.getBirthday() != null) {
                currentUser.setBirthday(update.getBirthday());
            }
            if (update.getGender() != null) {
                currentUser.setGender(update.getGender());
            }
            if (update.getPhoneNumber() != null) {
                currentUser.setPhoneNumber(update.getPhoneNumber());
            }
            if (update.getAddress() != null) {
                currentUser.setAddress(update.getAddress());
            }
            currentUser.setUpdatedAt(LocalDateTime.now());
        }

        return userRepository.save(currentUser);
    }

    public boolean deleteUserById(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
