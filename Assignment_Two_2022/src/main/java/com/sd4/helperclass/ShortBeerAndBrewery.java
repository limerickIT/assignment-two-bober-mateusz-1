/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.helperclass;

import javax.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author venom
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ShortBeerAndBrewery {
    String beerName;
    String beerDescription;
    String breweryName;
}
