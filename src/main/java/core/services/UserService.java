package core.services;

import api.dto.UserCreateDTO;
import api.dto.UserDTO;
import data.entities.Patient;
import data.entities.User;
import data.repositories.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@ApplicationScoped
public class UserService {

    @Inject
    UserRepository userRepository;

    public List<UserDTO> getAllUsers(int pageIndex, int pageSize) {
        List<User> users = userRepository.listAllUsers(pageIndex, pageSize);
        return users.stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getUserById(UUID userId) {
        User user = userRepository.findById(userId);
        if (user == null) return null;
        return toDTO(user);
    }

    public UserDTO getUserByEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        User user = userRepository.findByEmail(email);
        if (user == null) return null;
        return toDTO(user);
    }

    public long countUsers() {
        return userRepository.countTotalUsers();
    }


    @Transactional
    public UserDTO createUser(UserCreateDTO dto) {
        validateCreateDTO(dto);

        User user = new Patient(); //Doctors och OtherStaff kan bara få sina konton tilldelade från HR
        user.setFullName(dto.fullName);
        user.setEmail(dto.email);
        user.setPassword(hashPassword(dto.password));
        user.setUserType(dto.userType);

        userRepository.persist(user);
        return toDTO(user);
    }

    @Transactional
    public UserDTO updateUser(UUID userId, UserCreateDTO dto) {
        User user = userRepository.findById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (dto.fullName != null) user.setFullName(dto.fullName);
        //if (dto.email != null) user.setEmail(dto.email); //TODO: Keycloak authentication
        if (dto.password != null) user.setPassword(hashPassword(dto.password));

        userRepository.persist(user);
        return toDTO(user);
    }

    @Transactional
    public boolean deleteUser(UUID userId) {
        return userRepository.deleteById(userId);
    }

    public UserDTO login(String email, String password) {
        if (email == null || password == null) {
            throw new IllegalArgumentException("Email and password cannot be null");
        }

        User user = userRepository.findByEmail(email);
        if (user == null || !user.getPassword().equals(hashPassword(password))) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        return new UserDTO(user.getId(), user.getFullName(), user.getEmail(), user.getUserType());
    }

    private void validateCreateDTO(UserCreateDTO dto) {
        if (dto.fullName == null || dto.fullName.isEmpty()) {
            throw new IllegalArgumentException("Full name is required");
        }
        if (dto.email == null || dto.email.isEmpty()) {
            throw new IllegalArgumentException("Email is required");
        }
        if (dto.password == null || dto.password.isEmpty()) {
            throw new IllegalArgumentException("Password is required");
        }
        if (dto.userType == null) {
            throw new IllegalArgumentException("User type is required");
        }
    }

    private String hashPassword(String password) {
        return password; // TODO: implement real hashing / integrate Keycloak
    }

    private UserDTO toDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getUserType()
        );
    }
}
