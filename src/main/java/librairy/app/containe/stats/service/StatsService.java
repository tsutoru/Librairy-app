package librairy.app.containe.stats.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import librairy.app.containe.category.entity.Category;
import librairy.app.containe.category.repository.CategoryRepository;
import librairy.app.containe.stockMovement.sale.entity.SaleMovement;
import librairy.app.containe.stockMovement.sale.repository.SaleMovementRepository;
import org.springframework.stereotype.Service;

@Service
public class StatsService {

  private final SaleMovementRepository saleMovementRepository;
  private final CategoryRepository categoryRepository;

  public StatsService(
      SaleMovementRepository saleMovementRepository, CategoryRepository categoryRepository) {
    this.saleMovementRepository = saleMovementRepository;
    this.categoryRepository = categoryRepository;
  }

  public Map<String, Double> getRevenueByGenre() {
    List<Category> categories = categoryRepository.findAll();
    List<SaleMovement> allSales = saleMovementRepository.findAll();

    Map<String, Double> revenueByGenre = new HashMap<>();

    categories.forEach(
        category -> {
          double revenue =
              allSales.stream()
                  .filter(
                      sale ->
                          sale.getBook().getCategory() != null
                              && sale.getBook().getCategory().getId().equals(category.getId()))
                  .mapToDouble(SaleMovement::getTotalAmount)
                  .sum();

          revenueByGenre.put(category.getName(), revenue);
        });

    return revenueByGenre;
  }

  public Map<String, Object> getRevenueByGenreDetailed() {
    List<Category> categories = categoryRepository.findAll();
    List<SaleMovement> allSales = saleMovementRepository.findAll();

    double totalRevenue = allSales.stream().mapToDouble(SaleMovement::getTotalAmount).sum();

    Map<String, Object> result = new HashMap<>();

    categories.forEach(
        category -> {
          double revenue =
              allSales.stream()
                  .filter(
                      sale ->
                          sale.getBook().getCategory() != null
                              && sale.getBook().getCategory().getId().equals(category.getId()))
                  .mapToDouble(SaleMovement::getTotalAmount)
                  .sum();

          long totalSales =
              allSales.stream()
                  .filter(
                      sale ->
                          sale.getBook().getCategory() != null
                              && sale.getBook().getCategory().getId().equals(category.getId()))
                  .count();

          double percentage = totalRevenue > 0 ? (revenue / totalRevenue) * 100 : 0;

          Map<String, Object> categoryStats = new HashMap<>();
          categoryStats.put("revenue", revenue);
          categoryStats.put("percentage", Math.round(percentage * 100.0) / 100.0);
          categoryStats.put("totalSales", totalSales);

          result.put(category.getName(), categoryStats);
        });

    return result;
  }
}
