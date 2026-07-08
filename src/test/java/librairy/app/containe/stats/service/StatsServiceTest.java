package librairy.app.containe.stats.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Map;
import librairy.app.containe.book.entity.Book;
import librairy.app.containe.category.entity.Category;
import librairy.app.containe.category.repository.CategoryRepository;
import librairy.app.containe.stockMovement.sale.entity.SaleMovement;
import librairy.app.containe.stockMovement.sale.repository.SaleMovementRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class StatsServiceTest {

  @Mock private SaleMovementRepository saleMovementRepository;
  @Mock private CategoryRepository categoryRepository;

  @InjectMocks private StatsService statsService;

  @Test
  void getRevenueByGenre_shouldSumRevenuePerCategory() {
    Category fiction = mock(Category.class);
    given(fiction.getId()).willReturn("cat-1");
    given(fiction.getName()).willReturn("Fiction");

    Book book = mock(Book.class);
    given(book.getCategory()).willReturn(fiction);

    SaleMovement sale1 = mock(SaleMovement.class);
    given(sale1.getBook()).willReturn(book);
    given(sale1.getTotalAmount()).willReturn(50.0);

    SaleMovement sale2 = mock(SaleMovement.class);
    given(sale2.getBook()).willReturn(book);
    given(sale2.getTotalAmount()).willReturn(100.0);

    given(categoryRepository.findAll()).willReturn(List.of(fiction));
    given(saleMovementRepository.findAll()).willReturn(List.of(sale1, sale2));

    Map<String, Double> result = statsService.getRevenueByGenre();

    assertThat(result).containsEntry("Fiction", 150.0);
  }

  @Test
  void getRevenueByGenre_shouldReturnZero_whenNoSalesForCategory() {
    Category tech = mock(Category.class);
    given(tech.getName()).willReturn("Tech");

    given(categoryRepository.findAll()).willReturn(List.of(tech));
    given(saleMovementRepository.findAll()).willReturn(List.of());

    Map<String, Double> result = statsService.getRevenueByGenre();

    assertThat(result).containsEntry("Tech", 0.0);
  }

  @Test
  void getRevenueByGenreDetailed_shouldComputePercentageAndCount() {
    Category fiction = mock(Category.class);
    given(fiction.getId()).willReturn("cat-1");
    given(fiction.getName()).willReturn("Fiction");

    Book book = mock(Book.class);
    given(book.getCategory()).willReturn(fiction);

    SaleMovement sale = mock(SaleMovement.class);
    given(sale.getBook()).willReturn(book);
    given(sale.getTotalAmount()).willReturn(100.0);

    given(categoryRepository.findAll()).willReturn(List.of(fiction));
    given(saleMovementRepository.findAll()).willReturn(List.of(sale));

    Map<String, Object> result = statsService.getRevenueByGenreDetailed();

    @SuppressWarnings("unchecked")
    Map<String, Object> fictionStats = (Map<String, Object>) result.get("Fiction");

    assertThat(fictionStats.get("revenue")).isEqualTo(100.0);
    assertThat(fictionStats.get("percentage")).isEqualTo(100.0);
    assertThat(fictionStats.get("totalSales")).isEqualTo(1L);
  }
}
