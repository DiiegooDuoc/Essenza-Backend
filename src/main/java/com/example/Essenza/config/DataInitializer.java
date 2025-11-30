package com.example.Essenza.config;

import com.example.Essenza.model.Rol;
import com.example.Essenza.model.Usuario;
import com.example.Essenza.repository.RolRepository;
import com.example.Essenza.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class DataInitializer {

    @Bean
    @Transactional // Asegura que las operaciones de BD se ejecuten como una sola transacción
    public CommandLineRunner initData(
            RolRepository rolRepository, 
            UsuarioRepository usuarioRepository, 
            PasswordEncoder passwordEncoder) {

        return args -> {
            // 1. CREACIÓN DE ROLES (Punto 9a)
            // Solo creamos los roles si no existen
            Rol clienteRol = rolRepository.findByNombre("CLIENTE").orElseGet(() -> {
                Rol rol = new Rol(null, "CLIENTE", null);
                return rolRepository.save(rol);
            });

            Rol adminRol = rolRepository.findByNombre("ADMIN").orElseGet(() -> {
                Rol rol = new Rol(null, "ADMIN", null);
                return rolRepository.save(rol);
            });

            // Solo creamos el admin si no existe
            if (usuarioRepository.findByEmail("admin@essenza.cl").isEmpty()) {
                Usuario admin = new Usuario();
                admin.setNombre("Admin");
                admin.setApellido("Essenza");
                admin.setEmail("admin@essenza.cl");
                admin.setTelefono(12345678); // Teléfono de ejemplo
                admin.setDireccion("Calle Falsa 123");
                
                // IMPORTANTE: Codificar la contraseña (Punto 9)
                admin.setPasswordHash(passwordEncoder.encode("password123")); // Contraseña: password123
                admin.setRol(adminRol);

                usuarioRepository.save(admin);
                System.out.println("--- ADMIN CREADO: admin@essenza.cl / password123 ---");
            }
        };
    }
}