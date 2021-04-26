package ru.clevertec.checksystem.webapi.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.clevertec.checksystem.core.dto.ProductDto;
import ru.clevertec.checksystem.core.service.common.IProductService;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ProductController {

    private final IProductService productService;

    @GetMapping
    ResponseEntity<Page<ProductDto>> get(Pageable pageable) {
        return new ResponseEntity<>(productService.getProductsPage(pageable), HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    ResponseEntity<ProductDto> get(@PathVariable Long id) {
        return new ResponseEntity<>(productService.getProductById(id), HttpStatus.OK);
    }

    @GetMapping("/count")
    ResponseEntity<Long> getCount() {
        return new ResponseEntity<>(productService.getProductCount(), HttpStatus.OK);
    }

    @DeleteMapping(value = "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id) {
        productService.deleteProductById(id);
    }

    @PutMapping
    ResponseEntity<ProductDto> update(@RequestBody @Valid ProductDto productDto) {
        return new ResponseEntity<>(productService.updateProduct(productDto), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<ProductDto> create(@RequestBody @Valid ProductDto productDto) {
        return new ResponseEntity<>(productService.createProduct(productDto), HttpStatus.CREATED);
    }
}
