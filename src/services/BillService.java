package services;

import models.Bill;
import models.Product;
import models.User;
import services.DiscountService.DiscountResult;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class BillService {
    
    private final List<Bill> bills;
    private final DiscountService discountService;
    
    public BillService(DiscountService discountService) {
        this.bills = new ArrayList<>();
        this.discountService = discountService;
    }
    
    public Bill createBill(List<Product> products, int sinpe, User currentUser, String promoCode) {
        double subtotal = calculateSubtotal(products);
        
        DiscountResult discountResult = discountService.applyBestDiscount(subtotal, promoCode);
        
        // Crear la factura con los datos calculados
        Bill bill = new Bill(products, sinpe, currentUser);
        
        // Si hay un descuento aplicado, actualizar los valores
        if (discountResult.hasDiscount()) {
            bill.setSubtotal(subtotal);
            double discountedSubtotal = discountResult.getFinalAmount();
            double taxes = discountedSubtotal * 0.13;
            bill.setImpuestos(taxes);
            bill.setTotal(discountedSubtotal + taxes);
        }
        
        bills.add(bill);
        return bill;
    }
    
    private double calculateSubtotal(List<Product> products) {
        return products.stream()
                .mapToDouble(Product::getPrecio)
                .sum();
    }
    
    public List<Bill> getAllBills() {
        return new ArrayList<>(bills);
    }
    
    public Optional<Bill> getBillById(int id) {
        return bills.stream()
                .filter(b -> b.getId() == id)
                .findFirst();
    }
    
    public List<Bill> getBillsByUser(String cedula) {
        return bills.stream()
                .filter(b -> b.getCedulaCliente().equals(cedula))
                .collect(Collectors.toList());
    }
    
    public List<Bill> getBillsByDate(LocalDate date) {
        return bills.stream()
                .filter(b -> b.getFecha().toLocalDate().equals(date))
                .collect(Collectors.toList());
    }
    
    public List<Bill> searchBills(String criteria) {
        String lowerCriteria = criteria.toLowerCase();
        
        return bills.stream()
                .filter(b -> 
                    b.getCedulaCliente().toLowerCase().contains(lowerCriteria) ||
                    String.valueOf(b.getId()).equals(lowerCriteria) ||
                    String.valueOf(b.getTotal()).contains(lowerCriteria)
                )
                .collect(Collectors.toList());
    }
    
    public Map<LocalDate, List<Bill>> getDailyBills() {
        return bills.stream()
                .collect(Collectors.groupingBy(bill -> bill.getFecha().toLocalDate()));
    }
    
    public double getTotalSales() {
        return bills.stream()
                .mapToDouble(Bill::getTotal)
                .sum();
    }
    
    public double getDailySales(LocalDate date) {
        return bills.stream()
                .filter(b -> b.getFecha().toLocalDate().equals(date))
                .mapToDouble(Bill::getTotal)
                .sum();
    }
    
    public Map<String, Object> generateDailyReport(LocalDate date) {
        List<Bill> dailyBills = getBillsByDate(date);
        
        Map<String, Object> report = new HashMap<>();
        report.put("date", date);
        report.put("totalTransactions", dailyBills.size());
        report.put("totalAmount", dailyBills.stream().mapToDouble(Bill::getTotal).sum());
        
        // Productos más vendidos
        Map<String, Long> productCounts = dailyBills.stream()
                .flatMap(b -> b.getProductos().stream())
                .collect(Collectors.groupingBy(Product::getNombre, Collectors.counting()));
        
        report.put("topProducts", productCounts);
        
        return report;
    }
    
    public Map<LocalDateTime, Double> getSalesTrend(LocalDate startDate, LocalDate endDate) {
        return bills.stream()
                .filter(b -> !b.getFecha().toLocalDate().isBefore(startDate) && 
                             !b.getFecha().toLocalDate().isAfter(endDate))
                .collect(Collectors.groupingBy(
                    Bill::getFecha,
                    Collectors.summingDouble(Bill::getTotal)
                ));
    }
}