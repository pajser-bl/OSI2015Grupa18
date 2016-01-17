/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Podaci;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pajser
 */
public class Izvjestaj implements java.io.Serializable{
    private String _kasir;
    private String _datum;
    private String _tekst;
    
    public Izvjestaj(String kasir,String tekst){
        DateFormat dateFormat=new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        Date date=new Date();
        _datum=dateFormat.format(date);
        _kasir=kasir;
        _tekst=tekst;
    }
    
    public void print(){
        
        System.out.println("-------------------");
        System.out.println("Izvjestaj "+_datum+"\nKasir: "+_kasir+"\n"+_tekst);
        System.out.println("-------------------");
    }
    public void save(){
        try {
            FileOutputStream fOut = new FileOutputStream("izvjestaji"+File.separator+"I-"+_datum);
            ObjectOutputStream oOut=new ObjectOutputStream(fOut);
            oOut.writeObject(this);
            oOut.close();
            fOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Izvjestaj.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Izvjestaj.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public Izvjestaj read(String lokacija){
        try {
            FileInputStream fIn = new FileInputStream(lokacija);
            ObjectInputStream oIn=new ObjectInputStream(fIn);
            Izvjestaj izvjestaj=(Izvjestaj)oIn.readObject();
            oIn.close();
            fIn.close();
            return izvjestaj;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Izvjestaj.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Izvjestaj.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
        
    }
    
    
    
    }
