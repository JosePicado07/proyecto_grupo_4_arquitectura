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
        
        if (currentQuantity < quantity) {
            return false;
        }
        
        int removed = 0;
        List<Product> toRemove = new ArrayList<>();
        
        for (Product item : items) {
            if (item.getId() == productId && removed < quantity) {
                toRemove.add(item);
                removed++;
            }
        }
        
        items.removeAll(toRemove);
        
        if (currentQuantity == quantity) {
            quantities.remove(productId);
        } else {
            quantities.put(productId, currentQuantity - quantity);
        }
        
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
                .mapToDouble(Product::getPrecio)
                .sum();
    }
    
    public List<CartItem> getItemsWithQuantity() {
        Map<Integer, Product> productMap = new HashMap<>();
        List<CartItem> cartItems = new ArrayList<>();
        
        for (Product product : items) {
            productMap.put(product.getId(), product);
        }
        
        for (Map.Entry<Integer, Integer> entry : quantities.entrySet()) {
            Product product = productMap.get(entry.getKey());
            int quantity = entry.getValue();
            double subtotal = product.getPrecio() * quantity;
            
            cartItems.add(new CartItem(product, quantity, subtotal));
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