package com.example.Essenza.controller;

import com.example.Essenza.model.Rol;
import com.example.Essenza.model.Usuario;
import com.example.Essenza.payload.JwtAuthenticationResponse;
import com.example.Essenza.payload.LoginRequest;
import com.example.Essenza.payload.RegisterRequest;
import com.example.Essenza.repository.RolRepository;
import com.example.Essenza.repository.UsuarioRepository;
import com.example.Essenza.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Autenticación", description = "Endpoints para registro e inicio de sesión seguro.")
@RestController
@RequestMapping("/api/v1/auth")
@CrossOrigin(origins = "http://localhost:5173") // CORS para React Frontend
public class AuthController {

    @Autowired private AuthenticationManager authenticationManager;
    @Autowired private UsuarioRepository usuarioRepository;
    @Autowired private RolRepository rolRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private JwtTokenProvider tokenProvider;

    // ENDPOINT DE LOGIN (Punto 9)
    @Operation(summary = "Inicia sesión y devuelve un Token JWT")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthenticationResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Genera el token JWT (Punto 9b)
        String jwt = tokenProvider.generateToken(authentication);
        
        // Obtiene el rol del usuario autenticado para la respuesta (Punto 9a, 9d)
        String role = authentication.getAuthorities().iterator().next().getAuthority();

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, role));
    }

    // ENDPOINT DE REGISTRO (Punto 9)
    @Operation(summary = "Registra un nuevo usuario Cliente")
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest registerRequest) {
        
        // 1. Validar si el email ya está en uso
        if (usuarioRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            return new ResponseEntity<>("Email ya está en uso!", HttpStatus.BAD_REQUEST);
        }

        // 2. Buscar el rol por defecto (CLIENTE) (Punto 9a)
        Rol userRol = rolRepository.findByNombre("CLIENTE")
                .orElseThrow(() -> new RuntimeException("Error: Rol de cliente no encontrado."));

        // 3. Crear el objeto Usuario y cifrar la contraseña (Punto 9)
        Usuario usuario = new Usuario();
        usuario.setNombre(registerRequest.getNombre());
        usuario.setApellido(registerRequest.getApellido());
        usuario.setEmail(registerRequest.getEmail());
        usuario.setDireccion(registerRequest.getDireccion());
        usuario.setTelefono(registerRequest.getTelefono());
        
        // Cifrar la contraseña
        usuario.setPasswordHash(passwordEncoder.encode(registerRequest.getPassword()));

        // Asignar el rol por defecto
        usuario.setRol(userRol);

        usuarioRepository.save(usuario);

        return new ResponseEntity<>("Usuario registrado exitosamente!", HttpStatus.CREATED);
    }
}