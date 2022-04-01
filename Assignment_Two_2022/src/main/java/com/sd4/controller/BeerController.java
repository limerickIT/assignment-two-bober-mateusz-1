/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.controller;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfDocument;
import com.itextpdf.text.pdf.PdfWriter;
import com.sd4.helperclass.ShortBeerAndBrewery;
import com.sd4.model.Beer;
import com.sd4.model.Brewery;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.sd4.service.BeerService;
import com.sd4.service.BreweryService;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.hibernate.cfg.Environment;
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
import org.zeroturnaround.zip.ZipUtil;
import com.sd4.helperclass.PDFhelper;
import javax.xml.transform.stream.StreamResult;
import com.itextpdf.io.image.ImageDataFactory;


import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.sd4.model.Category;
import com.sd4.model.Style;
import com.sd4.service.CategoryService;
import com.sd4.service.StyleService;
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
    
    @Autowired
    private StyleService styleService;
    
    @Autowired
    private CategoryService categoryService;

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

    @GetMapping(value = "/findImage/{id}/{size}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> findImage(@PathVariable Long id, @PathVariable String size) throws IOException {
        Optional<Beer> beer = beerService.findOne(id);

        if (beer.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

        Beer beerObject = beer.get();
        String beerImage = beerObject.getImage();
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("static/assets/images/" + size + "/" + beerImage);

        return ResponseEntity.ok(IOUtils.toByteArray(in));
    }

    @GetMapping(value = "/findPdf/{id}", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> findPDF(@PathVariable Long id) throws FileNotFoundException, DocumentException, IOException, InterruptedException {

        String currDir = System.getProperty("user.dir");

        Optional<Beer> beer = beerService.findOne(id);
        if (beer.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        Beer beerObject = beer.get();
        Long breweryID = beerObject.getBrewery_id();
        Optional<Brewery> brewery = breweryService.findOne(breweryID);
        if (brewery.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        
        String beerImage = beerObject.getImage();
        
        Integer categoryID =  beerObject.getCat_id();
        String categoryIDstr = categoryID.toString();
        Long categoryIDLong = Long.parseLong(categoryIDstr);
        
        Integer styleID = beerObject.getStyle_id();

        
        Optional<Category> category = categoryService.findOne(categoryIDLong);
        
        Category categoryObject = category.get();
        
        Optional<Style> style = styleService.findOne(styleID);
        
        Style styleObject = style.get();
     
        Brewery breweryObject = brewery.get();
        String imagePath = currDir + "/src/main/resources/static/assets/images/thumbs/" + beerImage;

        Image img = Image.getInstance(imagePath);
        
        Document document = new Document();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        Font font = FontFactory.getFont(FontFactory.TIMES_ROMAN, 14, BaseColor.BLACK);

        Chunk chunk = new Chunk("Name : " + beerObject.getName(), font);

        Chunk ABVChunk = new Chunk("Beer ABV : " + beerObject.getAbv().toString(), font);

        Chunk descriptionChunk = new Chunk("Description : " + beerObject.getDescription(), font);

        Chunk sellPriceChunk = new Chunk("Sell Price : " + beerObject.getBuy_price().toString(), font);

        Chunk breweryNameChunk = new Chunk("Brewery name : " + breweryObject.getName());
        
        Chunk breweryWebsiteChunk = new Chunk("Website : " + breweryObject.getWebsite());
        
        Chunk beerCategoryChunk = new Chunk("Category : " + categoryObject.getCat_name());
        
        Chunk styleChunk = new Chunk("Beer Style : " + styleObject.getStyle_name());
        
        Paragraph data = new Paragraph();
        
        data.add(chunk);
        PDFhelper.addEmptyLine(data, 1);
        data.add(ABVChunk);
        PDFhelper.addEmptyLine(data, 1);
        data.add(descriptionChunk);
        PDFhelper.addEmptyLine(data, 1);
        data.add(sellPriceChunk);
        PDFhelper.addEmptyLine(data, 1);
        data.add(breweryNameChunk);
        PDFhelper.addEmptyLine(data, 1);
        data.add(breweryWebsiteChunk);
        PDFhelper.addEmptyLine(data, 1);
        data.add(beerCategoryChunk);
        PDFhelper.addEmptyLine(data, 1);
        data.add(styleChunk);
        PDFhelper.addEmptyLine(data, 1);
        
        PdfWriter.getInstance(document, out);
        document.open();
        document.add(img);
        document.add(data);
        document.close();
        
        return ResponseEntity.ok(out.toByteArray());

    }

    @GetMapping(value = "/allImages", produces = "application/zip")
    public ResponseEntity<byte[]> findImages() throws IOException {

        File imageDirectory = new File(System.getProperty("user.dir"), "/src/main/resources/static/assets/images");

        File targetDirectory = new File(System.getProperty("user.dir"), "/src/main/resources/static/assets/tmp/images.zip");

        ZipUtil.pack(imageDirectory, targetDirectory);

        InputStream in = this.getClass().getClassLoader().getResourceAsStream("static/assets/tmp/images.zip");

        return ResponseEntity.ok(IOUtils.toByteArray(in));
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
    public ResponseEntity removeBeer(@PathVariable Long id) {
        beerService.deleteByID(id);
        return new ResponseEntity(HttpStatus.OK);

    }

    @PostMapping(value = "/insertBeer", consumes = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity add(@RequestBody Beer b) {
        beerService.saveAuthor(b);
        return new ResponseEntity(HttpStatus.CREATED);
    }

}
