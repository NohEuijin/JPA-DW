package com.example.dw.repository.goods;

import com.example.dw.domain.entity.goods.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long>{

    Cart findCartIdByUsersId(Long userId);
}
