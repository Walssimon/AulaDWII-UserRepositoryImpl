package br.com.senacsp.tads.stads4ma.library.presentation;


import br.com.senacsp.tads.stads4ma.library.infrascrture.config.JwtHelper;
import br.com.senacsp.tads.stads4ma.library.presentation.dtos.AuthRequest;
import br.com.senacsp.tads.stads4ma.library.presentation.dtos.AuthResponse;
import br.com.senacsp.tads.stads4ma.library.presentation.dtos.TokenRefreshRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtHelper jwtHelper;
    private final UserDetailsService userDetailsService;

    @PostMapping
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest authRequest){
        Autentication autentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.username(),authRequest.password())
        );

        UserDetails userDetails = authentication.getPrincipal();

        final String acessToken = this.jwtHelper.generateToken(userDetails);
        final String refreshToken = this.jwtHelper.generateRefreshToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(acessToken, refreshToken));
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody TokenRefreshRequest tokenRefreshRequest){
        final String token = tokenRefreshRequest.refreshToken();
        String username = this.jwtHelper.extractUsername(token);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        if (this.jwtHelper.isTokenValid(token , userDetails)){
            final String newAccessToken = this.jwtHelper.generateToken(userDetails);
            return new ResponseEntity.ok(new AuthResponse(newAccessToken, token));
        }else{
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body("Refresh Token Invalido ou expirado");
        }

    }



}
