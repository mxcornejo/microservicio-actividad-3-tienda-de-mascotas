// package com.tiendamascotas.tiendamascotas.services;

// import com.tiendamascotas.tiendamascotas.dto.ProfitSummary;
// import com.tiendamascotas.tiendamascotas.models.Customer;
// import com.tiendamascotas.tiendamascotas.models.Product;
// import com.tiendamascotas.tiendamascotas.models.Sale;
// import jakarta.annotation.PostConstruct;
// import org.springframework.stereotype.Service;

// import java.time.LocalDate;
// import java.util.*;
// import java.util.stream.Collectors;

// @Service
// public class DataService {
// private final List<Product> products = new ArrayList<>();
// private final List<Customer> customers = new ArrayList<>();
// private final List<Sale> sales = new ArrayList<>();

// private int productIdCounter = 1;
// private int customerIdCounter = 1;
// private int saleIdCounter = 1;

// // Datos iniciales
// @PostConstruct
// void seed() {
// // Productos (>= 3)
// agregarProducto("Alimento Premium", "ALIMENTO", 7000.0, 12990.0);
// agregarProducto("Collar Reflectante", "ACCESORIO", 2000.0, 5990.0);
// agregarProducto("Arena Gato 10kg", "HIGIENE", 4500.0, 9990.0);

// // Clientes (>= 3)
// agregarCliente("Ana", "ana@mail.com");
// agregarCliente("Luis", "luis@mail.com");
// agregarCliente("Paty", "paty@mail.com");

// // Ventas (>= 3) — usa los IDs reales existentes
// // Busca por nombre para obtener IDs actuales:
// int pAlimento = buscarProductoPorNombre("Alimento Premium");
// int pArena = buscarProductoPorNombre("Arena Gato 10kg");
// int pCollar = buscarProductoPorNombre("Collar Reflectante");

// int cAna = buscarClientePorEmail("ana@mail.com");
// int cLuis = buscarClientePorEmail("luis@mail.com");
// int cPaty = buscarClientePorEmail("paty@mail.com");

// // Registrar ventas (fecha hoy y días anteriores)
// registrarVenta(pAlimento, cAna, 2, 12990.0, LocalDate.now().minusDays(2));
// registrarVenta(pArena, cLuis, 1, 9990.0, LocalDate.now().minusDays(1));
// registrarVenta(pAlimento, cPaty, 1, 12990.0, LocalDate.now());
// registrarVenta(pCollar, cAna, 1, 5990.0, LocalDate.now());
// }

// // Productos
// public Product agregarProducto(String nombre, String categoria, double costo,
// double precio) {
// if (nombre == null || nombre.isBlank()) throw new
// IllegalArgumentException("El nombre del producto no puede estar vacío.");
// if (costo < 0) throw new IllegalArgumentException("El costo no puede ser
// negativo.");
// if (precio <= 0) throw new IllegalArgumentException("El precio debe ser mayor
// a 0.");
// Product p = new Product(productIdCounter++, nombre, categoria, costo,
// precio);
// products.add(p);
// return p;
// }

// public List<Product> obtenerProductos() {
// return products;
// }

// // Clientes
// public Customer agregarCliente(String nombre, String email) {
// if (nombre == null || nombre.isBlank()) throw new
// IllegalArgumentException("El nombre del cliente no puede estar vacío.");
// if (email == null || email.isBlank()) throw new IllegalArgumentException("El
// email no puede estar vacío.");
// Customer c = new Customer(customerIdCounter++, nombre, email);
// customers.add(c);
// return c;
// }

// public List<Customer> obtenerClientes() {
// return customers;
// }

// // Ventas
// public Sale registrarVenta(int productId, int customerId, int cantidad,
// double precioUnitario, LocalDate fecha) {
// if (cantidad <= 0) throw new IllegalArgumentException("La cantidad debe ser
// mayor a 0.");
// if (precioUnitario <= 0) throw new IllegalArgumentException("El precio
// unitario debe ser mayor a 0.");
// if (fecha == null) throw new IllegalArgumentException("La fecha es
// obligatoria.");

// Product producto = buscarProducto(productId).orElseThrow(() -> new
// IllegalArgumentException("Producto no existe."));
// Customer cliente = buscarCliente(customerId).orElseThrow(() -> new
// IllegalArgumentException("Cliente no existe."));
// // Nota: no forzamos que precioUnitario == producto.getPrice(), pero se
// podría validar si el negocio lo requiere.

// Sale nueva = new Sale(saleIdCounter++, producto.getId(), cliente.getId(),
// cantidad, fecha, precioUnitario);
// sales.add(nueva);
// return nueva;
// }

// public List<Sale> obtenerVentas() {
// return sales;
// }

// // Alias en inglés para consistencia con controladores en inglés
// public List<Sale> getAllSales() {
// return obtenerVentas();
// }

// public Optional<Sale> getSale(int id) {
// return sales.stream().filter(s -> s.getId() == id).findFirst();
// }

// public List<Sale> getSalesByDate(LocalDate fecha) {
// if (fecha == null) throw new IllegalArgumentException("La fecha es
// obligatoria.");
// return sales.stream()
// .filter(v -> fecha.equals(v.getDate()))
// .toList();
// }

// public List<Sale> getSalesByRange(LocalDate desde, LocalDate hasta) {
// if (desde == null || hasta == null) throw new
// IllegalArgumentException("Fechas 'from' y 'to' son obligatorias.");
// return sales.stream()
// .filter(v -> !v.getDate().isBefore(desde) && !v.getDate().isAfter(hasta))
// .toList();
// }

// // Ganancias (ingresos) por periodo
// public double gananciasPorDia(LocalDate fecha) {
// if (fecha == null) throw new IllegalArgumentException("La fecha es
// obligatoria.");
// return sales.stream()
// .filter(v -> fecha.equals(v.getDate()))
// .mapToDouble(v -> v.getUnitPrice() * v.getQuantity())
// .sum();
// }

// public double gananciasPorMes(int anio, int mes) {
// validarAnio(anio);
// validarMes(mes);
// return sales.stream()
// .filter(v -> v.getDate().getYear() == anio && v.getDate().getMonthValue() ==
// mes)
// .mapToDouble(v -> v.getUnitPrice() * v.getQuantity())
// .sum();
// }

// public double gananciasPorAnio(int anio) {
// validarAnio(anio);
// return sales.stream()
// .filter(v -> v.getDate().getYear() == anio)
// .mapToDouble(v -> v.getUnitPrice() * v.getQuantity())
// .sum();
// }

// // Resumen con costos (utilidad bruta)
// public ProfitSummary resumenPorDia(LocalDate fecha, String moneda) {
// if (fecha == null) throw new IllegalArgumentException("La fecha es
// obligatoria.");
// return construirResumen(
// "Día " + fecha,
// moneda,
// sales.stream().filter(v ->
// fecha.equals(v.getDate())).collect(Collectors.toList())
// );
// }

// public ProfitSummary resumenPorMes(int anio, int mes, String moneda) {
// validarAnio(anio);
// validarMes(mes);
// return construirResumen(
// String.format("%04d-%02d", anio, mes),
// moneda,
// sales.stream().filter(v -> v.getDate().getYear() == anio &&
// v.getDate().getMonthValue() == mes).collect(Collectors.toList())
// );
// }

// public ProfitSummary resumenPorAnio(int anio, String moneda) {
// validarAnio(anio);
// return construirResumen(
// String.valueOf(anio),
// moneda,
// sales.stream().filter(v -> v.getDate().getYear() ==
// anio).collect(Collectors.toList())
// );
// }

// private ProfitSummary construirResumen(String periodo, String moneda,
// List<Sale> ventasPeriodo) {
// double ingresos = ventasPeriodo.stream()
// .mapToDouble(v -> v.getUnitPrice() * v.getQuantity())
// .sum();

// // Costo total basado en el costo del producto
// Map<Integer, Product> productIndex =
// products.stream().collect(Collectors.toMap(Product::getId, p -> p));
// double costo = ventasPeriodo.stream()
// .mapToDouble(v -> {
// Product p = productIndex.get(v.getProductId());
// return (p != null ? p.getCost() : 0.0) * v.getQuantity();
// })
// .sum();

// return new ProfitSummary(periodo, moneda, ingresos, costo,
// ventasPeriodo.size());
// }

// // Helpers
// public Optional<Product> buscarProducto(int productId) {
// return products.stream().filter(p -> p.getId() == productId).findFirst();
// }

// public Optional<Customer> buscarCliente(int customerId) {
// return customers.stream().filter(c -> c.getId() == customerId).findFirst();
// }

// // helpers rápidos
// private int buscarProductoPorNombre(String name) {
// return products.stream()
// .filter(p -> p.getName().equalsIgnoreCase(name))
// .findFirst().orElseThrow().getId();
// }

// private int buscarClientePorEmail(String email) {
// return customers.stream()
// .filter(c -> c.getEmail().equalsIgnoreCase(email))
// .findFirst().orElseThrow().getId();
// }

// private void validarMes(int mes) {
// if (mes < 1 || mes > 12) throw new IllegalArgumentException("Mes inválido
// (1-12).");
// }

// private void validarAnio(int anio) {
// if (anio < 1) throw new IllegalArgumentException("Año inválido.");
// }

// // Expuestos para otros controladores
// public List<Customer> getAllCustomers() {
// return customers;
// }

// public Customer getCustomerById(int id) {
// return customers.stream().filter(c -> c.getId() ==
// id).findFirst().orElse(null);
// }

// // Estadísticas: unidades vendidas por categoría en rango
// public Map<String, Integer> salesUnitsByCategory(LocalDate from, LocalDate
// to) {
// if (from == null || to == null) throw new IllegalArgumentException("Fechas
// 'from' y 'to' son obligatorias.");
// var productIndex = products.stream().collect(Collectors.toMap(Product::getId,
// p -> p));

// return sales.stream()
// .filter(v -> !v.getDate().isBefore(from) && !v.getDate().isAfter(to))
// .collect(Collectors.groupingBy(
// v -> {
// var p = productIndex.get(v.getProductId());
// return p != null ? p.getCategory() : "Desconocido";
// },
// Collectors.summingInt(Sale::getQuantity)
// ));
// }
// }
