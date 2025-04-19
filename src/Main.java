import models.Bill;
import models.Product;
import models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class Main {
    static BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    static PrintStream out = System.out;
    static int menuOption = 0;
    static ArrayList<User> users = new ArrayList<>();
    static boolean valid = false;
    static User currentUser;
    static ArrayList<Product> productArrayList= new ArrayList<>();
    static ArrayList<Product> cart= new ArrayList<>();
    static ArrayList<Bill> billList = new ArrayList<>();


    public static void main(String[] args) throws IOException {
        populateProducts();
        listProducts();
        shop();
        pay();

    }

    static void menu() throws IOException {
        while (menuOption != 3) {
            out.println("Selecccione una opcion:");
            out.println("1. Registrar usuario");
            out.println("2. Iniciar sesion");
            out.println("3. Salir");
            switch (menuOption) {
                case 1:
                    registerUser();
                    break;
                case 2:
                    //add logic in case login valid
                    login();
                    break;
                case 3:
                    out.println("Gracias por usar el programa");
                    break;
            }

        }

    }

    //Login
    private static void login() throws IOException {
        int attemptsCounter = 0;
        while (attemptsCounter < 3) {
            out.println("Ingrese el email:");
            String email = in.readLine();
            out.println("Ingrese la contrasenna:");
            String password = in.readLine();

            valid = validate(email, password);
            attemptsCounter++;

            if (valid) {
                break;
            }else{
                out.println("Usuario o contrasenna incorrectos");
            }
        }
    }

    private static boolean validate(String email, String password){
        currentUser = users.stream().filter(user -> user.validateUser(email, password)).findFirst().get();
        if (currentUser != null){
            return true;
        }
        return false;
    }

    private static void registerUser() {

    }

    //Listar productos

    private static void listProducts() {
        productArrayList.stream().forEach(product -> {
            if (product.getId()<8){
                out.println("Categoria: Componentes");
                out.println(product.toString());
            }else{
                out.println("Categoria: Perifericos");
                out.println(product.toString());
            }

        });
    }

    //Generador de productos

    public static void populateProducts() {
        productArrayList.add(new Product(1, "Procesador AMD Ryzen 5 5600X", 135000.0));
        productArrayList.add(new Product(2, "Memoria RAM DDR4 16GB 3200MHz", 45000.0));
        productArrayList.add(new Product(3, "Disco SSD NVMe 1TB", 72000.0));
        productArrayList.add(new Product(4, "Tarjeta Madre ASUS B550M", 88000.0));
        productArrayList.add(new Product(5, "Fuente de Poder 650W 80+ Bronze", 43000.0));
        productArrayList.add(new Product(6, "Tarjeta Gráfica NVIDIA RTX 3060", 230000.0));
        productArrayList.add(new Product(7, "Enfriamiento Líquido 240mm", 69000.0));
        productArrayList.add(new Product(8, "Gabinete ATX con RGB", 39000.0));
        productArrayList.add(new Product(9, "Monitor 24'' Full HD 144Hz", 98000.0));
        productArrayList.add(new Product(10, "Teclado Mecánico RGB", 27000.0));
    }

    public static void printCart (){
        out.println("Su lista de compras es la siguiente:");
        cart.stream().forEach(product -> out.println(product.toString()));
    }

    //toda la logica de comprar productos y agregarlos al carrito del usuario actual
    public static void shop() throws IOException {
        int continueShopping = 1;
        int itemIndex = 0;

        while(continueShopping != 0){
            listProducts();
            out.println("Digite el id de productos que desea comprar");
            itemIndex = Integer.parseInt(in.readLine());
            if (itemIndex <1 || itemIndex >10){
                out.println("Valor invalido intente de nuevo");
            }else{
                Product tmpProduct = productArrayList.get(itemIndex-1);
                out.println("EL producto seleccionado es:"+ '\''+tmpProduct.toString() );
                out.println("Desea agregarlo al carrito? Digite 'N' para cancelar o pulse cualquier tecla para continuar");
                if (in.readLine().equals("N")){
                    out.println("Producto NO agregado al carrito");
                }else{
                    out.println("Digite cuantas unidades del producto desea comprar");
                    int quantity = Integer.parseInt(in.readLine());
                    for (int cont = 0; cont < quantity; cont++) {
                        cart.add(tmpProduct);
                    }
                }
                out.println("Desea comprar otro producto? Digite 0 para salir o cualquier otro valor para continuar");
                continueShopping = Integer.parseInt(in.readLine());
            }

        }
        printCart();
        out.println("Desea aliminar algun producto de su carrito? Digite 1 para eliminar algun producto o 0 para continuar");
        if (in.readLine().equals("1")){
            out.println("Digite el ID del producto que desea eliminar?");
            int id = Integer.parseInt(in.readLine());
            out.println("Digite cuantos productos de estos desea eliminar?");
            int quantity = Integer.parseInt(in.readLine());
            for (int cont = 0; cont < quantity; cont++) {
                for (Product p : cart) {
                    if (p.getId() == id) {
                        cart.remove(p);
                        break;
                    }
                }
            }
            printCart();
        }
    }

    public static void pay() throws IOException {
        double subtotal = cart.stream()
                .mapToDouble(Product::getPrecio)
                .sum();

        out.println("Tech CostaRica" + '\'' + '\'');
        out.println("Factura" + '\'' + '\'');
        out.println("Artículos");
        cart.stream().forEach(product -> out.println(product.toString()));
        out.println('\'' + '\'' + "Subtotal: " + subtotal + '\'' + '\'');
        out.println("Impuestos: 13%" + '\'' + '\'');
        out.println("Total: " + (subtotal * 1.13) + '\'' + '\'');

        out.println("Digite el número de comprobante de SINPE móvil para realizar la compra");
        int sinpe = Integer.parseInt(in.readLine());

        // Crear la factura y agregarla al historial
        Bill factura = new Bill(new ArrayList<>(cart), sinpe, currentUser);
        billList.add(factura);

        out.println("Compra exitosa. Su factura ha sido registrada.");
    }

    public static void reportsMenu() throws IOException {
        int opcion;
        do {
            out.println("\n--- Módulo de Reporterías ---");
            out.println("1. Ver historial completo de compras");
            out.println("2. Ver resumen de ventas diarias");
            out.println("3. Buscar compras por cédula");
            out.println("0. Salir del módulo de reportería");
            out.print("Seleccione el reporte que desea ver: ");
            opcion = Integer.parseInt(in.readLine());

            switch (opcion) {
                case 1:
                    fullReport();
                    break;
                case 2:
                    dailyReport();
                    break;
                case 3:
                    searchByCedula();
                    break;
                case 0:
                    out.println("Saliendo del módulo de reportería...");
                    break;
                default:
                    out.println("Opción inválida. Intente de nuevo.");
            }
        } while (opcion != 0);
    }

    public static void fullReport() {
        if (billList.isEmpty()) {
            out.println("No hay facturas registradas.");
            return;
        }
        for (Bill bill : billList) {
            bill.imprimirFactura();
            out.println("----------------------------------------------------");
        }
    }

    public static void dailyReport() {
        if (billList.isEmpty()) {
            out.println("No hay ventas registradas.");
            return;
        }

        Map<LocalDate, List<Bill>> agrupadoPorDia = billList.stream()
                .collect(Collectors.groupingBy(bill -> bill.getFecha().toLocalDate()));

        for (LocalDate dia : agrupadoPorDia.keySet()) {
            List<Bill> facturasDelDia = agrupadoPorDia.get(dia);
            double totalFacturado = facturasDelDia.stream()
                    .mapToDouble(Bill::getTotal)
                    .sum();

            Map<String, Long> productos = facturasDelDia.stream()
                    .flatMap(b -> b.getProductos().stream())
                    .collect(Collectors.groupingBy(Product::getNombre, Collectors.counting()));

            out.println("\nFecha: " + dia);
            out.println("Cantidad de transacciones: " + facturasDelDia.size());
            out.println("Total facturado: ₡" + totalFacturado);
            out.println("Desglose por producto:");
            productos.forEach((nombre, cantidad) -> out.println("- " + nombre + ": " + cantidad + " unidades"));
            out.println("----------------------------------------------------");
        }
    }

    public static void searchByCedula() throws IOException {
        out.println("Digite la cédula del cliente:");
        String cedula = in.readLine();

        List<Bill> facturasCliente = billList.stream()
                .filter(b -> b.getCedulaCliente().equals(cedula))
                .toList();

        if (facturasCliente.isEmpty()) {
            out.println("No se encontraron facturas para esa cédula.");
            return;
        }

        for (Bill bill : facturasCliente) {
            bill.imprimirFactura();
            out.println("----------------------------------------------------");
        }
    }
}