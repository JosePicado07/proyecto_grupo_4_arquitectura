package services;

import models.Discount;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class DiscountService {
    
    private List<Discount> discounts;
    
    public DiscountService() {
        this.discounts = new ArrayList<>();
        initializeDefaultDiscounts();
    }
    
    private void initializeDefaultDiscounts() {
        // Descuento porcentual general
        discounts.add(new Discount(
            "Descuento de apertura", 
            true, 
            10.0, 
            LocalDate.now().minusDays(30), 
            LocalDate.now().plusDays(30),
            null));
        
        // Descuento de monto fijo con código promocional
        discounts.add(new Discount(
            "Cupón de bienvenida", 
            false, 
            5000.0, 
            LocalDate.now().minusDays(15), 
            LocalDate.now().plusDays(15),
            "BIENVENIDO"));
            
        // Descuento sin fecha de fin
        discounts.add(new Discount(
            "Descuento clientes frecuentes", 
            true, 
            5.0, 
            LocalDate.now().minusDays(60)));
    }
    
    public boolean addDiscount(Discount discount) {
        return discounts.add(discount);
    }
    
    public boolean removeDiscount(int id) {
        return discounts.removeIf(d -> d.getId() == id);
    }
    
    public List<Discount> getAllDiscounts() {
        return new ArrayList<>(discounts);
    }
    
    public List<Discount> getActiveDiscounts() {
        return discounts.stream()
                .filter(Discount::estaVigente)
                .collect(Collectors.toList());
    }
    
    public Optional<Discount> getDiscountById(int id) {
        return discounts.stream()
                .filter(d -> d.getId() == id)
                .findFirst();
    }
    
    public Optional<Discount> validatePromoCode(String code) {
        if (code == null || code.isEmpty()) {
            return Optional.empty();
        }
        
        return discounts.stream()
                .filter(d -> d.estaVigente() && d.validarCodigo(code))
                .findFirst();
    }
    
    public DiscountResult applyBestDiscount(double subtotal, String promoCode) {
        // Primero buscamos si hay un descuento por código promocional
        Optional<Discount> promoDiscount = validatePromoCode(promoCode);
        
        // Si hay un descuento por código, lo aplicamos
        if (promoDiscount.isPresent()) {
            Discount discount = promoDiscount.get();
            double discountAmount = discount.calcularDescuento(subtotal);
            return new DiscountResult(discount, discountAmount, subtotal - discountAmount);
        }
        
        // Si no hay código o no es válido, buscamos el mejor descuento automático
        Optional<Discount> bestDiscount = getActiveDiscounts().stream()
                .filter(d -> d.getCodigoPromo() == null) // Solo descuentos automáticos
                .max((d1, d2) -> {
                    double amount1 = d1.calcularDescuento(subtotal);
                    double amount2 = d2.calcularDescuento(subtotal);
                    return Double.compare(amount1, amount2);
                });
        
        // Si encontramos un descuento automático, lo aplicamos
        if (bestDiscount.isPresent()) {
            Discount discount = bestDiscount.get();
            double discountAmount = discount.calcularDescuento(subtotal);
            return new DiscountResult(discount, discountAmount, subtotal - discountAmount);
        }
        
        // Si no hay descuentos aplicables, retornamos sin descuento
        return new DiscountResult(null, 0, subtotal);
    }
    
    public static class DiscountResult {
        private final Discount discountApplied;
        private final double discountAmount;
        private final double finalAmount;
        
        public DiscountResult(Discount discountApplied, double discountAmount, double finalAmount) {
            this.discountApplied = discountApplied;
            this.discountAmount = discountAmount;
            this.finalAmount = finalAmount;
        }
        
        public Discount getDiscountApplied() {
            return discountApplied;
        }
        
        public double getDiscountAmount() {
            return discountAmount;
        }
        
        public double getFinalAmount() {
            return finalAmount;
        }
        
        public boolean hasDiscount() {
            return discountApplied != null && discountAmount > 0;
        }
    }
}