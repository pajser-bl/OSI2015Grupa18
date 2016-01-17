package Podaci;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author pajser
 */
import java.util.Scanner;

public class Proizvod implements java.io.Serializable{
    private String _naziv;
    private long _sifra;
    private double _cijena;
    private static long _counter;//sifre krecu od 000001 pa ++
    
    //Getters
    public String getNaziv(){return _naziv;}
    public long getSifra(){return _sifra;}
    public double getCijena(){return _cijena;}
    //Setters
    public void setNaziv(String naziv){_naziv=naziv;}
    public void setSifra(long sifra){_sifra=sifra;}
    public void setCijena(double cijena){_cijena=cijena;}
    
    //konstruktor
    public Proizvod(String naziv,double cijena){
        _naziv=naziv;
        _cijena=cijena;
        _sifra=++_counter;
    }
    
    @Override
    public String toString(){
        return _sifra+"#"+_naziv+"#"+_cijena;
    }
    public boolean equals(Proizvod proizvod){
        return _sifra==proizvod.getSifra();
    }
    //print za racun
    public void print(){
	System.out.format("%08d", _sifra);
	System.out.print(" "+_naziv+" "+_cijena);
    }
    //za modifikaciju i unos novih proizvoda
    public void read(){
	boolean ok=false;
        try (Scanner sc = new Scanner(System.in)) {
            while(!ok){
                System.out.println("Naziv: ");
                this._naziv=sc.nextLine();
                System.out.println("Cijena: ");
                this._cijena=sc.nextDouble();
                print();
                System.out.println("\nDa li ste zadovoljni unosom?[y/n]");
                if(sc.next().equalsIgnoreCase("y"))
                    ok=true;
            }
        }        
    }

    
    
}
