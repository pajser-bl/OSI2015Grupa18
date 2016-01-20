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
import java.util.HashMap;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Racun implements java.io.Serializable {

    private String _time;
    private double _sum;
    private String _cashier;
    private String _buyer;
    public final HashMap<Proizvod, Integer> _lista;

    //Getters
    public String getTime() {
        return _time;
    }

    public double getSum() {
        return _sum;
    }

    public String getCashier() {
        return _cashier;
    }

    //Setters
    public void setTime(String _time) {
        this._time = _time;
    }

    public void setSum(double _sum) {
        this._sum = _sum;
    }

    public void setCashier(String _cashier) {
        this._cashier = _cashier;
    }

    public Racun(String cashier, String buyer) {
        _buyer = buyer;
        _cashier = cashier;
        _sum = 0;
        this._lista = new HashMap<>();
    }

    public void add(Proizvod proizvod, int kolicina) {
        if (_lista.containsKey(proizvod)) {
            _lista.put(proizvod, _lista.get(proizvod) + kolicina);
        } else {
            _lista.put(proizvod, kolicina);
        }
        _sum += proizvod.getCijena() * kolicina;
    }

    public void remove(Proizvod proizvod, int kolicina) {
        if (_lista.containsKey(proizvod)) {
            if (_lista.get(proizvod) - kolicina <= 0) {
                _lista.remove(proizvod);
            } else {
                _lista.put(proizvod, _lista.get(proizvod) - kolicina);
            }
            _sum -= proizvod.getCijena() * kolicina;
        } else {
            System.out.println("Naredba nemoguca. Proizvoda nema na listi.");
        }
    }

    public void finish() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
        Date date = new Date();
        _time = dateFormat.format(date);
        try {
            FileOutputStream fOut = new FileOutputStream("racuni" + File.separator + "R-" + _time + ".ser");
            ObjectOutputStream oOut = new ObjectOutputStream(fOut);
            oOut.writeObject(this);
            fOut.close();
            oOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //za citanje serijalizovanog/sacuvanog racuna
    public static Racun read(String lokacija) {
        try {
            Racun racun;
            FileInputStream fIn = new FileInputStream(lokacija);
            ObjectInputStream oIn = new ObjectInputStream(fIn);
            racun = (Racun) oIn.readObject();
            fIn.close();
            oIn.close();
            return racun;
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        } catch (IOException | ClassNotFoundException ex) {
            Logger.getLogger(Racun.class.getName()).log(Level.SEVERE, null, ex);
            return null;
        }
    }

    public void print() {
        System.out.println(_time);
        System.out.println("Radnik: " + _cashier);
        if (!_buyer.equals("no_name")) {
            System.out.println("Kupac: " + _buyer);
        }
        for (Proizvod i : _lista.keySet()) {
            String kolicina = _lista.get(i).toString();
            i.print();
            System.out.printf(" x%s\n", kolicina);
        }
        System.out.printf("SUM:%.2fKM\n", _sum);
        System.out.println("--------------");
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this._time);
        hash = 17 * hash + (int) (Double.doubleToLongBits(this._sum) ^ (Double.doubleToLongBits(this._sum) >>> 32));
        hash = 17 * hash + Objects.hashCode(this._cashier);
        hash = 17 * hash + Objects.hashCode(this._buyer);
        hash = 17 * hash + Objects.hashCode(this._lista);
        return hash;
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
        final Racun other = (Racun) obj;
        if (Double.doubleToLongBits(this._sum) != Double.doubleToLongBits(other._sum)) {
            return false;
        }
        if (!Objects.equals(this._time, other._time)) {
            return false;
        }
        if (!Objects.equals(this._cashier, other._cashier)) {
            return false;
        }
        if (!Objects.equals(this._buyer, other._buyer)) {
            return false;
        }
        return Objects.equals(this._lista, other._lista);
    }

}
