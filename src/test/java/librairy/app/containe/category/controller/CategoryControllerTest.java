package librairy.app.containe.category.controller;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import librairy.app.containe.category.entity.Category;
import librairy.app.containe.category.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockBean private CategoryService categoryService;

  @Autowired private ObjectMapper objectMapper;

  private Category category;

  @BeforeEach
  void setUp() {
    category = new Category();
    category.setId("1");
    category.setName("Fiction");
  }

  @Test
  void create_shouldReturnCreatedCategory() throws Exception {
    when(categoryService.create(any(Category.class))).thenReturn(category);

    mockMvc
        .perform(
            post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.name").value("Fiction"));

    verify(categoryService, times(1)).create(any(Category.class));
  }

  @Test
  void getAll_shouldReturnListOfCategories() throws Exception {
    when(categoryService.getAll()).thenReturn(List.of(category));

    mockMvc
        .perform(get("/categories"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$", hasSize(1)))
        .andExpect(jsonPath("$[0].id").value("1"));

    verify(categoryService, times(1)).getAll();
  }

  @Test
  void getById_shouldReturnCategory() throws Exception {
    when(categoryService.getById("1")).thenReturn(category);

    mockMvc
        .perform(get("/categories/{id}", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("1"))
        .andExpect(jsonPath("$.name").value("Fiction"));

    verify(categoryService, times(1)).getById("1");
  }

  @Test
  void update_shouldReturnUpdatedCategory() throws Exception {
    Category updated = new Category();
    updated.setId("1");
    updated.setName("Non-Fiction");

    when(categoryService.update(eq("1"), any(Category.class))).thenReturn(updated);

    mockMvc
        .perform(
            put("/categories/{id}", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updated)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.name").value("Non-Fiction"));

    verify(categoryService, times(1)).update(eq("1"), any(Category.class));
  }

  @Test
  void delete_shouldReturnNoContent() throws Exception {
    doNothing().when(categoryService).delete("1");

    mockMvc.perform(delete("/categories/{id}", "1")).andExpect(status().isNoContent());

    verify(categoryService, times(1)).delete("1");
  }
}
