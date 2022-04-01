/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sd4.repository;

import com.sd4.model.Style;
import java.util.Optional;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 *
 * @author venom
 */
public interface StyleRepository extends PagingAndSortingRepository<Style, Long> {

    public Optional<Style> findById(Integer id);
    
}
