package org.example.entity;
@Service
public class FlashSale {

    @Autowired
    private ProductRepository productRepository;

    @Scheduled(fixedRate = 1000)  // 每秒检查一次库存  
    public void checkStock() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            if (product.getStock() > 0) {
                // 如果有库存，进行抢购操作  
                int availableStock = product.getStock();
                if (orderService.placeOrder(new Order(product, availableStock))) {
                    // 更新库存  
                    product.setStock(0);
                    productRepository.save(product);
                    System.out.println("User " + Thread.currentThread().getName() + " successfully placed an order for " + product.getName());
                } else {
                    System.out.println("Failed to place order for " + product.getName());
                }
            }
        }
    }
}