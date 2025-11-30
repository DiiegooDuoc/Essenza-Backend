package com.example.Essenza.controller;

import com.example.Essenza.model.Producto;
import com.example.Essenza.service.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Esto habilita la documentación en Swagger (Punto 8)
@Tag(name = "Productos", description = "Endpoints para la gestión del catálogo de perfumes.")
@RestController
@RequestMapping("/api/v1/productos") // Ruta base: /api/v1/productos
@CrossOrigin(origins = "http://localhost:5173") // CORS para React Frontend
public class ProductoController {

    private final ProductoService productoService;

    @Autowired
    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    // GET ALL (READ) - Público (Punto 8)
    // Ruta: GET /api/v1/productos
    @Operation(summary = "Obtiene todos los productos del catálogo")
    @GetMapping
    public ResponseEntity<List<Producto>> obtenerTodos() {
        List<Producto> productos = productoService.obtenerTodosLosProductos();
        return ResponseEntity.ok(productos);
    }

    // GET BY ID (READ) - Público (Punto 8)
    // Ruta: GET /api/v1/productos/{id}
    @Operation(summary = "Obtiene un producto por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<Producto> obtenerPorId(@PathVariable Long id) {
        return productoService.obtenerProductoPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST (CREATE) - Protegido por Rol ADMIN (Punto 9a)
    // Spring Security restringe este método gracias a la regla en SecurityConfig
    // Ruta: POST /api/v1/productos
    @Operation(summary = "Crea un nuevo producto (Requiere rol ADMIN)")
    @PostMapping
    public ResponseEntity<Producto> crearProducto(@RequestBody Producto producto) {
        Producto nuevoProducto = productoService.guardarProducto(producto);
        return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
    }

    // PUT (UPDATE) - Protegido por Rol ADMIN (Punto 9a)
    // Ruta: PUT /api/v1/productos/{id}
    @Operation(summary = "Actualiza un producto existente por ID (Requiere rol ADMIN)")
    @PutMapping("/{id}")
    public ResponseEntity<Producto> actualizarProducto(@PathVariable Long id, @RequestBody Producto producto) {
        try {
            Producto productoActualizado = productoService.actualizarProducto(id, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // DELETE (DELETE) - Protegido por Rol ADMIN (Punto 9a)
    // Ruta: DELETE /api/v1/productos/{id}
    @Operation(summary = "Elimina un producto por ID (Requiere rol ADMIN)")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProducto(@PathVariable Long id) {
        try {
            productoService.eliminarProducto(id);
            return ResponseEntity.noContent().build(); 
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}