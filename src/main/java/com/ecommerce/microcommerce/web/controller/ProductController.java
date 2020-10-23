package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Api(value="ecommercePROJECT")
@RestController
public class ProductController {

    @Autowired
    private ProductDao productDao;

    public List<Product> listeProduits = new ArrayList<Product>(){
        {
            add(new Product(2, "Aspirateur Robot", 500,200));
            add(new Product(1, "Ordinateur Portable",350, 120));
            add(new Product(3, "Table de Ping Pong", 750, 400));
        }
    };


    //Récupérer la liste des produits
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
        Iterable<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }


    //Récupérer un produit par son Id
    @ApiOperation(value = "get specified product by id", response = Product.class, tags= "getById")
    @RequestMapping(value = "/getProductById/{productId}", method=RequestMethod.GET)
    public Product afficherUnProduit(int productId) {
        for(int i=0;i<listeProduits.size();i++){
            if(listeProduits.get(i).getId()==productId){
                return listeProduits.get(i);
            }
        }
        return new Product(0, "NA", 0, 0);
    }




    //ajouter un produit
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        Product productAdded =  productDao.save(product);

        if (productAdded == null)
            return ResponseEntity.noContent().build();

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(productAdded.getId())
                .toUri();

        return ResponseEntity.created(location).build();
    }

    // supprimer un produit
    @ApiOperation(value = "delete specified product by id", response = Product.class, tags= "delete")
    @RequestMapping(value = "/deleteProduct/{productId}", method=RequestMethod.DELETE)
    public void supprimerProduit(int productId) {
        this.productDao.delete(productId);
    }

    // Mettre à jour un produit
    @ApiOperation(value = "update specified product", response = Product.class, tags= "update")
    @RequestMapping(value = "/updateProductById/{product}", method=RequestMethod.PUT)
    public void updateProduit(@RequestBody Product product) {
        Product p = productDao.findById(product.getId());
        p.setNom(product.getNom());
        p.setPrix(product.getPrix());
        p.setPrixAchat(product.getPrixAchat());
    }


    //Pour les tests
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }



}
