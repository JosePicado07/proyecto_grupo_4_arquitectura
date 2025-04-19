package services;

import models.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProductService {
    
    private final List<Product> products;
    private final Map<Integer, String> productCategories;
    
    public ProductService() {
        this.products = new ArrayList<>();
        this.productCategories = new HashMap<>();
        
        initializeProducts();
    }
    
    private void initializeProducts() {
        // Componentes
        addProduct(new Product(1, "Procesador AMD Ryzen 5 5600X", 135000.0), "Componentes");
        addProduct(new Product(2, "Memoria RAM DDR4 16GB 3200MHz", 45000.0), "Componentes");
        addProduct(new Product(3, "Disco SSD NVMe 1TB", 72000.0), "Componentes");
        addProduct(new Product(4, "Tarjeta Madre ASUS B550M", 88000.0), "Componentes");
        addProduct(new Product(5, "Fuente de Poder 650W 80+ Bronze", 43000.0), "Componentes");
        addProduct(new Product(6, "Tarjeta Gráfica NVIDIA RTX 3060", 230000.0), "Componentes");
        addProduct(new Product(7, "Enfriamiento Líquido 240mm", 69000.0), "Componentes");
        
        // Periféricos
        addProduct(new Product(8, "Gabinete ATX con RGB", 39000.0), "Periféricos");
        addProduct(new Product(9, "Monitor 24'' Full HD 144Hz", 98000.0), "Periféricos");
        addProduct(new Product(10, "Teclado Mecánico RGB", 27000.0), "Periféricos");
        
        // Accesorios
        addProduct(new Product(11, "Mouse Gaming 12000 DPI", 18500.0), "Accesorios");
        addProduct(new Product(12, "Auriculares Gaming 7.1", 32000.0), "Accesorios");
        addProduct(new Product(13, "Mousepad XL RGB", 9500.0), "Accesorios");
        
        // Software
        addProduct(new Product(14, "Windows 11 Pro", 55000.0), "Software");
        addProduct(new Product(15, "Office 365 (1 año)", 28000.0), "Software");
    }
    
    public boolean addProduct(Product product, String category) {
        if (products.add(product)) {
            productCategories.put(product.getId(), category);
            return true;
        }
        return false;
    }
    
    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }
    
    public Map<String, List<Product>> getProductsByCategory() {
        Map<String, List<Product>> result = new HashMap<>();
        
        for (Product product : products) {
            String category = productCategories.getOrDefault(product.getId(), "Sin categoría");
            
            if (!result.containsKey(category)) {
                result.put(category, new ArrayList<>());
            }
            
            result.get(category).add(product);
        }
        
        return result;
    }
    
    public List<Product> getProductsByCategory(String category) {
        return products.stream()
                .filter(p -> category.equals(productCategories.get(p.getId())))
                .collect(Collectors.toList());
    }
    
    public Optional<Product> getProductById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }
    
    public List<Product> searchProductsByName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return new ArrayList<>();
        }
        
        String lowerKeyword = keyword.toLowerCase();
        
        return products.stream()
                .filter(p -> p.getNombre().toLowerCase().contains(lowerKeyword))
                .collect(Collectors.toList());
    }
    
    public List<Product> searchProductsByPriceRange(double minPrice, double maxPrice) {
        return products.stream()
                .filter(p -> p.getPrecio() >= minPrice && p.getPrecio() <= maxPrice)
                .collect(Collectors.toList());
    }
    
    public String getProductCategory(int productId) {
        return productCategories.getOrDefault(productId, "Sin categoría");
    }
    
    public List<String> getAllCategories() {
        return new ArrayList<>(productCategories.values().stream()
                .distinct()
                .collect(Collectors.toList()));
    }
    
    public boolean updateProduct(Product product) {
        Optional<Product> existingProduct = getProductById(product.getId());
        
        if (existingProduct.isPresent()) {
            int index = products.indexOf(existingProduct.get());
            products.set(index, product);
            return true;
        }
        
        return false;
    }
    
    public boolean removeProduct(int id) {
        boolean removed = products.removeIf(p -> p.getId() == id);
        
        if (removed) {
            productCategories.remove(id);
        }
        
        return removed;
    }
}