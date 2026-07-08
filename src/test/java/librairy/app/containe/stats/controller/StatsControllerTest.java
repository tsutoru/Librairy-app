package librairy.app.containe.stats.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Map;
import librairy.app.containe.stats.service.StatsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(StatsController.class)
class StatsControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private StatsService statsService;

  @Test
  void getRevenueByGenre_shouldReturnMap() throws Exception {
    given(statsService.getRevenueByGenre()).willReturn(Map.of("Fiction", 150.0, "Tech", 300.0));

    mockMvc
        .perform(get("/stats/revenue/by-genre"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.Fiction").value(150.0))
        .andExpect(jsonPath("$.Tech").value(300.0));
  }

  @Test
  void getRevenueByGenreDetailed_shouldReturnDetailedStats() throws Exception {
    Map<String, Object> techStats = Map.of("revenue", 300.0, "percentage", 66.67, "totalSales", 5L);
    given(statsService.getRevenueByGenreDetailed()).willReturn(Map.of("Tech", techStats));

    mockMvc
        .perform(get("/stats/revenue/by-genre/detailed"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.Tech.revenue").value(300.0))
        .andExpect(jsonPath("$.Tech.totalSales").value(5));
  }
}
