package com.villainscode.controller;

import com.villainscode.entity.Product;
import com.villainscode.service.ProductService;
import java.util.List;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/shop")
public class ProductController {

    private final ProductService productMaintenanceService;

    public ProductController(ProductService productMaintenanceService) {
        this.productMaintenanceService = productMaintenanceService;
    }

    @GetMapping("/list")
    public String displayList(Model model) {
        List<Product> products = productMaintenanceService.findAll();
        model.addAttribute("productList", products);

        return "shop/productList";
    }

    @GetMapping("/delete")
    public void delete(@RequestParam("productId") int productId) {
        productMaintenanceService.delete(productId);
    }
}
