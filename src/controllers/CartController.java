package controllers;

import models.Cart;
import models.Product;
import models.Cart.CartItem;
import services.CartService;
import services.ProductService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class CartController {
    
    private final CartService cartService;
    private final ProductService productService;
    
    public CartController(CartService cartService, ProductService productService) {
        this.cartService = cartService;
        this.productService = productService;
    }
    
    public boolean addToCart(int productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        
        Optional<Product> product = productService.getProductById(productId);
        
        if (product.isEmpty()) {
            return false;
        }
        
        return cartService.addProduct(product.get(), quantity);
    }
    
    public boolean removeFromCart(int productId, int quantity) {
        if (quantity <= 0) {
            return false;
        }
        
        return cartService.removeProduct(productId, quantity);
    }
    
    public void clearCart() {
        cartService.clearCart();
    }
    
    public List<Product> getCartItems() {
        return cartService.getCartItems();
    }
    
    public Map<Integer, Integer> getProductQuantities() {
        return cartService.getProductQuantities();
    }
    
    public int getItemCount() {
        return cartService.getItemCount();
    }
    
    public double calculateSubtotal() {
        return cartService.calculateSubtotal();
    }
    
    public List<CartItem> getCartSummary() {
        return cartService.getCartItemsWithQuantity();
    }
    
    public Cart getCart() {
        return cartService.getCart();
    }
}