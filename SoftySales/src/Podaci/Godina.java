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

public class Godina implements java.io.Serializable {

    private String _godina;
    private double _sum;
    private ArrayList<Mjesec> _listaMjeseci;
    protected Statistika statistika;

    public void setGodina(String _godina) {
        this._godina = _godina;
    }

    public void setSum(double _sum) {
        this._sum = _sum;
    }

    public String getGodina() {
        return _godina;
    }

    public double getSum() {
        return _sum;
    }

    public Godina(String godina) {
//        DateFormat dateFormat = new SimpleDateFormat("MM");
//        Date date = new Date();
//        _godina = dateFormat.format(date);
        _godina=godina;
        statistika=new Statistika();
        _listaMjeseci=new ArrayList<>();
        _sum = 0;
    }

    public void add(Mjesec mjesec) {
        _listaMjeseci.add(mjesec);
        _sum += mjesec.getSum();
        statistika.addStatistika(mjesec.statistika);
    }

    public void save() {
        try {
            FileOutputStream fOut = new FileOutputStream("godine" + File.separator + "G-" + _godina + ".ser");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(this);
            oOut.close();
            fOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Godina.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Godina.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Godina read(String lokacija) {
        try {
            Godina godina;
            FileInputStream fIn = new FileInputStream(lokacija);
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            godina = (Godina) oIn.readObject();
            fIn.close();
            oIn.close();
            return godina;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mjesec.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Godina.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void print() {
        System.out.println("==============");
        System.out.println("Racuni za " + _godina + " .");
        for (Mjesec r : _listaMjeseci) {
            r.print();
        }
        System.out.println("TOTAL YEARLY SUM:" + _sum);
        System.out.println("==============");
    }

    public static void print(Godina godina) {
        System.out.println("==============");
        System.out.println("Racuni za " + godina + " .");
        for (Mjesec r : godina._listaMjeseci) {
            r.print();
        }
        System.out.println("TOTAL YEARLY SUM:" + godina._sum);
        System.out.println("==============");
    }
    public boolean isEmpty(){
        return _listaMjeseci.isEmpty();
    }
}
