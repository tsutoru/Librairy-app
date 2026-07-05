package librairy.app.containe.stockMovement.controller;

import java.util.List;
import java.util.Map;
import librairy.app.containe.stockMovement.service.StockMovementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class StockMovementController {

  private final StockMovementService stockMovementService;

  public StockMovementController(StockMovementService stockMovementService) {
    this.stockMovementService = stockMovementService;
  }

  @GetMapping("/books/{bookId}/stock")
  public ResponseEntity<Integer> getStock(@PathVariable String bookId) {
    return ResponseEntity.ok(stockMovementService.getStockByBookId(bookId));
  }

  @GetMapping("/books/{bookId}/stock/available")
  public ResponseEntity<Boolean> isAvailable(@PathVariable String bookId) {
    return ResponseEntity.ok(stockMovementService.isAvailable(bookId));
  }

  @GetMapping("/books/low-stock")
  public ResponseEntity<List<Map<String, Object>>> getLowStockBooks(
      @RequestParam(defaultValue = "3") int threshold) {
    return ResponseEntity.ok(stockMovementService.getLowStockBooks(threshold));
  }
}
