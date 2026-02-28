package NewsPortal.com.example.NewsPortalApp.service;

import NewsPortal.com.example.NewsPortalApp.dto.*;
import NewsPortal.com.example.NewsPortalApp.entity.User;
import NewsPortal.com.example.NewsPortalApp.repository.UserRepository;
import NewsPortal.com.example.NewsPortalApp.security.JwtUtil;
import NewsPortal.com.example.NewsPortalApp.exception.CustomApiException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // SIGNUP
    public SignupResponseDto signup(SignupRequestDto request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new CustomApiException(
                    HttpStatus.CONFLICT,
                    "Email already exists"
            );
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole("CUSTOMER");

        User savedUser = userRepository.save(user);

        return new SignupResponseDto(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole()
        );
    }

    // LOGIN
    public LoginResponseDto login(LoginRequestDto request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.UNAUTHORIZED,
                        "Invalid email or password"
                ));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new CustomApiException(
                    HttpStatus.UNAUTHORIZED,
                    "Invalid email or password"
            );
        }

        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        return new LoginResponseDto(
                user.getId(),
                user.getRole(),
                token
        );
    }

    // PROFILE
    public ProfileResponseDto getProfile(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        return new ProfileResponseDto(
                user.getUsername(),
                user.getEmail(),
                user.getRole()
        );
    }

    // HOME
    public HomeResponseDto getHomeContent() {

        HomeResponseDto dto = new HomeResponseDto();
        dto.setTitle("Welcome to News Portal");
        dto.setContent("This is the home page of the news portal. Stay updated with the latest news.");

        return dto;
    }

    // BECOME REPORTER
    public BecomeReporterResponseDto becomeReporter(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomApiException(
                        HttpStatus.NOT_FOUND,
                        "User not found"
                ));

        // Already reporter check
        if ("REPORTER".equals(user.getRole())) {
            throw new CustomApiException(
                    HttpStatus.BAD_REQUEST,
                    "User is already a reporter"
            );
        }

        // Update role
        user.setRole("REPORTER");
        userRepository.save(user);

        // Generate new token with updated role
        String token = jwtUtil.generateToken(user.getId(), user.getRole());

        return new BecomeReporterResponseDto(
                user.getId(),
                user.getRole(),
                token
        );
    }
}


