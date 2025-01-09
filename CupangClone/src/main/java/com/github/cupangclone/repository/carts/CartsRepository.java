package com.github.cupangclone.repository.carts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartsRepository extends JpaRepository<Carts, Long> {
    Optional<Carts> findByUserPrincipal_UserPrincipalIdAndItems_itemIdAndItemOption_ItemOptionId(Long userPrincipalId, Long itemId, Long itemOptionId);
}
