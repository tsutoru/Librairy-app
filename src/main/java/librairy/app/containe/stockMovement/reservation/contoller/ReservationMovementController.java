package librairy.app.containe.stockMovement.reservation.contoller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import librairy.app.containe.stockMovement.reservation.entity.ReservationMovement;
import librairy.app.containe.stockMovement.reservation.service.ReservationMovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
public class ReservationMovementController {

  private final ReservationMovementService reservationMovementService;

  public ReservationMovementController(ReservationMovementService reservationMovementService) {
    this.reservationMovementService = reservationMovementService;
  }

  @PostMapping
  public ResponseEntity<ReservationMovement> recordReservation(
      @RequestBody Map<String, Object> body) {
    String bookId = (String) body.get("bookId");
    String customerId = (String) body.get("customerId");
    int quantity = (int) body.get("quantity");
    LocalDate expirationDate = LocalDate.parse((String) body.get("expirationDate"));
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            reservationMovementService.recordReservation(
                bookId, customerId, quantity, expirationDate));
  }

  @GetMapping
  public ResponseEntity<List<ReservationMovement>> getAll() {
    return ResponseEntity.ok(reservationMovementService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReservationMovement> getById(@PathVariable String id) {
    return ResponseEntity.ok(reservationMovementService.getById(id));
  }

  @GetMapping("/book/{bookId}")
  public ResponseEntity<List<ReservationMovement>> getByBookId(@PathVariable String bookId) {
    return ResponseEntity.ok(reservationMovementService.getByBookId(bookId));
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<ReservationMovement>> getByCustomerId(
      @PathVariable String customerId) {
    return ResponseEntity.ok(reservationMovementService.getByCustomerId(customerId));
  }
}
