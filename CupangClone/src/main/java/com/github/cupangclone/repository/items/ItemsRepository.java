package com.github.cupangclone.repository.items;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemsRepository extends JpaRepository<Items, Long>, PagingAndSortingRepository<Items, Long> {

    @Query("SELECT i FROM Items i WHERE i.userPrincipal.userPrincipalId = :userPrincipalId")
    List<Items> findByUserPrincipalId(@Param("userPrincipalId") Long userPrincipalId);

    Page<Items> findAllBy(Pageable pageable);

}
