package com.github.cupangclone.repository.itemOption;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemOptionRepository extends JpaRepository<ItemOption, Long> {
}
