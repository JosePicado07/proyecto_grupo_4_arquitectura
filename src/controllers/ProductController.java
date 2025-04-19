package controllers;

import models.Product;
import services.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ProductController {
    
    private final ProductService productService;
    
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }
    
    public Map<String, List<Product>> getProductsByCategory() {
        return productService.getProductsByCategory();
    }
    
    public List<Product> getProductsInCategory(String category) {
        return productService.getProductsByCategory(category);
    }
    
    public List<String> getAllCategories() {
        return productService.getAllCategories();
    }
    
    public Optional<Product> getProductById(int id) {
        return productService.getProductById(id);
    }
    
    public String getProductCategory(int productId) {
        return productService.getProductCategory(productId);
    }
    
    public List<Product> searchProducts(String keyword) {
        return productService.searchProductsByName(keyword);
    }
    
    public List<Product> searchProductsByPrice(double minPrice, double maxPrice) {
        return productService.searchProductsByPriceRange(minPrice, maxPrice);
    }
    
    public boolean addProduct(String nombre, double precio, String categoria) {
        if (nombre == null || nombre.trim().isEmpty() || precio <= 0) {
            return false;
        }
        
        // Generar un nuevo ID (simple para este caso)
        int newId = productService.getAllProducts().size() + 1;
        Product newProduct = new Product(newId, nombre, precio);
        
        return productService.addProduct(newProduct, categoria);
    }
    
    public boolean updateProduct(int id, String nombre, double precio) {
        Optional<Product> existingProduct = productService.getProductById(id);
        
        if (existingProduct.isEmpty()) {
            return false;
        }
        
        if (nombre == null || nombre.trim().isEmpty() || precio <= 0) {
            return false;
        }
        
        Product product = existingProduct.get();
        product.setNombre(nombre);
        product.setPrecio(precio);
        
        return productService.updateProduct(product);
    }
    
    public boolean removeProduct(int id) {
        return productService.removeProduct(id);
    }
}