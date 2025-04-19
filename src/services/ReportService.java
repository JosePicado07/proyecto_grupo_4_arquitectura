package services;

import models.Bill;
import models.Product;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportService {
    
    private final BillService billService;
    
    public ReportService(BillService billService) {
        this.billService = billService;
    }
    
    public Map<String, Object> generateDailyReport(LocalDate date) {
        List<Bill> dailyBills = billService.getBillsByDate(date);
        
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
    
    public Map<LocalDate, Double> getSalesByDateRange(LocalDate startDate, LocalDate endDate) {
        return billService.getAllBills().stream()
                .filter(b -> !b.getFecha().toLocalDate().isBefore(startDate) && 
                             !b.getFecha().toLocalDate().isAfter(endDate))
                .collect(Collectors.groupingBy(
                    bill -> bill.getFecha().toLocalDate(),
                    Collectors.summingDouble(Bill::getTotal)
                ));
    }
    
    public Map<Integer, Double> getSalesByHourOfDay(LocalDate date) {
        return billService.getBillsByDate(date).stream()
                .collect(Collectors.groupingBy(
                    bill -> bill.getFecha().getHour(),
                    Collectors.summingDouble(Bill::getTotal)
                ));
    }
    
    public Map<String, Double> getSalesByProduct(LocalDate date) {
        List<Bill> bills = billService.getBillsByDate(date);
        
        Map<String, Double> productSales = new HashMap<>();
        
        bills.forEach(bill -> {
            bill.getProductos().forEach(product -> {
                String productName = product.getNombre();
                double price = product.getPrecio();
                
                productSales.put(
                    productName, 
                    productSales.getOrDefault(productName, 0.0) + price
                );
            });
        });
        
        return productSales;
    }
    
    public double getTotalSales() {
        return billService.getAllBills().stream()
                .mapToDouble(Bill::getTotal)
                .sum();
    }
    
    public double getTotalSalesForDate(LocalDate date) {
        return billService.getBillsByDate(date).stream()
                .mapToDouble(Bill::getTotal)
                .sum();
    }
    
    public Map<String, Integer> getProductQuantitySold(LocalDate date) {
        List<Bill> bills = billService.getBillsByDate(date);
        
        return bills.stream()
                .flatMap(bill -> bill.getProductos().stream())
                .collect(Collectors.groupingBy(
                    Product::getNombre,
                    Collectors.summingInt(p -> 1)
                ));
    }
    
    // Para RF16: Búsqueda en historial por criterios
    public List<Bill> searchBillHistory(String criteria) {
        return billService.searchBills(criteria);
    }
    
    // Para RF23-24: Visualización de tendencias
    public Map<String, Object> generateSalesTrends() {
        Map<String, Object> trends = new HashMap<>();
        
        // Tendencias de los últimos 7 días
        LocalDate today = LocalDate.now();
        LocalDate weekAgo = today.minusDays(7);
        
        trends.put("weeklyTrend", getSalesByDateRange(weekAgo, today));
        
        // Ventas por hora del día (promedio de la semana)
        Map<Integer, Double> hourlyAverage = new HashMap<>();
        
        for (int i = 0; i < 24; i++) {
            final int hour = i;
            double total = 0;
            int days = 0;
            
            for (LocalDate date = weekAgo; !date.isAfter(today); date = date.plusDays(1)) {
                double dailyHourSales = billService.getBillsByDate(date).stream()
                        .filter(b -> b.getFecha().getHour() == hour)
                        .mapToDouble(Bill::getTotal)
                        .sum();
                
                if (dailyHourSales > 0) {
                    total += dailyHourSales;
                    days++;
                }
            }
            
            if (days > 0) {
                hourlyAverage.put(hour, total / days);
            } else {
                hourlyAverage.put(hour, 0.0);
            }
        }
        
        trends.put("hourlyTrend", hourlyAverage);
        
        return trends;
    }
}