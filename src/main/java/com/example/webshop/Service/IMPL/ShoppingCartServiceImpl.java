package com.example.webshop.Service.IMPL;

import com.example.webshop.Model.Enum.CartStatus;
import com.example.webshop.Model.Exception.*;
import com.example.webshop.Model.Product;
import com.example.webshop.Model.ShoppingCart;
import com.example.webshop.Model.Transaction;
import com.example.webshop.Model.User;
import com.example.webshop.Model.dto.ChargeRequest;
import com.example.webshop.Persistance.ShoppingCartRepository;
import com.example.webshop.Service.*;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final UserService userService;
    private final ProductService productService;
    private PaymentService paymentService;
    private final TransactionService transactionService;

    public ShoppingCartServiceImpl(ShoppingCartRepository shoppingCartRepository, UserService userService, ProductService productService, PaymentService paymentService, TransactionService transactionService) {
        this.shoppingCartRepository = shoppingCartRepository;
        this.userService = userService;
        this.productService = productService;
        this.paymentService = paymentService;
        this.transactionService = transactionService;
    }

    @Override
    public List<Product> listItems(String username) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        return shoppingCart.getProducts();
    }

    @Override
    public ShoppingCart createShoppingCart(String username) {
        User user = userService.findById(username);
        if (this.shoppingCartRepository.existsByUserUsernameAndStatus(username, CartStatus.CREATED)) {
            throw new ActiveShoppingCartAlreadyExists();
        }
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        this.shoppingCartRepository.save(shoppingCart);
        return shoppingCart;
    }

    @Override
    public ShoppingCart addBookToShoppingCart(String username, Long productId) {

        ShoppingCart shoppingCart = getActiveShoppingCartOrCreateNew(username);
        Product product = this.productService.findById(productId);
        for (Product p : shoppingCart.getProducts()) {
            if (p.getId().equals(productId)) {
                throw new ProductAlreadyInCartException(productId);
            }
        }

        shoppingCart.getProducts().add(product);

        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart removeProductFromShoppingCart(String username, Long productId) {
        ShoppingCart shoppingCart = getActiveShoppingCartOrCreateNew(username);

        shoppingCart.setProducts(
                shoppingCart.getProducts()
                        .stream()
                        .filter(item -> !item.getId().equals(productId))
                        .collect(Collectors.toList())
        );
        if (shoppingCart.getProducts().isEmpty()) {
            shoppingCart.setStatus(CartStatus.CANCELED);
        }
        return this.shoppingCartRepository.save(shoppingCart);
    }

    //helper
    private ShoppingCart getActiveShoppingCartOrCreateNew(String username) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        if (shoppingCart == null) {
            shoppingCart = new ShoppingCart();
            shoppingCart.setUser(userService.findById(username));
            shoppingCart = this.shoppingCartRepository.save(shoppingCart);
        }
        return shoppingCart;
    }

    @Override
    public ShoppingCart cancelShoppingCart(String username) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        if (shoppingCart == null) {
            throw new ShoppingCartNotActiveException(username);
        }
        shoppingCart.setStatus(CartStatus.CANCELED);
        return this.shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCart checkoutShoppingCart(String username, ChargeRequest chargeRequest) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserUsernameAndStatus(username, CartStatus.CREATED);
        if (shoppingCart == null) {
            throw new ShoppingCartNotActiveException(username);
        }
        List<Product> products = shoppingCart.getProducts();
        float price = 0;
        for (Product p : products) {
            if (p.getQuantity() <= 0) {
                shoppingCart.getProducts().remove(p);
                throw new ProductOutOfStockException(p.getId());
            }
            p.setQuantity(p.getQuantity() - 1);
            price += p.getPrice();
        }
        Charge charge = null;
        try {
            charge = this.paymentService.pay(chargeRequest);
        } catch (CardException | APIException | AuthenticationException | InvalidRequestException | APIConnectionException e) {
            throw new TransactionFailedException(username, e.getMessage());
        }

        Transaction t = new Transaction();
        t.setSenderUser(userService.findById(username));
        t.setId(charge.getId());
        t.setObject(charge.getObject());
        t.setAmount(charge.getAmount());
        t.setAmountRefunded(charge.getAmountRefunded());
        t.setApplication(charge.getApplication());
        t.setApplicationFee(charge.getApplicationFee());
        t.setCaptured(charge.getCaptured());
        t.setCreated(charge.getCreated());
        t.setCurrency(charge.getCurrency());
        t.setDescription(charge.getDescription());
        t.setPaid(charge.getPaid());
        t.setRefunded(charge.getRefunded());
        t.setStatus(charge.getStatus());
        t.setDisputed(charge.getDispute());

        this.transactionService.save(t);
        shoppingCart.setProducts(products);
        shoppingCart.setStatus(CartStatus.FINISHED);
        return shoppingCartRepository.save(shoppingCart);
    }

    @Override
    public ShoppingCart findActiveShoppingCartByUsername(String userId) {
        ShoppingCart shoppingCart = this.shoppingCartRepository.findByUserUsernameAndStatus(userId, CartStatus.CREATED);
        if (shoppingCart == null)
            throw new ShoppingCartNotActiveException(userId);
        return this.shoppingCartRepository.findByUserUsernameAndStatus(userId, CartStatus.CREATED);
    }
}
