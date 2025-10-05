package com.tiendamascotas.tiendamascotas.repository;

import com.tiendamascotas.tiendamascotas.dto.ProfitSummary;
import com.tiendamascotas.tiendamascotas.models.Customer;
import com.tiendamascotas.tiendamascotas.models.Product;
import com.tiendamascotas.tiendamascotas.models.Sale;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SaleRepositoryIT {

    @Autowired
    private SaleRepository saleRepo;

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Test
    void resumenPorDia_and_salesUnitsByCategory_work() {
        Product p1 = productRepo.save(new Product(0, "Alimento", "ALIMENTO", 7000.0, 12990.0));
        Product p2 = productRepo.save(new Product(0, "Collar", "ACCESORIO", 2000.0, 5990.0));
        Customer c = customerRepo.save(new Customer(0, "Ana", "ana@mail.com"));

        LocalDate day = LocalDate.of(2024, 4, 1);
        saleRepo.save(new Sale(0, p1.getId(), c.getId(), 2, day, 12990.0));
        saleRepo.save(new Sale(0, p2.getId(), c.getId(), 1, day, 5990.0));

        ProfitSummary summary = saleRepo.resumenPorDia(day, "CLP");
        assertThat(summary).isNotNull();
        assertThat(summary.getRevenue()).isEqualTo(2 * 12990.0 + 1 * 5990.0);

        Map<String, Integer> byCategory = saleRepo.salesUnitsByCategory(day, day);
        assertThat(byCategory).containsEntry("ALIMENTO", 2);
        assertThat(byCategory).containsEntry("ACCESORIO", 1);
    }
}
