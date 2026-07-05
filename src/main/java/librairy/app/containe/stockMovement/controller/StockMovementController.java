package librairy.app.containe.stockMovement.controller;

import librairy.app.containe.stockMovement.service.StockMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/books/{bookId}/stock")
public class StockMovementController {

  private final StockMovementService stockMovementService;

  public StockMovementController(StockMovementService stockMovementService) {
    this.stockMovementService = stockMovementService;
  }

  @GetMapping
  public ResponseEntity<Integer> getStock(@PathVariable String bookId) {
    return ResponseEntity.ok(stockMovementService.getStockByBookId(bookId));
  }

  @GetMapping("/available")
  public ResponseEntity<Boolean> isAvailable(@PathVariable String bookId) {
    return ResponseEntity.ok(stockMovementService.isAvailable(bookId));
  }
}
