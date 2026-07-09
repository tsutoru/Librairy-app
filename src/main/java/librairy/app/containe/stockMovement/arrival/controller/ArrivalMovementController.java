package librairy.app.containe.stockMovement.arrival.controller;

import java.util.List;
import java.util.Map;
import librairy.app.containe.stockMovement.arrival.entity.ArrivalMovement;
import librairy.app.containe.stockMovement.arrival.service.ArrivalMovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/arrivals")
public class ArrivalMovementController {

  private final ArrivalMovementService arrivalMovementService;

  public ArrivalMovementController(ArrivalMovementService arrivalMovementService) {
    this.arrivalMovementService = arrivalMovementService;
  }

  @PostMapping
  public ResponseEntity<ArrivalMovement> recordArrival(@RequestBody Map<String, Object> body) {
    String bookId = (String) body.get("bookId");
    int quantity = (int) body.get("quantity");
    String supplier = (String) body.get("supplier");
    String format = (String) body.get("format");
    double price = ((Number) body.get("price")).doubleValue();

    return ResponseEntity.status(HttpStatus.CREATED)
            .body(
                    arrivalMovementService.recordArrival(
                            bookId,
                            quantity,
                            supplier,
                            format,
                            price
                    )
            );
  }

  @GetMapping
  public ResponseEntity<List<ArrivalMovement>> getAll() {
    return ResponseEntity.ok(arrivalMovementService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ArrivalMovement> getById(@PathVariable String id) {
    return ResponseEntity.ok(arrivalMovementService.getById(id));
  }

  @GetMapping("/book/{bookId}")
  public ResponseEntity<List<ArrivalMovement>> getByBookId(@PathVariable String bookId) {
    return ResponseEntity.ok(arrivalMovementService.getByBookId(bookId));
  }

  @GetMapping("/supplier/{supplier}")
  public ResponseEntity<List<ArrivalMovement>> getBySupplier(@PathVariable String supplier) {
    return ResponseEntity.ok(arrivalMovementService.getBySupplier(supplier));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable String id) {
    arrivalMovementService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
