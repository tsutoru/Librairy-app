package librairy.app.containe.stockMovement.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Map;
import librairy.app.containe.stockMovement.service.StockMovementService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StockMovementController.class)
class StockMovementControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private StockMovementService stockMovementService;

  @Test
  void getStock_shouldReturnQuantity() throws Exception {
    String bookId = "boo-1111-1111-1111-111111111111";
    given(stockMovementService.getStockByBookId(bookId)).willReturn(12);

    mockMvc
        .perform(get("/books/{bookId}/stock", bookId))
        .andExpect(status().isOk())
        .andExpect(content().string("12"));
  }

  @Test
  void isAvailable_shouldReturnTrue_whenStockPositive() throws Exception {
    String bookId = "boo-1111-1111-1111-111111111111";
    given(stockMovementService.isAvailable(bookId)).willReturn(true);

    mockMvc
        .perform(get("/books/{bookId}/stock/available", bookId))
        .andExpect(status().isOk())
        .andExpect(content().string("true"));
  }

  @Test
  void isAvailable_shouldReturnFalse_whenStockZero() throws Exception {
    String bookId = "boo-2222-2222-2222-222222222222";
    given(stockMovementService.isAvailable(bookId)).willReturn(false);

    mockMvc
        .perform(get("/books/{bookId}/stock/available", bookId))
        .andExpect(status().isOk())
        .andExpect(content().string("false"));
  }

  @Test
  void getLowStockBooks_shouldReturnListWithDefaultThreshold() throws Exception {
    List<Map<String, Object>> lowStock =
        List.of(Map.of("bookId", "boo-3333-3333-3333-333333333333", "stock", 2));
    given(stockMovementService.getLowStockBooks(3)).willReturn(lowStock);

    mockMvc
        .perform(get("/books/low-stock"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].bookId").value("boo-3333-3333-3333-333333333333"))
        .andExpect(jsonPath("$[0].stock").value(2));
  }

  @Test
  void getLowStockBooks_shouldUseCustomThreshold() throws Exception {
    given(stockMovementService.getLowStockBooks(5)).willReturn(List.of());

    mockMvc
        .perform(get("/books/low-stock").param("threshold", "5"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$").isArray())
        .andExpect(jsonPath("$").isEmpty());
  }
}
