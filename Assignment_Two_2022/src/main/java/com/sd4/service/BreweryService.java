/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.service;

import com.sd4.model.Brewery;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import com.sd4.repository.BreweryRepository;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
/**
 *
 * @author venom
 */
@Service
public class BreweryService {
    
    @Autowired
    private BreweryRepository breweryRepo;
    
    public Optional<Brewery> findOne(Long id) {
        return breweryRepo.findById(id);
    }
    
    public List<Brewery> findAll() {
        return (List<Brewery>) breweryRepo.findAll();
    }
    
    public long count(){
        return breweryRepo.count();
    }
    
    public void deleteByID(long beerID){
        breweryRepo.deleteById(beerID);
    }
    
    public void saveAuthor(Brewery a){
        breweryRepo.save(a);
    }
    
     public Page<Brewery> findPaginated(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Page<Brewery> pagedResult = breweryRepo.findAll(paging);
        return pagedResult;
    }
}
