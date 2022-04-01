/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sd4.helperclass;

import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author venom
 */
public class PDFhelper {
    
    //Should be a singleton class
    public PDFhelper(){
        
    }
    public static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }

    }
    
    
}
