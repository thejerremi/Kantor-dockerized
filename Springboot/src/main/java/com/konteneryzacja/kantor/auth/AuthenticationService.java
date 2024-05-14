package com.konteneryzacja.kantor.auth;

import com.konteneryzacja.kantor.config.JwtService;
import com.konteneryzacja.kantor.token.Token;
import com.konteneryzacja.kantor.token.TokenRepository;
import com.konteneryzacja.kantor.token.TokenType;
import com.konteneryzacja.kantor.user.Role;
import com.konteneryzacja.kantor.user.User;
import com.konteneryzacja.kantor.user.UserDTO;
import com.konteneryzacja.kantor.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Optional;

@ControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(IllegalStateException.class)
  public ResponseEntity<String> handleIllegalStateException(IllegalStateException e) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
  }
}
@Service
@RequiredArgsConstructor
public class AuthenticationService {
  private final UserRepository repository;
  private final TokenRepository tokenRepository;
  private final PasswordEncoder passwordEncoder;
  private final JwtService jwtService;
  private final AuthenticationManager authenticationManager;

  public AuthenticationResponse register(RegisterRequest request) {
    if (repository.findByEmail(request.getEmail()).isPresent()) {
      throw new IllegalStateException("User already exists.");
    }
    var user = User.builder()
        .firstname(request.getFirstname())
        .lastname(request.getLastname())
        .email(request.getEmail())
        .password(passwordEncoder.encode(request.getPassword()))
        .balance(BigDecimal.valueOf(0))
        .role(Role.USER)
        .build();
    var savedUser = repository.save(user);
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    saveUserToken(savedUser, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
        .refreshToken(refreshToken)
            .user(convertToDTO(savedUser))
        .build();
  }

  public AuthenticationResponse authenticate(AuthenticationRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            request.getEmail(),
            request.getPassword()
        )
    );
    var user = repository.findByEmail(request.getEmail())
        .orElseThrow();
    var jwtToken = jwtService.generateToken(user);
    var refreshToken = jwtService.generateRefreshToken(user);
    revokeAllUserTokens(user);
    saveUserToken(user, jwtToken);
    return AuthenticationResponse.builder()
        .accessToken(jwtToken)
            .refreshToken(refreshToken)
            .user(convertToDTO(user))
        .build();
  }

  private void saveUserToken(User user, String jwtToken) {
    var token = Token.builder()
        .user(user)
        .token(jwtToken)
        .tokenType(TokenType.BEARER)
        .expired(false)
        .revoked(false)
        .build();
    tokenRepository.save(token);
  }

  private void revokeAllUserTokens(User user) {
    var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
    if (validUserTokens.isEmpty())
      return;
    validUserTokens.forEach(token -> {
      token.setExpired(true);
      token.setRevoked(true);
    });
    tokenRepository.saveAll(validUserTokens);
  }

  public void refreshToken(
          HttpServletRequest request,
          HttpServletResponse response
  ) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String refreshToken;
    final String userEmail;
    if (authHeader == null ||!authHeader.startsWith("Bearer ")) {
      return;
    }
    refreshToken = authHeader.substring(7);
    userEmail = jwtService.extractUsername(refreshToken);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(refreshToken, user)) {
        var accessToken = jwtService.generateToken(user);
        revokeAllUserTokens(user);
        saveUserToken(user, accessToken);
        var authResponse = AuthenticationResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
        new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
      }
    }
  }
  public Optional<User> findUserByToken(String token) {
    final String userEmail;
    User user;
    if (token == null || !token.startsWith("Bearer ")) {
      return Optional.empty();
    }
    token = token.substring(7);
    userEmail = jwtService.extractUsername(token);
    if (userEmail != null) {
      user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(token, user)) {
        return Optional.of(user);
      }
    }
    return Optional.empty();
  }
  public ResponseEntity<UserDTO> findUserDTOByToken(HttpServletRequest request) throws IOException {
    final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
    final String token;
    final String userEmail;
    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      throw new IllegalStateException("No active user with this token");
    }
    token = authHeader.substring(7);

    userEmail = jwtService.extractUsername(token);
    if (userEmail != null) {
      var user = this.repository.findByEmail(userEmail)
              .orElseThrow();
      if (jwtService.isTokenValid(token, user)) {
        return ResponseEntity.ok(convertToDTO(user));
      }else{
        throw new IllegalStateException("Token for this user is no longer valid.");
      }
    }
    return null;
  }
  public UserDTO convertToDTO(User user) {
    return new UserDTO(
            user.getFirstname(),
            user.getLastname(),
            user.getEmail(),
            user.getBalance(),
            user.getRole()
    );
  }
}
