/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;

import com.sd4.helperclass.ShortBeerAndBrewery;
import com.sd4.model.Beer;
import com.sd4.model.Brewery;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.sd4.helperclass.Iframe;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.core.scheme.VCard;
import net.glxn.qrgen.javase.QRCode;
import org.apache.commons.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 *
 * @author venom
 */
@RequestMapping("brewery")
@RestController
public class BreweryController {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private BeerService beerService;

    @Autowired
    private BreweryService breweryService;

    @GetMapping(value = "/map/{id}", produces = MediaType.TEXT_HTML_VALUE)
    public ResponseEntity<String> findMap(@PathVariable Long id) {
        Optional<Brewery> brewery = breweryService.findOne(id);

        String Iframe1 = "<iframe width=`600` height=`450`style=`border:0` loading=`lazy` allowfullscreen src=`https://www.google.com/maps/embed/v1/place?key=AIzaSyCDCJHZvpb8c24tlq78239GInpcyfw5NgQ &q=";
        String Iframe2 = "`></iframe>";
        //Inefficient Practice
        String Iframe1Parsed = Iframe1.replace('`', '"');
        String Iframe2Parsed = Iframe2.replace('`', '"');
        //
        if (brewery.get().getAddress1().isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        String breweryAddress = brewery.get().getAddress1();

        breweryAddress = breweryAddress.replace(' ', '+');
        String finalParsed = Iframe1Parsed + breweryAddress + Iframe2Parsed;
        return ResponseEntity.ok(finalParsed);
    }
    
    @GetMapping(value = "/findQR/{id}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> findQR(@PathVariable Long id){
        Optional<Brewery> brewery = breweryService.findOne(id);
        
        //This Functionality could be seperated into a QRCode Service (For Seperation of Concern)
        if(brewery.isEmpty()){
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Brewery brew = brewery.get();
        VCard newCard = new VCard(brew.getName()).setAddress(brew.getAddress1()).setPhoneNumber(brew.getPhone()).setEmail(brew.getEmail()).setWebsite(brew.getWebsite());
        ByteArrayOutputStream bos = QRCode.from(newCard).withSize(200, 200).stream();
        byte[] qrImage  = bos.toByteArray();
        //---------------------------------------------------------------
        
        return ResponseEntity.ok(qrImage);
    } 
}
