package ru.gb.thymeleafshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.gb.gbapi.product.api.ProductGateway;
import ru.gb.gbapi.product.dto.ProductDto;


@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    private final ProductGateway productGateway;

    @GetMapping("/all")
    public String getProductList(Model model) {
        model.addAttribute("products", productGateway.getProductList());
        return "product-list";
    }

    @GetMapping("/{productId}")
    public String info(Model model, @PathVariable(name = "productId") Long id) {
        ProductDto productDto;
        if (id != null) {
            productDto = productGateway.getProduct(id).getBody();
        } else {
            return "redirect:/product/all";
        }
        model.addAttribute("product", productDto);
        return "product-form";
    }

    @GetMapping
    public String showForm(Model model, @RequestParam(name = "id", required = false) Long id) {
        ProductDto productDto;

        if (id != null) {
            productDto = productGateway.getProduct(id).getBody();
        } else {
            productDto = new ProductDto();
        }
        model.addAttribute("product", productDto);
        return "product-form";
    }

    @PostMapping
    public String saveProduct(ProductDto product) {
        productGateway.handlePost(product);
        return "redirect:/product/all";
    }

    @GetMapping("/delete")
    public String deleteById(@RequestParam(name = "id") Long id) {
        productGateway.deleteById(id);
        return "redirect:/product/all";
    }

}
