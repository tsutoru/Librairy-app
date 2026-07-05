package librairy.app.containe.stockMovement.reservationCancelled.controller;

import java.util.List;
import java.util.Map;
import librairy.app.containe.stockMovement.reservationCancelled.entity.ReservationCancelledMovement;
import librairy.app.containe.stockMovement.reservationCancelled.service.ReservationCancelledMovementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations/cancelled")
public class ReservationCancelledMovementController {

  private final ReservationCancelledMovementService cancelledMovementService;

  public ReservationCancelledMovementController(
      ReservationCancelledMovementService cancelledMovementService) {
    this.cancelledMovementService = cancelledMovementService;
  }

  @PostMapping
  public ResponseEntity<ReservationCancelledMovement> cancelReservation(
      @RequestBody Map<String, String> body) {
    String reservationId = body.get("reservationId");
    String customerId = body.get("customerId");
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(cancelledMovementService.cancelReservation(reservationId, customerId));
  }

  @GetMapping
  public ResponseEntity<List<ReservationCancelledMovement>> getAll() {
    return ResponseEntity.ok(cancelledMovementService.getAll());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ReservationCancelledMovement> getById(@PathVariable String id) {
    return ResponseEntity.ok(cancelledMovementService.getById(id));
  }

  @GetMapping("/book/{bookId}")
  public ResponseEntity<List<ReservationCancelledMovement>> getByBookId(
      @PathVariable String bookId) {
    return ResponseEntity.ok(cancelledMovementService.getByBookId(bookId));
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<ReservationCancelledMovement>> getByCustomerId(
      @PathVariable String customerId) {
    return ResponseEntity.ok(cancelledMovementService.getByCustomerId(customerId));
  }
}
