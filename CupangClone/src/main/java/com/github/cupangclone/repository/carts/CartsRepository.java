package com.github.cupangclone.repository.carts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartsRepository extends JpaRepository<Carts, Long>, PagingAndSortingRepository<Carts, Long> {
    Optional<Carts> findByUserPrincipal_UserPrincipalIdAndItems_itemIdAndItemOption_ItemOptionId(Long userPrincipalId, Long itemId, Long itemOptionId);

    Optional<Carts> findByCartIdAndUserPrincipal_UserPrincipalId(Long cartId, Long userPrincipalId);

    List<Carts> findByUserPrincipal_UserPrincipalId(Long userPrincipalId);
}
