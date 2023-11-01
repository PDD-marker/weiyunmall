package org.example.controller;

@RestController
@RequestMapping("/api/products")
public class FlashSaleController {
    @Autowired
    private PurchaseService purchaseService;

    @GetMapping("/{productId}")
    public Product getProduct(@PathVariable Long productId) {
        return purchaseService.getProduct(productId);
    }

    @PostMapping("/{productId}/purchase")
    public boolean placeOrder(@PathVariable Long productId, @RequestBody Order order) {
        return purchaseService.placeOrder(order);
    }
}