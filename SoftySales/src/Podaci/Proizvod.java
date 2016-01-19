package Podaci;

import java.util.Objects;
import java.util.Scanner;

public class Proizvod implements java.io.Serializable {

    private String _naziv;
    private long _sifra;
    private double _cijena;
    private static long _counter;//sifre krecu od 000001 pa ++

//  Getters
    public String getNaziv() {
        return _naziv;
    }

    public long getSifra() {
        return _sifra;
    }

    public double getCijena() {
        return _cijena;
    }
//  Setters

    public void setNaziv(String naziv) {
        _naziv = naziv;
    }

    public void setSifra(long sifra) {
        _sifra = sifra;
    }

    public void setCijena(double cijena) {
        _cijena = cijena;
    }

    //konstruktor
    public Proizvod(String naziv, double cijena) {
        _naziv = naziv;
        _cijena = cijena;
        _sifra = ++_counter;
    }

    @Override
    public String toString() {
        return _sifra + "#" + _naziv + "#" + _cijena;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Proizvod other = (Proizvod) obj;
        if (this._sifra != other._sifra) {
            return false;
        }
        if (Double.doubleToLongBits(this._cijena) != Double.doubleToLongBits(other._cijena)) {
            return false;
        }
        return Objects.equals(this._naziv, other._naziv);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 29 * hash + Objects.hashCode(this._naziv);
        hash = 29 * hash + (int) (this._sifra ^ (this._sifra >>> 32));
        hash = 29 * hash + (int) (Double.doubleToLongBits(this._cijena) ^ (Double.doubleToLongBits(this._cijena) >>> 32));
        return hash;
    }

    //print za racun
    public void print() {
        System.out.format("%08d", _sifra);
        System.out.print(" " + _naziv + " " + _cijena);
    }

    //za modifikaciju i unos novih proizvoda
    public void read() {
        boolean ok = false;
        try (Scanner sc = new Scanner(System.in)) {
            while (!ok) {
                System.out.println("Naziv: ");
                this._naziv = sc.nextLine();
                System.out.println("Cijena: ");
                this._cijena = sc.nextDouble();
                print();
                System.out.println("\nDa li ste zadovoljni unosom?[y/n]");
                if (sc.next().equalsIgnoreCase("y")) {
                    ok = true;
                }
            }
        }
    }

}
