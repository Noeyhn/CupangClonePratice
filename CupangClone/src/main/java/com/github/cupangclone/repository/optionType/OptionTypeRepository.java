package com.github.cupangclone.repository.optionType;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionTypeRepository extends JpaRepository<OptionType, Long> {
    OptionType findByOptionTypeName(String optionTypeName);
}
