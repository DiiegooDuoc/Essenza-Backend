package com.example.Essenza.service;

import com.example.Essenza.model.Producto;
import com.example.Essenza.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

// Clase que contiene la lógica de negocio para la gestión de Productos.
@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    // Inyección de dependencia del Repositorio (para interactuar con la BD)
    @Autowired
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    /**
     * Guarda un nuevo producto.
     * @param producto El objeto Producto a guardar.
     * @return El Producto guardado.
     */
    public Producto guardarProducto(Producto producto) {
        // Aquí se implementaría lógica de negocio como validación de stock inicial,
        // o asignación de categorías por defecto.
        return productoRepository.save(producto);
    }

    /**
     * Obtiene todos los productos.
     * @return Lista de todos los productos.
     */
    public List<Producto> obtenerTodosLosProductos() {
        return productoRepository.findAll();
    }

    /**
     * Obtiene un producto por su ID.
     * @param id El ID del producto.
     * @return Optional que contiene el producto si existe.
     */
    public Optional<Producto> obtenerProductoPorId(Long id) {
        return productoRepository.findById(id);
    }

    /**
     * Actualiza un producto existente.
     * @param id El ID del producto a actualizar.
     * @param productoActualizado Objeto con los nuevos datos.
     * @return El Producto actualizado.
     * @throws RuntimeException Si el producto no se encuentra.
     */
    public Producto actualizarProducto(Long id, Producto productoActualizado) {
        // Buscamos el producto existente por ID
        return productoRepository.findById(id).map(productoExistente -> {
            
            // Aplicamos los nuevos datos al objeto existente
            productoExistente.setNombre(productoActualizado.getNombre());
            productoExistente.setDescripcion(productoActualizado.getDescripcion());
            productoExistente.setPrecio(productoActualizado.getPrecio());
            productoExistente.setStock(productoActualizado.getStock());
            productoExistente.setImagenUrl(productoActualizado.getImagenUrl());
            productoExistente.setCategoria(productoActualizado.getCategoria());
            
            // Guardamos y devolvemos el producto actualizado
            return productoRepository.save(productoExistente);
        }).orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id)); 
    }

    /**
     * Elimina un producto por su ID.
     * @param id El ID del producto a eliminar.
     * @throws RuntimeException Si el producto no se encuentra.
     */
    public void eliminarProducto(Long id) {
        if (productoRepository.existsById(id)) {
            productoRepository.deleteById(id);
        } else {
            throw new RuntimeException("No se puede eliminar. Producto no encontrado con ID: " + id);
        }
    }
}