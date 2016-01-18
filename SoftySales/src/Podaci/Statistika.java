package Podaci;

import java.util.HashMap;

public class Statistika implements java.io.Serializable {

    protected HashMap<Proizvod, Integer> _listaProizvoda;

//   Za dodavanje novih proizvoda u statistiku tokom dodavanja proizvoda u
//   sistem.
    public void addToList(Proizvod proizvod) {
        if (_listaProizvoda.containsKey(proizvod)) {
            _listaProizvoda.put(proizvod, 0);
        }
    }

    public void add(Proizvod proizvod, int kolicina) {
        if (_listaProizvoda.containsKey(proizvod)) {
            _listaProizvoda.put(proizvod, _listaProizvoda.get(proizvod) + kolicina);
        } else {
            this.addToList(proizvod);
            _listaProizvoda.put(proizvod, kolicina);
        }
    }

    public void print() {
        _listaProizvoda.keySet().stream().forEach((i) -> {
            String proizvod = i.getNaziv();
            String kolicina = _listaProizvoda.get(i).toString();
            System.out.println(proizvod + " x" + kolicina);
        });
    }

    public int getCoeficient(Proizvod proizvod) {
        return (int) _listaProizvoda.get(proizvod);
    }

    public void addStatistika(Statistika statistika) {
        for (Proizvod i : statistika._listaProizvoda.keySet()) {
            this.add(i, statistika._listaProizvoda.get(i));
        }
    }
}
