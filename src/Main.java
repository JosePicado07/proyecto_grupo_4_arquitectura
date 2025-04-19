import controllers.*;
import models.*;
import services.*;
import utils.DateUtils;
import utils.ValidationUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    static PrintStream out = System.out;
    static int menuOption = 0;
    
    // Servicios
    static AuthService authService;
    static ProductService productService;
    static CartService cartService;
    static BillService billService;
    static DiscountService discountService;
    static ReportService reportService;
    
    // Controladores
    static UserController userController;
    static ProductController productController;
    static CartController cartController;
    static BillController billController;
    static ReportController reportController;
    
    public static void main(String[] args) throws IOException {
        initializeServices();
        showMainMenu();
    }
    
    private static void initializeServices() {
        // Inicializar servicios
        authService = new AuthService();
        discountService = new DiscountService();
        productService = new ProductService();
        cartService = new CartService();
        billService = new BillService(discountService);
        reportService = new ReportService(billService);
        
        // Inicializar controladores
        userController = new UserController(authService);
        productController = new ProductController(productService);
        cartController = new CartController(cartService, productService);
        billController = new BillController(billService, cartService);
        reportController = new ReportController(reportService);
        
        // Inicializar catálogo de productos
        initializeProducts();
    }
    
    private static void initializeProducts() {
        // Lo mismo que el método populateProducts() original pero usando el controlador
        productController.addProduct("Procesador AMD Ryzen 5 5600X", 135000.0, "Componentes");
        productController.addProduct("Memoria RAM DDR4 16GB 3200MHz", 45000.0, "Componentes");
        productController.addProduct("Disco SSD NVMe 1TB", 72000.0, "Componentes");
        productController.addProduct("Tarjeta Madre ASUS B550M", 88000.0, "Componentes");
        productController.addProduct("Fuente de Poder 650W 80+ Bronze", 43000.0, "Componentes");
        productController.addProduct("Tarjeta Gráfica NVIDIA RTX 3060", 230000.0, "Componentes");
        productController.addProduct("Enfriamiento Líquido 240mm", 69000.0, "Componentes");
        productController.addProduct("Gabinete ATX con RGB", 39000.0, "Perifericos");
        productController.addProduct("Monitor 24'' Full HD 144Hz", 98000.0, "Perifericos");
        productController.addProduct("Teclado Mecánico RGB", 27000.0, "Perifericos");
    }
    
    private static void showMainMenu() throws IOException {
        boolean exit = false;
        
        while (!exit) {
            out.println("\n===== TECH COSTA RICA =====");
            out.println("1. Registrar usuario");
            out.println("2. Iniciar sesión");
            out.println("3. Salir");
            out.print("Seleccione una opción: ");
            
            try {
                int option = Integer.parseInt(in.readLine());
                
                switch (option) {
                    case 1:
                        registerUser();
                        break;
                    case 2:
                        if (login()) {
                            showUserMenu();
                        }
                        break;
                    case 3:
                        exit = true;
                        out.println("Gracias por usar el programa");
                        break;
                    default:
                        out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                out.println("Por favor, ingrese un número válido.");
            }
        }
    }
    
    private static void registerUser() throws IOException {
        out.println("\n===== REGISTRO DE USUARIO =====");
        
        out.print("Cédula: ");
        String cedula = in.readLine();
        
        out.print("Nombre: ");
        String name = in.readLine();
        
        out.print("Primer Apellido: ");
        String firstLastName = in.readLine();
        
        out.print("Segundo Apellido: ");
        String secondLastName = in.readLine();
        
        out.print("Email: ");
        String email = in.readLine();
        
        out.print("Teléfono: ");
        String phone = in.readLine();
        
        out.print("Dirección: ");
        String address = in.readLine();
        
        out.print("Fecha de nacimiento (DD/MM/AAAA): ");
        String birthDateStr = in.readLine();
        LocalDate birthDate = DateUtils.parseDate(birthDateStr);
        
        out.print("Contraseña: ");
        String password = in.readLine();
        
        // Validar los datos
        if (!ValidationUtils.isValidCedula(cedula)) {
            out.println("Error: Cédula inválida.");
            return;
        }
        
        if (!ValidationUtils.isValidEmail(email)) {
            out.println("Error: Email inválido.");
            return;
        }
        
        if (!ValidationUtils.isValidPhone(phone)) {
            out.println("Error: Teléfono inválido.");
            return;
        }
        
        if (!ValidationUtils.isValidName(name) || 
            !ValidationUtils.isValidName(firstLastName) || 
            !ValidationUtils.isValidName(secondLastName)) {
            out.println("Error: Uno o más nombres inválidos.");
            return;
        }
        
        if (!ValidationUtils.isValidPassword(password)) {
            out.println("Error: La contraseña debe tener al menos 8 caracteres.");
            return;
        }
        
        if (birthDate == null) {
            out.println("Error: Fecha de nacimiento inválida.");
            return;
        }
        
        // Registrar el usuario
        boolean success = userController.registerUser(
            cedula, name, firstLastName, secondLastName, email, 
            phone, address, birthDate, password, User.UserType.USER
        );
        
        if (success) {
            out.println("Usuario registrado exitosamente.");
        } else {
            out.println("Error al registrar el usuario. El email o cédula ya existe.");
        }
    }
    
    private static boolean login() throws IOException {
        out.println("\n===== INICIAR SESIÓN =====");
        
        int attempts = 0;
        boolean loggedIn = false;
        
        while (attempts < 3 && !loggedIn) {
            out.print("Email: ");
            String email = in.readLine();
            
            out.print("Contraseña: ");
            String password = in.readLine();
            
            AuthService.LoginResult result = userController.login(email, password);
            
            if (result.isSuccess()) {
                out.println("Inicio de sesión exitoso.");
                loggedIn = true;
            } else {
                out.println(result.getMessage());
                attempts++;
            }
        }
        
        if (!loggedIn && attempts >= 3) {
            out.println("Demasiados intentos fallidos. Volviendo al menú principal.");
        }
        
        return loggedIn;
    }
    
    private static void showUserMenu() throws IOException {
        boolean exit = false;
        
        while (!exit) {
            User currentUser = userController.getCurrentUser();
            out.println("\n===== MENÚ PRINCIPAL =====");
            out.println("Bienvenido, " + currentUser.getName() + " " + currentUser.getFirstLastName());
            out.println("1. Ver catálogo de productos");
            out.println("2. Ver carrito de compras");
            out.println("3. Finalizar compra");
            
            // Opciones adicionales según el tipo de usuario
            if (currentUser.getUserType() == User.UserType.ADMIN || 
                currentUser.getUserType() == User.UserType.SELLER) {
                out.println("4. Módulo de reportes");
            }
            
            out.println("0. Cerrar sesión");
            out.print("Seleccione una opción: ");
            
            try {
                int option = Integer.parseInt(in.readLine());
                
                switch (option) {
                    case 1:
                        showProductCatalog();
                        break;
                    case 2:
                        showCart();
                        break;
                    case 3:
                        checkout();
                        break;
                    case 4:
                        if (currentUser.getUserType() == User.UserType.ADMIN || 
                            currentUser.getUserType() == User.UserType.SELLER) {
                            showReportsMenu();
                        } else {
                            out.println("Opción inválida. Intente de nuevo.");
                        }
                        break;
                    case 0:
                        userController.logout();
                        exit = true;
                        out.println("Sesión cerrada.");
                        break;
                    default:
                        out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                out.println("Por favor, ingrese un número válido.");
            }
        }
    }
    
    private static void showProductCatalog() throws IOException {
        boolean exit = false;
        
        while (!exit) {
            out.println("\n===== CATÁLOGO DE PRODUCTOS =====");
            
            // Mostrar productos por categoría
            Map<String, List<Product>> productsByCategory = productController.getProductsByCategory();
            
            for (String category : productsByCategory.keySet()) {
                out.println("\nCategoría: " + category);
                List<Product> products = productsByCategory.get(category);
                
                for (Product product : products) {
                    out.println(product.getId() + ". " + product.getNombre() + " - ₡" + product.getPrecio());
                }
            }
            
            out.println("\n1. Agregar producto al carrito");
            out.println("0. Volver al menú principal");
            out.print("Seleccione una opción: ");
            
            try {
                int option = Integer.parseInt(in.readLine());
                
                switch (option) {
                    case 1:
                        addToCart();
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                out.println("Por favor, ingrese un número válido.");
            }
        }
    }
    
    private static void addToCart() throws IOException {
        out.print("Ingrese el ID del producto: ");
        int productId = Integer.parseInt(in.readLine());
        
        Optional<Product> productOpt = productController.getProductById(productId);
        
        if (productOpt.isEmpty()) {
            out.println("Producto no encontrado.");
            return;
        }
        
        Product product = productOpt.get();
        out.println("Producto seleccionado: " + product.getNombre() + " - ₡" + product.getPrecio());
        
        out.print("Ingrese la cantidad: ");
        int quantity = Integer.parseInt(in.readLine());
        
        if (quantity <= 0) {
            out.println("La cantidad debe ser mayor a 0.");
            return;
        }
        
        boolean success = cartController.addToCart(productId, quantity);
        
        if (success) {
            out.println(quantity + " unidades de " + product.getNombre() + " agregadas al carrito.");
        } else {
            out.println("Error al agregar el producto al carrito.");
        }
    }
    
    private static void showCart() throws IOException {
        boolean exit = false;
        
        while (!exit) {
            out.println("\n===== CARRITO DE COMPRAS =====");
            
            List<Cart.CartItem> cartItems = cartController.getCartSummary();
            
            if (cartItems.isEmpty()) {
                out.println("El carrito está vacío.");
            } else {
                for (Cart.CartItem item : cartItems) {
                    Product product = item.getProduct();
                    out.println(product.getId() + ". " + product.getNombre() + 
                                " - Cantidad: " + item.getQuantity() + 
                                " - Subtotal: ₡" + item.getSubtotal());
                }
                
                out.println("\nSubtotal: ₡" + cartController.calculateSubtotal());
            }
            
            out.println("\n1. Modificar cantidad de un producto");
            out.println("2. Eliminar producto del carrito");
            out.println("3. Vaciar carrito");
            out.println("4. Finalizar compra");
            out.println("0. Volver al menú principal");
            out.print("Seleccione una opción: ");
            
            try {
                int option = Integer.parseInt(in.readLine());
                
                switch (option) {
                    case 1:
                        updateCartItemQuantity();
                        break;
                    case 2:
                        removeFromCart();
                        break;
                    case 3:
                        cartController.clearCart();
                        out.println("Carrito vaciado.");
                        break;
                    case 4:
                        if (checkout()) {
                            exit = true;
                        }
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                out.println("Por favor, ingrese un número válido.");
            }
        }
    }
    
    private static void updateCartItemQuantity() throws IOException {
        List<Cart.CartItem> cartItems = cartController.getCartSummary();
        
        if (cartItems.isEmpty()) {
            out.println("El carrito está vacío.");
            return;
        }
        
        out.print("Ingrese el ID del producto a modificar: ");
        int productId = Integer.parseInt(in.readLine());
        
        boolean productExists = false;
        for (Cart.CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                productExists = true;
                break;
            }
        }
        
        if (!productExists) {
            out.println("Producto no encontrado en el carrito.");
            return;
        }
        
        out.print("Ingrese la nueva cantidad: ");
        int newQuantity = Integer.parseInt(in.readLine());
        
        if (newQuantity <= 0) {
            out.println("La cantidad debe ser mayor a 0.");
            return;
        }
        
        // Obtener la cantidad actual
        int currentQuantity = cartController.getProductQuantities().getOrDefault(productId, 0);
        
        if (newQuantity > currentQuantity) {
            // Necesitamos agregar unidades
            boolean success = cartController.addToCart(productId, newQuantity - currentQuantity);
            
            if (success) {
                out.println("Cantidad actualizada a " + newQuantity + ".");
            } else {
                out.println("Error al actualizar la cantidad.");
            }
        } else if (newQuantity < currentQuantity) {
            // Necesitamos quitar unidades
            boolean success = cartController.removeFromCart(productId, currentQuantity - newQuantity);
            
            if (success) {
                out.println("Cantidad actualizada a " + newQuantity + ".");
            } else {
                out.println("Error al actualizar la cantidad.");
            }
        } else {
            out.println("La cantidad es la misma, no se realizaron cambios.");
        }
    }
    
    private static void removeFromCart() throws IOException {
        List<Cart.CartItem> cartItems = cartController.getCartSummary();
        
        if (cartItems.isEmpty()) {
            out.println("El carrito está vacío.");
            return;
        }
        
        out.print("Ingrese el ID del producto a eliminar: ");
        int productId = Integer.parseInt(in.readLine());
        
        int currentQuantity = cartController.getProductQuantities().getOrDefault(productId, 0);
        
        if (currentQuantity == 0) {
            out.println("Producto no encontrado en el carrito.");
            return;
        }
        
        out.print("Ingrese la cantidad a eliminar (máximo " + currentQuantity + "): ");
        int quantity = Integer.parseInt(in.readLine());
        
        if (quantity <= 0 || quantity > currentQuantity) {
            out.println("Cantidad inválida.");
            return;
        }
        
        boolean success = cartController.removeFromCart(productId, quantity);
        
        if (success) {
            out.println(quantity + " unidades eliminadas del carrito.");
        } else {
            out.println("Error al eliminar productos del carrito.");
        }
    }
    
    private static boolean checkout() throws IOException {
        List<Cart.CartItem> cartItems = cartController.getCartSummary();
        
        if (cartItems.isEmpty()) {
            out.println("El carrito está vacío. No se puede finalizar la compra.");
            return false;
        }
        
        out.println("\n===== FINALIZAR COMPRA =====");
        
        // Mostrar resumen del carrito
        out.println("Resumen de compra:");
        for (Cart.CartItem item : cartItems) {
            Product product = item.getProduct();
            out.println(product.getNombre() + 
                        " - Cantidad: " + item.getQuantity() + 
                        " - Subtotal: ₡" + item.getSubtotal());
        }
        
        double subtotal = cartController.calculateSubtotal();
        double impuestos = subtotal * 0.13;
        double total = subtotal + impuestos;
        
        out.println("\nSubtotal: ₡" + subtotal);
        out.println("Impuestos (13%): ₡" + impuestos);
        out.println("Total: ₡" + total);
        
        out.print("\n¿Tiene un código promocional? (S/N): ");
        String tieneCodigoPromo = in.readLine();
        
        String codigoPromo = null;
        if (tieneCodigoPromo.equalsIgnoreCase("S")) {
            out.print("Ingrese el código promocional: ");
            codigoPromo = in.readLine();
        }
        
        out.print("\nIngrese el número de comprobante SINPE móvil: ");
        int sinpe = Integer.parseInt(in.readLine());
        
        // Crear la factura
        Bill factura = billController.createBill(sinpe, userController.getCurrentUser(), codigoPromo);
        
        if (factura != null) {
            out.println("\n===== COMPRA EXITOSA =====");
            out.println("Su factura ha sido registrada con el número: " + factura.getId());
            factura.imprimirFactura();
            return true;
        } else {
            out.println("Error al procesar la compra. Verifique los datos e intente nuevamente.");
            return false;
        }
    }
    
    private static void showReportsMenu() throws IOException {
        boolean exit = false;
        
        while (!exit) {
            out.println("\n===== MÓDULO DE REPORTES =====");
            out.println("1. Ver historial completo de compras");
            out.println("2. Ver resumen de ventas diarias");
            out.println("3. Buscar compras por cédula");
            out.println("0. Volver al menú principal");
            out.print("Seleccione una opción: ");
            
            try {
                int option = Integer.parseInt(in.readLine());
                
                switch (option) {
                    case 1:
                        showFullReport();
                        break;
                    case 2:
                        showDailyReport();
                        break;
                    case 3:
                        searchByCedula();
                        break;
                    case 0:
                        exit = true;
                        break;
                    default:
                        out.println("Opción inválida. Intente de nuevo.");
                }
            } catch (NumberFormatException e) {
                out.println("Por favor, ingrese un número válido.");
            }
        }
    }
    
    private static void showFullReport() {
        out.println("\n===== HISTORIAL COMPLETO DE COMPRAS =====");
        
        List<Bill> bills = billController.getAllBills();
        
        if (bills.isEmpty()) {
            out.println("No hay facturas registradas.");
            return;
        }
        
        for (Bill bill : bills) {
            bill.imprimirFactura();
            out.println("----------------------------------------------------");
        }
    }
    
    private static void showDailyReport() throws IOException {
        out.println("\n===== RESUMEN DE VENTAS DIARIAS =====");
        
        out.print("Ingrese la fecha (DD/MM/AAAA) o deje en blanco para la fecha actual: ");
        String dateStr = in.readLine();
        
        LocalDate date;
        if (dateStr == null || dateStr.trim().isEmpty()) {
            date = LocalDate.now();
        } else {
            date = DateUtils.parseDate(dateStr);
            if (date == null) {
                out.println("Formato de fecha inválido.");
                return;
            }
        }
        
        Map<String, Object> report = reportController.getDailyReport(date);
        
        if (report == null || report.isEmpty()) {
            out.println("No hay datos para la fecha especificada.");
            return;
        }
        
        out.println("\nFecha: " + report.get("date"));
        out.println("Cantidad de transacciones: " + report.get("totalTransactions"));
        out.println("Total facturado: ₡" + report.get("totalAmount"));
        
        @SuppressWarnings("unchecked")
        Map<String, Long> productCounts = (Map<String, Long>) report.get("topProducts");
        
        if (productCounts != null && !productCounts.isEmpty()) {
            out.println("\nDesglose por producto:");
            productCounts.forEach((nombre, cantidad) -> 
                out.println("- " + nombre + ": " + cantidad + " unidades")
            );
        }
    }
    
    private static void searchByCedula() throws IOException {
        out.println("\n===== BÚSQUEDA DE COMPRAS POR CÉDULA =====");
        
        out.print("Ingrese la cédula del cliente: ");
        String cedula = in.readLine();
        
        if (!ValidationUtils.isValidCedula(cedula)) {
            out.println("Formato de cédula inválido.");
            return;
        }
        
        List<Bill> clientBills = billController.getBillsByUser(cedula);
        
        if (clientBills.isEmpty()) {
            out.println("No se encontraron facturas para la cédula especificada.");
            return;
        }
        
        out.println("\nFacturas encontradas: " + clientBills.size());
        
        for (Bill bill : clientBills) {
            bill.imprimirFactura();
            out.println("----------------------------------------------------");
        }
    }
}