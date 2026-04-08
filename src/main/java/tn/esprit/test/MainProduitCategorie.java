package org.example.test;

import org.example.entities.Category;
import org.example.entities.Product;
import org.example.services.CategoryService;
import org.example.services.ProductService;
import org.example.utils.MyDatabase;

public class MainProduitCategorie {
    public static void main(String[] args) {
        MyDatabase.getInstance();

        CategoryService categoryService = new CategoryService();
        ProductService productService = new ProductService();

        System.out.println("\nCATEGORIES");
        categoryService.addCategory(new Category("laptop"));
        categoryService.getAllCategories().forEach(System.out::println);

        if (!categoryService.getAllCategories().isEmpty()) {
            Category firstCategory = categoryService.getAllCategories().get(0);
            firstCategory.setName("keyboard updated");
            categoryService.updateCategory(firstCategory);
            System.out.println(categoryService.getCategoryById(firstCategory.getId()));
        }

        System.out.println("\nPRODUITS");
        productService.addProduct(new Product("Laptop Pro", "16GB RAM", 999.99, 10, "laptop.png", 1));
        productService.getAllProducts().forEach(System.out::println);

        if (!productService.getAllProducts().isEmpty()) {
            Product firstProduct = productService.getAllProducts().get(0);
            firstProduct.setName("Laptop Pro Max");
            firstProduct.setDescription("32GB RAM");
            firstProduct.setPrice(1299.99);
            firstProduct.setStock(5);
            firstProduct.setImage("laptop2.png");
            productService.updateProduct(firstProduct);
            System.out.println(productService.getProductById(firstProduct.getId()));
        }
    }
}