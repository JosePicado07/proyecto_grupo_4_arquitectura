package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cart {
    
    private final List<Product> items;
    private final Map<Integer, Integer> quantities;
    
    public Cart() {
        this.items = new ArrayList<>();
        this.quantities = new HashMap<>();
    }
    
    public boolean addItem(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }
        
        for (int i = 0; i < quantity; i++) {
            items.add(product);
        }
        
        int currentQuantity = quantities.getOrDefault(product.getId(), 0);
        quantities.put(product.getId(), currentQuantity + quantity);
        
        return true;
    }
    
    public boolean removeItem(int productId, int quantity) {
        int currentQuantity = quantities.getOrDefault(productId, 0);
        if (currentQuantity < quantity || quantity <= 0) {
            return false;
        }
        
        int removed = 0;
        List<Product> toRemove = new ArrayList<>();
        for (Product item : items) {
            if (item != null && item.getId() == productId && removed < quantity) {
                toRemove.add(item);
                removed++;
            }
        }
        
        items.removeAll(toRemove);
        if (removed > 0) {
            quantities.put(productId, currentQuantity - removed);
            if (quantities.get(productId) == 0) {
                quantities.remove(productId);
            }
            return true;
        }
        
        // Si no se eliminó nada, limpiar la entrada si existe
        quantities.remove(productId);
        return false;
    }
    
    public boolean updateItem(int productId, int newQuantity) {
        if (newQuantity < 0) {
            return false;
        }
        
        int currentQuantity = quantities.getOrDefault(productId, 0);
        if (currentQuantity == 0) {
            return false; // Producto no está en el carrito
        }
        
        if (newQuantity == 0) {
            quantities.remove(productId);
            items.removeIf(item -> item != null && item.getId() == productId);
            return true;
        }
        
        Product product = items.stream()
                .filter(p -> p != null && p.getId() == productId)
                .findFirst()
                .orElse(null);
        
        if (product == null) {
            quantities.remove(productId);
            return false;
        }
        
        int quantityDifference = newQuantity - currentQuantity;
        if (quantityDifference > 0) {
            for (int i = 0; i < quantityDifference; i++) {
                items.add(product);
            }
        } else if (quantityDifference < 0) {
            int toRemove = -quantityDifference;
            List<Product> toRemoveList = new ArrayList<>();
            int removed = 0;
            for (Product item : items) {
                if (item != null && item.getId() == productId && removed < toRemove) {
                    toRemoveList.add(item);
                    removed++;
                }
            }
            items.removeAll(toRemoveList);
            if (removed < toRemove) {
                quantities.remove(productId);
                return false;
            }
        }
        
        quantities.put(productId, newQuantity);
        return true;
    }
    
    public void clear() {
        items.clear();
        quantities.clear();
    }
    
    public List<Product> getItems() {
        return new ArrayList<>(items);
    }
    
    public Map<Integer, Integer> getQuantities() {
        return new HashMap<>(quantities);
    }
    
    public int getItemCount() {
        return items.size();
    }
    
    public double calculateSubtotal() {
        return items.stream()
                .filter(product -> product != null)
                .mapToDouble(Product::getPrecio)
                .sum();
    }
    
    public List<CartItem> getItemsWithQuantity() {
        Map<Integer, Product> productMap = new HashMap<>();
        List<CartItem> cartItems = new ArrayList<>();
        
        for (Product product : items) {
            if (product != null) {
                productMap.put(product.getId(), product);
            }
        }
        
        List<Integer> invalidIds = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
            Product product = productMap.get(entry.getKey());
            if (product != null) {
                int quantity = entry.getValue();
                double subtotal = product.getPrecio() * quantity;
                cartItems.add(new CartItem(product, quantity, subtotal));
            } else {
                invalidIds.add(entry.getKey());
            }
        }
        
        // Limpiar IDs inválidos de quantities
        for (Integer id : invalidIds) {
            quantities.remove(id);
        }
        
        return cartItems;
    }
    
    public static class CartItem {
        private final Product product;
        private final int quantity;
        private final double subtotal;
        
        public CartItem(Product product, int quantity, double subtotal) {
            this.product = product;
            this.quantity = quantity;
            this.subtotal = subtotal;
        }
        
        public Product getProduct() {
            return product;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public double getSubtotal() {
            return subtotal;
        }
    }
}