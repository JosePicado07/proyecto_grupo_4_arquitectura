package services;

import models.Cart;
import models.Product;
import models.Cart.CartItem;

import java.util.List;
import java.util.Map;

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
        if (newQuantity <= 0) {
            // Si la cantidad es 0 o negativa, eliminar el producto
            return removeProduct(productId, getProductQuantity(productId));
        }
        
        int currentQuantity = getProductQuantity(productId);
        
        if (currentQuantity == 0) {
            // El producto no está en el carrito, no se puede actualizar
            return false;
        }
        
        if (currentQuantity < newQuantity) {
            // Necesitamos añadir más unidades
            return cart.addItem(null, newQuantity - currentQuantity); // Esto necesitaría el objeto Product
        } else if (currentQuantity > newQuantity) {
            // Necesitamos quitar unidades
            return cart.removeItem(productId, currentQuantity - newQuantity);
        }
        
        // La cantidad es la misma, no hacemos nada
        return true;
    }
}