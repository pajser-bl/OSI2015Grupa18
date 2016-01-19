package Podaci;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Sedmica implements java.io.Serializable {

    private String _startDate;
    private String _endDate;
    private double _sum;
    private ArrayList<Dan> _listaDana;
    public Statistika statistika;

//    Getteri
    public String getStartDate() {
        return _startDate;
    }

    public String getEndDate() {
        return _endDate;
    }

    public double getSum() {
        return _sum;
    }
//    Setteri

    public void setStartDate(String _startDate) {
        this._startDate = _startDate;
    }

    public void setEndDate(String _endDate) {
        this._endDate = _endDate;
    }

    public void setSum(double _sum) {
        this._sum = _sum;
    }

    public Sedmica() {
        this._listaDana = new ArrayList<Dan>();
        this._sum = 0;
        this.statistika = new Statistika();
    }

    public void save() {
        try {
            FileOutputStream fOut = new FileOutputStream("sedmice" + File.separator + "S-" + _startDate + "-" + _endDate + ".ser");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(this);
            fOut.close();
            oOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sedmica.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Sedmica.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(Dan dan) {
        _listaDana.add(dan);
        _sum += dan.getSum();
        statistika.addStatistika(dan.statistika);
    }

    public static Sedmica read(String lokacija) {
        try {
            Sedmica sedmica;
            FileInputStream fIn = new FileInputStream(lokacija);
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            sedmica = (Sedmica) oIn.readObject();
            fIn.close();
            oIn.close();
            return sedmica;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Sedmica.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Sedmica.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void print() {
        System.out.println("==============");
        System.out.println("Racuni od " + _startDate + " do " + _endDate + " .");
        System.out.println("==============");
        for (Dan r : _listaDana) {
            r.print();
        }
        System.out.println("TOTAL WEEKLY SUM:" + _sum);
        System.out.println("==============");
    }

    public static void print(Sedmica sedmica) {
        System.out.println("==============");
        System.out.println("Racuni od " + sedmica._startDate + " do " + sedmica._endDate + " .");
        System.out.println("==============");
        for (Dan r : sedmica._listaDana) {
            r.print();
        }
        System.out.println("TOTAL WEEKLY SUM:" + sedmica._sum);
        System.out.println("==============");
    }

    public boolean isEmpty() {
        return _listaDana.isEmpty();
    }
}
