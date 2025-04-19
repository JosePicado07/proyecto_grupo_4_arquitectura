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
    private final Map<String, List<Product>> productsByCategory;

    public ProductService() {
        this.products = new ArrayList<>();
        this.productsByCategory = new HashMap<>();
    }

    public boolean addProduct(Product product, String category) {
        if (product == null || category == null || category.trim().isEmpty()) {
            return false;
        }

        // Verificar si ya existe un producto con el mismo ID
        boolean exists = products.stream()
                .anyMatch(p -> p.getId() == product.getId());

        if (exists) {
            return false;
        }

        // Añadir el producto a la lista general
        products.add(product);

        // Añadir a la categoría correspondiente
        List<Product> categoryProducts = productsByCategory.getOrDefault(category, new ArrayList<>());
        categoryProducts.add(product);
        productsByCategory.put(category, categoryProducts);

        return true;
    }

    public boolean updateProduct(Product product) {
        if (product == null) {
            return false;
        }

        // Buscar y actualizar el producto
        for (int i = 0; i < products.size(); i++) {
            if (products.get(i).getId() == product.getId()) {
                products.set(i, product);

                // Actualizar en todas las categorías
                for (String category : productsByCategory.keySet()) {
                    List<Product> categoryProducts = productsByCategory.get(category);

                    for (int j = 0; j < categoryProducts.size(); j++) {
                        if (categoryProducts.get(j).getId() == product.getId()) {
                            categoryProducts.set(j, product);
                            break;
                        }
                    }
                }

                return true;
            }
        }

        return false;
    }

    public boolean removeProduct(int productId) {
        Optional<Product> productOpt = getProductById(productId);

        if (productOpt.isEmpty()) {
            return false;
        }

        Product product = productOpt.get();

        // Eliminar de la lista general
        products.remove(product);

        // Eliminar de las categorías
        for (String category : productsByCategory.keySet()) {
            productsByCategory.get(category).removeIf(p -> p.getId() == productId);
        }

        return true;
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(products);
    }

    public Map<String, List<Product>> getProductsByCategory() {
        return new HashMap<>(productsByCategory);
    }

    public List<Product> getProductsByCategory(String category) {
        return productsByCategory.getOrDefault(category, new ArrayList<>());
    }

    public Optional<Product> getProductById(int id) {
        return products.stream()
                .filter(p -> p.getId() == id)
                .findFirst();
    }

    public List<String> getAllCategories() {
        return new ArrayList<>(productsByCategory.keySet());
    }

    public String getProductCategory(int productId) {
        for (String category : productsByCategory.keySet()) {
            boolean inCategory = productsByCategory.get(category).stream()
                    .anyMatch(p -> p.getId() == productId);

            if (inCategory) {
                return category;
            }
        }

        return null;
    }

    public List<Product> searchProductsByName(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
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

    public double getProductPrice(int productId) {
        Optional<Product> productOpt = getProductById(productId);
        return productOpt.map(Product::getPrecio).orElse(0.0);
    }

    public boolean isProductAvailable(int productId) {
        return getProductById(productId).isPresent();
    }
}