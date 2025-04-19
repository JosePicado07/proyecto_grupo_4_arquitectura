package controllers;

import models.Bill;
import models.Product;
import models.User;
import services.BillService;
import services.CartService;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class BillController {
    
    private final BillService billService;
    private final CartService cartService;
    
    public BillController(BillService billService, CartService cartService) {
        this.billService = billService;
        this.cartService = cartService;
    }
    
    public Bill createBill(int sinpe, User currentUser, String promoCode) {
        if (sinpe <= 0 || currentUser == null) {
            return null;
        }
        
        List<Product> cartItems = cartService.getCartItems();
        
        if (cartItems.isEmpty()) {
            return null;
        }
        
        Bill bill = billService.createBill(cartItems, sinpe, currentUser, promoCode);
        
        // Limpiar el carrito después de crear la factura
        cartService.clearCart();
        
        return bill;
    }
    
    public List<Bill> getAllBills() {
        return billService.getAllBills();
    }
    
    public Optional<Bill> getBillById(int id) {
        return billService.getBillById(id);
    }
    
    public List<Bill> getBillsByUser(String cedula) {
        return billService.getBillsByUser(cedula);
    }
    
    public List<Bill> getBillsByDate(LocalDate date) {
        return billService.getBillsByDate(date);
    }
    
    public List<Bill> searchBills(String criteria) {
        return billService.searchBills(criteria);
    }
}