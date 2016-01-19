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
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Dan implements java.io.Serializable {

    private String _datum;
    private double _sum;
    private ArrayList<Racun> _listaRacuna;
    public Statistika statistika;

    public String getDatum() {
        return _datum;
    }

    public double getSum() {
        return _sum;
    }

    public void setDatum(String datum) {
        _datum = datum;
    }

    public void setSum(int sum) {
        _sum = sum;
    }
    public boolean isEmpty(){
        return _listaRacuna.isEmpty();
    }
    public Dan() {
        DateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Date date = new Date();
        this._datum = dateFormat.format(date);
        this._listaRacuna = new ArrayList<>();
        this.statistika=new Statistika();
        this._sum = 0;
    }

    public void add(Racun racun) {
        _listaRacuna.add(racun);
        Integer t=0;
        for(Proizvod p:racun._lista.keySet()){
            t=racun._lista.get(p);
            statistika.add(p,t);
        }
        _sum += racun.getSum();
    }

    public void save() {
        try {
            FileOutputStream fOut = new FileOutputStream("dani" + File.separator + "D-" + _datum + ".ser");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(this);
            oOut.close();
            fOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Dan read(String lokacija) {
        try {
            Dan dan;
            FileInputStream fIn = new FileInputStream(lokacija);
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            dan = (Dan) oIn.readObject();
            fIn.close();
            oIn.close();
            return dan;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void print() {
        System.out.println("==============");
        System.out.println("Racuni za " + _datum + " .");
        for (Racun r : _listaRacuna) {
            r.print();
        }
        System.out.println("TOTAL DAILY SUM:" + _sum);
        System.out.println("==============");
    }

    public static void print(Dan dan) {
        System.out.println("==============");
        System.out.println("Racuni za " + dan._datum + " .");
        for (Racun r : dan._listaRacuna) {
            r.print();
        }
        System.out.println("TOTAL DAYLY SUM:" + dan._sum);
        System.out.println("==============");
    }

}
