package librairy.app.containe.stats.controller;

import java.util.Map;
import librairy.app.containe.stats.service.StatsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/stats")
public class StatsController {

    private final StatsService statsService;

    public StatsController(StatsService statsService) {
        this.statsService = statsService;
    }

    @GetMapping("/revenue/by-genre")
    public ResponseEntity<Map<String, Double>> getRevenueByGenre() {
        return ResponseEntity.ok(statsService.getRevenueByGenre());
    }

    @GetMapping("/revenue/by-genre/detailed")
    public ResponseEntity<Map<String, Object>> getRevenueByGenreDetailed() {
        return ResponseEntity.ok(statsService.getRevenueByGenreDetailed());
    }
}