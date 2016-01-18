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

public class Mjesec implements java.io.Serializable {

    private String _mjesec;
    private double _sum;
    private ArrayList<Dan> _listaDana;
    protected Statistika statistika;

//  Getteri
    public String getMjesec() {
        return _mjesec;
    }

    public double getSum() {
        return _sum;
    }
//  Setteri

    public void setMjesec(String _mjesec) {
        this._mjesec = _mjesec;
    }

    public void setSum(double _sum) {
        this._sum = _sum;
    }

    public Mjesec() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        Integer temp = Integer.valueOf(dateFormat.format(date));
        switch (temp) {
            case 1:
                this._mjesec = "Januar";
                break;
            case 2:
                this._mjesec = "Februar";
                break;
            case 3:
                this._mjesec = "Mart";
                break;
            case 4:
                this._mjesec = "April";
                break;
            case 5:
                this._mjesec = "Maj";
                break;
            case 6:
                this._mjesec = "Jun";
                break;
            case 7:
                this._mjesec = "Jul";
                break;
            case 8:
                this._mjesec = "Avgust";
                break;
            case 9:
                this._mjesec = "Septembar";
                break;
            case 10:
                this._mjesec = "Oktobar";
                break;
            case 11:
                this._mjesec = "Novembar";
                break;
            case 12:
                this._mjesec = "Decembar";
                break;
        }
        this._listaDana = new ArrayList<Dan>();
        this._sum = 0;
    }

    public void add(Dan dan) {
        _listaDana.add(dan);
        _sum += dan.getSum();
        statistika.addStatistika(dan.statistika);
    }

    public void save() {
        try {
            FileOutputStream fOut = new FileOutputStream("mjeseci" + File.separator + "M-" + _mjesec + ".ser");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(this);
            fOut.close();
            oOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mjesec.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Mjesec.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Mjesec read(String lokacija) {
        try {
            Mjesec mjesec;
            FileInputStream fIn = new FileInputStream(lokacija);
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            mjesec = (Mjesec) oIn.readObject();
            fIn.close();
            oIn.close();
            return mjesec;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Mjesec.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Mjesec.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void print() {
        System.out.println("==============");
        System.out.println("Racuni za " + _mjesec + " .");
        for (Dan r : _listaDana) {
            r.print();
        }
        System.out.println("TOTAL MONTHLY SUM:" + _sum);
        System.out.println("==============");
    }

    public static void print(Mjesec mjesec) {
        System.out.println("==============");
        System.out.println("Racuni za " + mjesec._mjesec + " .");
        for (Dan r : mjesec._listaDana) {
            r.print();
        }
        System.out.println("TOTAL MONTHLY SUM:" + mjesec._sum);
        System.out.println("==============");
    }

}
