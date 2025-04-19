package services;

import models.Cart;
import models.Product;
import models.Cart.CartItem;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CartService {
    
    private final Cart cart;
    
    public CartService() {
        this.cart = new Cart();
    }
    
    public boolean addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }
        
        return cart.addItem(product, quantity);
    }
    
    public boolean removeProduct(int productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        
        return cart.removeItem(productId, quantity);
    }
    
    public void clearCart() {
        cart.clear();
    }
    
    public List<Product> getCartItems() {
        return cart.getItems();
    }

    public Map<Integer, Integer> getProductQuantities() {
        return cart.getQuantities();
    }
    
    public int getItemCount() {
        return cart.getItemCount();
    }
    
    public double calculateSubtotal() {
        return cart.calculateSubtotal();
    }
    
    public List<CartItem> getCartItemsWithQuantity() {
        return cart.getItemsWithQuantity();
    }
    
    public Cart getCart() {
        return cart;
    }
    
    public boolean isProductInCart(int productId) {
        return cart.getQuantities().containsKey(productId);
    }
    
    public int getProductQuantity(int productId) {
        return cart.getQuantities().getOrDefault(productId, 0);
    }
    
    public boolean updateProductQuantity(int productId, int newQuantity) {
        return cart.updateItem(productId, newQuantity);
    }

}