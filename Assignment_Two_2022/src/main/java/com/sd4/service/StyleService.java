/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.service;

import com.sd4.model.Style;
import com.sd4.repository.StyleRepository;
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
public class StyleService {
    @Autowired
    private StyleRepository StyleRepo;
    
    public Optional<Style> findOne(Integer id) {
        return StyleRepo.findById(id);
    }
    
    public List<Style> findAll() {
        return (List<Style>) StyleRepo.findAll();
    }
    
    public long count(){
        return StyleRepo.count();
    }
    
    public void deleteByID(long styleID){
        StyleRepo.deleteById(styleID);
    }
    
    public void saveAuthor(Style a){
        StyleRepo.save(a);
    }
    
     public Page<Style> findPaginated(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Style> pagedResult = StyleRepo.findAll(paging);
        return pagedResult;
    }
}
