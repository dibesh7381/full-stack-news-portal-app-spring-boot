package NewsPortal.com.example.NewsPortalApp.controller;

import NewsPortal.com.example.NewsPortalApp.dto.*;
import NewsPortal.com.example.NewsPortalApp.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final AuthService authService;

    // HOME
    @GetMapping("/home")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponseDto<HomeResponseDto>> home() {

        HomeResponseDto response = authService.getHomeContent();

        ApiResponseDto<HomeResponseDto> apiResponse =
                new ApiResponseDto<>(true, "Home data fetched", response);

        return ResponseEntity.ok(apiResponse);
    }

    // SIGNUP
    @PostMapping("/signup")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponseDto<SignupResponseDto>> signup(
            @RequestBody SignupRequestDto request) {

        SignupResponseDto response = authService.signup(request);

        ApiResponseDto<SignupResponseDto> apiResponse =
                new ApiResponseDto<>(true, "Signup successful", response);

        return ResponseEntity.ok(apiResponse);
    }

    // LOGIN
    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponseDto<LoginResponseDto>> login(
            @RequestBody LoginRequestDto request) {

        LoginResponseDto response = authService.login(request);

        ApiResponseDto<LoginResponseDto> apiResponse =
                new ApiResponseDto<>(true, "Login successful", response);

        return ResponseEntity.ok(apiResponse);
    }

    // PROFILE
    @GetMapping("/profile")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ApiResponseDto<ProfileResponseDto>> getProfile(
            Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        ProfileResponseDto response = authService.getProfile(userId);

        ApiResponseDto<ProfileResponseDto> apiResponse =
                new ApiResponseDto<>(true, "Profile fetched successfully", response);

        return ResponseEntity.ok(apiResponse);
    }

    // BECOME REPORTER
    @PutMapping("/become-reporter")
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public ResponseEntity<ApiResponseDto<BecomeReporterResponseDto>> becomeReporter(
            Authentication authentication) {

        Long userId = (Long) authentication.getPrincipal();

        BecomeReporterResponseDto response =
                authService.becomeReporter(userId);

        ApiResponseDto<BecomeReporterResponseDto> apiResponse =
                new ApiResponseDto<>(true, "You are now a reporter", response);

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/about")
    @PreAuthorize("permitAll()")
    public ResponseEntity<ApiResponseDto<AboutPageDto>> AboutPage(){
        AboutPageDto response = authService.aboutPage();
        ApiResponseDto<AboutPageDto> aboutPages =
                new ApiResponseDto<>(true, "This is about page routes", response);

        return ResponseEntity.ok(aboutPages);
    }

}


