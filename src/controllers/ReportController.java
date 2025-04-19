package controllers;

import services.ReportService;

import java.time.LocalDate;
import java.util.Map;

public class ReportController {
    
    private final ReportService reportService;
    
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }
    
    public Map<String, Object> getDailyReport(LocalDate date) {
        return reportService.generateDailyReport(date);
    }
    
    public Map<LocalDate, Double> getSalesTrend(LocalDate startDate, LocalDate endDate) {
        return reportService.getSalesByDateRange(startDate, endDate);
    }
    
    public Map<Integer, Double> getSalesByHourOfDay(LocalDate date) {
        return reportService.getSalesByHourOfDay(date);
    }
    
    public Map<String, Double> getProductSales(LocalDate date) {
        return reportService.getSalesByProduct(date);
    }
    
    public double getTotalSales() {
        return reportService.getTotalSales();
    }
    
    public double getDailySales(LocalDate date) {
        return reportService.getTotalSalesForDate(date);
    }
    
    public Map<String, Integer> getProductQuantitySold(LocalDate date) {
        return reportService.getProductQuantitySold(date);
    }
    
    public Map<String, Object> generateSalesTrends() {
        return reportService.generateSalesTrends();
    }
}