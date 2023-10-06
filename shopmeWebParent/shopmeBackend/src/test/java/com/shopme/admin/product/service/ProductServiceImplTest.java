package com.shopme.admin.product.service;

import com.shopme.admin.product.repository.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
class ProductServiceImplTest {

    @Autowired
    private ProductRepository repository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 6);
        Category category = entityManager.find(Category.class, 10);

        Product product = new Product();
        product.setName("Canon Mark 2");
        product.setAlias("canon_mark_2");
        product.setShortDescription("Canon Mark 2 short description");
        product.setFullDescription("Canon Mark 2, full description");

        product.setBrand(brand);
        product.setCategory(category);

        product.setPrice(900);
        product.setCost(600);
        product.setEnabled(true);
        product.setInStock(true);
        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        Product savedProduct = repository.save(product);

        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    void test_listAll() {
        List<Product> productList = repository.findAll();

        productList.forEach(System.out::println);
    }

    @Test
    void listAllByPage() {
    }

    @Test
    void get() {
        Long id = 2L;

        Product product = repository.findById(id).get();
        System.out.println(product);

        assertThat(product).isNotNull();
    }

    @Test
    void update() {
        Long id = 2L;

        Product product = repository.findById(id).get();
        product.setPrice(1000);

        repository.save(product);

        Product updatedProduct = entityManager.find(Product.class, id);

        assertThat(updatedProduct.getPrice()).isEqualTo(1000);
    }

    @Test
    void save() {
    }

    @Test
    void deleteById() {
        Long id = 3L;

        repository.deleteById(id);

        Optional<Product> result = repository.findById(id);

        assertThat(!result.isPresent());
    }

    @Test
    void saveProductWithIMages() {
        Long id = 1L;
        Product product = repository.findById(id).get();

        product.setMainImage("main_image.jpg");
        product.addExtraImage("extra_image_1.png");
        product.addExtraImage("extra_image_2.png");
        product.addExtraImage("extra_image_2.png");

        Product savedProduct = repository.save(product);

        assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }

    @Test
    void checkUnique() {
    }
}