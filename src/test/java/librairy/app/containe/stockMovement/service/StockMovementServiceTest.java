package librairy.app.containe.stockMovement.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.book.repository.BookRepository;
import librairy.app.containe.exception.BadRequestException;
import librairy.app.containe.exception.NotFoundException;
import librairy.app.containe.stockMovement.arrival.entity.ArrivalMovement;
import librairy.app.containe.stockMovement.arrival.repository.ArrivalMovementRepository;
import librairy.app.containe.stockMovement.reservation.repository.ReservationMovementRepository;
import librairy.app.containe.stockMovement.reservationCancelled.repository.ReservationCancelledMovementRepository;
import librairy.app.containe.stockMovement.sale.entity.SaleMovement;
import librairy.app.containe.stockMovement.sale.repository.SaleMovementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StockMovementServiceTest {

  @Mock private ArrivalMovementRepository arrivalMovementRepository;
  @Mock private SaleMovementRepository saleMovementRepository;
  @Mock private ReservationMovementRepository reservationMovementRepository;
  @Mock private ReservationCancelledMovementRepository cancelledMovementRepository;
  @Mock private BookRepository bookRepository;

  @InjectMocks private StockMovementService stockMovementService;

  private static final String BOOK_ID = "b2dada51-4097-4d34-bb3c-46f67a4801b4";

  @Test
  void getStockByBookId_shouldThrow_whenIdIsNotValidUUID() {
    assertThatThrownBy(() -> stockMovementService.getStockByBookId("not-a-uuid"))
        .isInstanceOf(BadRequestException.class);
  }

  @Test
  void getStockByBookId_shouldThrow_whenBookNotFound() {
    given(bookRepository.findById(BOOK_ID)).willReturn(Optional.empty());

    assertThatThrownBy(() -> stockMovementService.getStockByBookId(BOOK_ID))
        .isInstanceOf(NotFoundException.class);
  }

  @Test
  void getStockByBookId_shouldReturnDifferenceBetweenInAndOut() {
    given(bookRepository.findById(BOOK_ID)).willReturn(Optional.of(new Book()));

    ArrivalMovement arrival = mock(ArrivalMovement.class);
    given(arrival.getQuantity()).willReturn(10);
    given(arrivalMovementRepository.findByBookId(BOOK_ID)).willReturn(List.of(arrival));

    SaleMovement sale = mock(SaleMovement.class);
    given(sale.getQuantity()).willReturn(4);
    given(saleMovementRepository.findByBookId(BOOK_ID)).willReturn(List.of(sale));

    given(cancelledMovementRepository.findByBookId(BOOK_ID)).willReturn(List.of());
    given(reservationMovementRepository.findByBookId(BOOK_ID)).willReturn(List.of());

    int stock = stockMovementService.getStockByBookId(BOOK_ID);

    assertThat(stock).isEqualTo(6);
  }

  @Test
  void isAvailable_shouldReturnFalse_whenStockIsZero() {
    given(bookRepository.findById(BOOK_ID)).willReturn(Optional.of(new Book()));
    given(arrivalMovementRepository.findByBookId(BOOK_ID)).willReturn(List.of());
    given(saleMovementRepository.findByBookId(BOOK_ID)).willReturn(List.of());
    given(cancelledMovementRepository.findByBookId(BOOK_ID)).willReturn(List.of());
    given(reservationMovementRepository.findByBookId(BOOK_ID)).willReturn(List.of());

    assertThat(stockMovementService.isAvailable(BOOK_ID)).isFalse();
  }
}
