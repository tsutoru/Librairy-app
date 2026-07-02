package librairy.app.containe.stockMovement.sale.controller;

import java.util.List;
import java.util.Map;
import librairy.app.containe.stockMovement.sale.entity.SaleMovement;
import librairy.app.containe.stockMovement.sale.service.SaleMovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/sales")
public class SaleMovementController {

    private final SaleMovementService saleMovementService;

    public SaleMovementController(SaleMovementService saleMovementService) {
        this.saleMovementService = saleMovementService;
    }

    @PostMapping
    public ResponseEntity<SaleMovement> recordSale(
            @RequestBody Map<String, Object> body) {
        String bookId = (String) body.get("bookId");
        String customerId = (String) body.get("customerId");
        int quantity = (int) body.get("quantity");
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(saleMovementService.recordSale(bookId, customerId, quantity));
    }

    @GetMapping
    public ResponseEntity<List<SaleMovement>> getAll() {
        return ResponseEntity.ok(saleMovementService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SaleMovement> getById(@PathVariable String id) {
        return ResponseEntity.ok(saleMovementService.getById(id));
    }

    @GetMapping("/book/{bookId}")
    public ResponseEntity<List<SaleMovement>> getByBookId(
            @PathVariable String bookId) {
        return ResponseEntity.ok(saleMovementService.getByBookId(bookId));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SaleMovement>> getByCustomerId(
            @PathVariable String customerId) {
        return ResponseEntity.ok(saleMovementService.getByCustomerId(customerId));
    }
}