/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;

import com.sd4.helperclass.ShortBeerAndBrewery;
import com.sd4.model.Beer;
import com.sd4.model.Brewery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import java.util.Optional;
import javax.servlet.http.HttpServletResponse;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.util.UriComponentsBuilder;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author venom
 */
@RequestMapping("Beer")
@RestController
public class BeerController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BeerService beerService;

    @Autowired
    private BreweryService breweryService;

    Pageable firstPageWithTwoElements = PageRequest.of(0, 2);

    Pageable secondPageWithFiveElements = PageRequest.of(1, 5);

    @GetMapping(value = "/findBeer/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<Beer> findOne(@PathVariable Long id) {
        Optional<Beer> beer = beerService.findOne(id);
        if (!beer.isPresent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            Link relLink = linkTo(methodOn(BeerController.class).findAll()).withRel("allBeers");
            beer.get().add(relLink);
            return ResponseEntity.ok(beer.get());
        }
    }

    @GetMapping(value = "/allBeers", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<List<Beer>> findAll() {
        List<Beer> allBeers = beerService.findAll();

        if (allBeers.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            for (final Beer b : allBeers) {
                //Add Self Link For Beer
                long id = b.getId();
                Link selfLink = linkTo(methodOn(BeerController.class).findOne(id)).withSelfRel();
                b.add(selfLink);
                //Add Short Brewery and Beer info Link
                Link relLink = linkTo(methodOn(BeerController.class).findShortBeerAndBrewery(id)).withRel("shortBeerBrewery");
                b.add(relLink);
            }
            return ResponseEntity.ok(allBeers);
        }
    }

    @GetMapping(value = "/shortBeerBrewery/{id}", produces = MediaTypes.HAL_JSON_VALUE)
    public ResponseEntity<ShortBeerAndBrewery> findShortBeerAndBrewery(@PathVariable Long id) {
        //Get Beer ID
        Optional<Beer> beer = beerService.findOne(id);
        long beerID = beer.get().getId();
        Optional<Brewery> brewery = breweryService.findOne(beerID);

        if (beer.isEmpty() || brewery.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            String beerName = beer.get().getName();
            String breweryName = brewery.get().getName();
            String beerDesc = beer.get().getDescription();
            ShortBeerAndBrewery SBAB = new ShortBeerAndBrewery(beerName, beerDesc, breweryName);
            return ResponseEntity.ok(SBAB);
        }
    }
    

    @GetMapping("/beers")
    public ResponseEntity<List<Beer>> findPaginated(@RequestParam("page") int page, @RequestParam("size") int size) {
        Page<Beer> resultPage = (Page<Beer>) beerService.findPaginated(page, size);
        if (!resultPage.hasContent()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(resultPage.getContent());
        }
    }
    
    @DeleteMapping(value = "removeBeer/{id}")
    public ResponseEntity removeBeer(@PathVariable Long id){
         beerService.deleteByID(id);
         return new ResponseEntity(HttpStatus.OK);
        
    }
    
    @PostMapping(value="insertBeer", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity add(@RequestBody Beer b){
       beerService.saveAuthor(b);
        return new ResponseEntity(HttpStatus.CREATED);
    }
    
}

