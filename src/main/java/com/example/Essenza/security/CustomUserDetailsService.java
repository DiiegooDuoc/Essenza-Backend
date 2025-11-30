package com.example.Essenza.security;

import com.example.Essenza.model.Usuario;
import com.example.Essenza.repository.UsuarioRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

// Implementación de UserDetailsService para cargar el usuario desde la BD
@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Carga el usuario por su email (Username en Spring Security)
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Busca el usuario en la BD (Punto 7a)
        Usuario usuario = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con email: " + email));

        // Crea las autoridades/roles a partir del Rol del usuario (Punto 9a)
        Set<GrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority(usuario.getRol().getNombre())
        );

        // Devuelve el objeto UserDetails que Spring Security usa internamente
        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(),
                usuario.getPasswordHash(), // Usamos el hash guardado
                authorities
        );
    }
}