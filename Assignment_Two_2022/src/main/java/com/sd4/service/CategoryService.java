/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.service;

import com.sd4.model.Brewery;
import com.sd4.model.Category;
import com.sd4.repository.BreweryRepository;
import com.sd4.repository.CategoryRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

/**
 *
 * @author venom
 */

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository CategoryRepo;
    
    public Optional<Category> findOne(Long id) {
        return CategoryRepo.findById(id);
    }
    
    public List<Category> findAll() {
        return (List<Category>) CategoryRepo.findAll();
    }
    
    public long count(){
        return CategoryRepo.count();
    }
    
    public void deleteByID(long categoryID){
        CategoryRepo.deleteById(categoryID);
    }
    
    public void saveAuthor(Category a){
        CategoryRepo.save(a);
    }
    
     public Page<Category> findPaginated(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Category> pagedResult = CategoryRepo.findAll(paging);
        return pagedResult;
    }
     
}
