/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.ArrayList;
import java.util.Random;

/**
 *
 * @author lolita
 */
public class Nodo {

    private int terminal;
    private TipoOperacion f;
    private Nodo der;
    private Nodo izq;
    private Nodo cond;
    private Nodo padre;
    private static double porcEescoger = 0.4;

    public Nodo(TipoOperacion f, int valor, Nodo izq, Nodo der, Nodo cond) {
        if (f == TipoOperacion.HOJA) {
            this.terminal = valor;
        }
        this.f = f;
        this.izq = izq;
        this.der = der;
        this.cond = cond;
        if (this.izq != null) {
            this.izq.padre = this;
        }
        if (this.der != null) {
            this.der.padre = this;
        }
        if (this.cond != null) {
            this.cond.padre = this;
        }
    }

    public int getTerminal() {
        return this.terminal;
    }

    public boolean valor(ArrayList<Boolean> tabla) {
        if (this.f == TipoOperacion.HOJA) {
            return tabla.get(this.terminal);
        } else {
            switch (f) {
                case AND:
                    return der.valor(tabla) && izq.valor(tabla);
                case OR:
                    return der.valor(tabla) || izq.valor(tabla);
                case NOT:
                    return !izq.valor(tabla);
                case IF:
                    return this.cond.valor(tabla) ? izq.valor(tabla) : der.valor(tabla);
                default:
                    return tabla.get(this.terminal);
            }
        }
    }

    public Nodo recorrer(ArrayList<Integer> traza) {
        if (traza.isEmpty()) {
            return this;
        } else {
            switch (traza.get(0)) {
                case 0:
                    traza.remove(0);
                    return this.izq.recorrer(traza);
                case 1:
                    traza.remove(0);
                    return this.der.recorrer(traza);
                case 2:
                    traza.remove(0);
                    return this.cond.recorrer(traza);
                default:
                    return this;
            }
        }
    }

    public void setNodo(Nodo nuevo, ArrayList<Integer> traza, int pos) {
        if ((!traza.isEmpty()) && pos < traza.size()) {
            switch (traza.get(pos)) {
                case 0:
                    if (this.izq != null) {
                        pos++;
                        this.izq.setNodo(nuevo, traza, pos);
                        break;
                    }
                case 1:
                    if (this.der != null) {
                        pos++;
                        this.der.setNodo(nuevo, traza, pos);
                        break;
                    }
                case 2:
                    if (this.cond != null) {
                        pos++;
                        this.cond.setNodo(nuevo, traza, pos);
                        break;
                    }
                default:
                    this.f = nuevo.f;
                    this.izq = nuevo.izq;
                    this.der = nuevo.der;
                    this.cond = nuevo.cond;
                    if (this.izq != null) {
                        this.izq.padre = this;
                    }
                    if (this.der != null) {
                        this.der.padre = this;
                    }
                    if (this.cond != null) {
                        this.cond.padre = this;
                    }
                    this.terminal = nuevo.terminal;
            }
        } else {
            this.f = nuevo.f;
            this.izq = nuevo.izq;
            this.der = nuevo.der;
            this.cond = nuevo.cond;
            if (this.izq != null) {
                this.izq.padre = this;
            }
            if (this.der != null) {
                this.der.padre = this;
            }
            if (this.cond != null) {
                this.cond.padre = this;
            }
            this.terminal = nuevo.terminal;
        }
    }

    public int profundidad() {
        int izq = 0,der = 0,cond = 0;
        
        if (this.f == TipoOperacion.HOJA) {
            return 1;
        } else if (this.f == TipoOperacion.AND || this.f == TipoOperacion.OR) {
            izq = this.izq.profundidad();
            der = this.der.profundidad();
            if(izq > der)
                return 1 + izq;
            else
                return 1 + der;
        } else if (this.f == TipoOperacion.NOT) {
            return 1 + this.izq.profundidad();
        } else {
            izq = this.izq.profundidad();
            der = this.der.profundidad();
            cond = this.cond.profundidad();
            if(izq > der && izq > cond)
                return 1 + izq;
            else if(der > izq && der > cond)
                return 1 + der;
            else{
                return 1 + cond; 
            }
            
        }
    }

    /**
     * Devuelve TRUE si el arbol contiene almenos un nodo de tipo AND o OR
     * @return
     */
    public boolean hasMutableFunction() {
        if (f == TipoOperacion.HOJA) {
            return false;
        } else if(f == TipoOperacion.AND || f == TipoOperacion.OR){
            return true;
        } else {
            switch (f) {
                case IF:
                    return izq.hasMutableFunction() || der.hasMutableFunction() || cond.hasMutableFunction();
                case NOT:
                    return izq.hasMutableFunction();
                default:
                    return izq.hasMutableFunction() || der.hasMutableFunction();
            }
        }
    }

    public void permuta(Random r){
        switch(f){
            case NOT:
                if(this.hasMutableFunction())
                    this.izq.permuta(r);
                break;
            case IF:
                if(r.nextBoolean() && this.izq.hasMutableFunction()){
                    this.izq.permuta(r);
                }else if(r.nextBoolean() && this.der.hasMutableFunction()){
                    this.der.permuta(r);
                }else if(this.cond.hasMutableFunction()){
                    this.cond.permuta(r);
                }else{
                    if(this.izq.hasMutableFunction()){
                        this.izq.permuta(r);
                    }else{
                        this.der.permuta(r);
                    }
                }
                break;
            case OR:
            case AND:
                if(r.nextBoolean() && this.izq.hasMutableFunction()){
                    this.izq.permuta(r);
                }else if(r.nextBoolean() && this.der.hasMutableFunction()){
                    this.der.permuta(r);
                }else{
                    Nodo copia = this.izq.copy();
                    this.izq = this.der.copy();
                    this.der = copia;
                }
                break;
            default:
                break;
        }
    }
    
    public Nodo terminalRandom(Random r, ArrayList<Integer> traza) {
        if (this.f == TipoOperacion.HOJA) {
            return this;
        } else {
            if (this.f == TipoOperacion.NOT) {
                if (this.izq.f != TipoOperacion.HOJA) {
                    traza.add(0);
                    return this.izq.terminalRandom(r, traza);
                } else {
                    return this.izq;
                }
            } else {
                if (r.nextBoolean()) {
                    traza.add(0);
                    return this.izq.terminalRandom(r, traza);
                } else if (r.nextBoolean()) {
                    traza.add(1);
                    return this.der.terminalRandom(r, traza);
                } else if (this.cond != null) {
                    traza.add(2);
                    return this.cond.terminalRandom(r, traza);
                } else {
                    traza.add(0);
                    return this.izq.terminalRandom(r, traza);
                }
            }
        }
    }

    public Nodo funcionRandom(Random r, ArrayList<Integer> traza) {
        if(this.cond != null && r.nextBoolean() && this.cond.f != TipoOperacion.HOJA){
            traza.add(2);
            return cond.funcionRandom(r,traza);
        }else if(this.der != null && r.nextBoolean() && this.der.f != TipoOperacion.HOJA){
            traza.add(1);
            return der.funcionRandom(r,traza);
        }else if(this.izq != null && r.nextBoolean() && this.izq.f != TipoOperacion.HOJA){
            traza.add(0);
            return izq.funcionRandom(r,traza);
        }else{
            if(this.padre != null && this.padre.padre != null)
                return this.padre;
            else
                return null;
        }
    }

    public void mutaFuncion(Random r) {
        if (r.nextBoolean()) {
            this.auxMutaf();
        } else {
            switch (f) {
                case NOT:
                    break;
                case HOJA:
                    break;
                default:
                    if (r.nextBoolean() && this.izq.f != TipoOperacion.HOJA) {
                        this.izq.mutaFuncion(r);
                    } else if (r.nextBoolean() && this.der.f != TipoOperacion.HOJA) {
                        this.der.mutaFuncion(r);
                    } else if (this.cond != null && this.cond.f != TipoOperacion.HOJA) {
                        this.izq.mutaFuncion(r);
                    } else {
                        this.izq.mutaFuncion(r);
                    }
                    break;
            }
        }
    }

    public void auxMutaf() {
        switch (f) {
            case OR:
                f = TipoOperacion.AND;
                break;
            case AND:
                f = TipoOperacion.OR;
                break;
            case NOT:
                break;
            default:
                break;
        }
    }

    public void setFuncion(TipoOperacion func) {
        this.f = func;
    }

    public TipoOperacion getFuncion() {
        return this.f;
    }

    public void setTerminal(int x) {
        this.terminal = x;
    }

    public void mutaTerminal(Random r, int tam) {
        int aux = terminal;
        int rand = r.nextInt(tam);
        while (rand == terminal) {
            rand = r.nextInt(tam);
        }
        terminal = rand;
    }

    public void setIzq(Nodo i) {
        this.izq = i;
    }

    public void setDer(Nodo d) {
        this.der = d;
    }

    public Nodo getDer() {
        return der;
    }

    public Nodo getIzq() {
        return izq;
    }

    public Nodo copy() {
        Nodo ret = null;
        switch (f) {
            case HOJA:
                ret = new Nodo(this.f, this.terminal, null, null, null);
                break;
            case NOT:
                ret = new Nodo(this.f, 0, this.izq.copy(), null, null);
                break;
            case OR:
                ret = new Nodo(this.f, 0, this.izq.copy(), this.der.copy(), null);
                break;
            case AND:
                ret = new Nodo(this.f, 0, this.izq.copy(), this.der.copy(), null);
                break;
            case IF:
                ret = new Nodo(this.f, 0, this.izq.copy(), this.der.copy(), this.cond.copy());
                break;
            default:
                break;
        }
        return ret;
    }

    public String toString(String[] tabla) {
        if (this.f == TipoOperacion.HOJA) {
            return tabla[this.terminal];
        } else {
            switch (f) {
                case HOJA:
                    return tabla[this.terminal];
                case NOT:
                    return "(" + this.f.toString() + " " + this.izq.toString(tabla) + ")";
                case IF:
                    return "(" + this.f.toString() + " " + this.cond.toString(tabla) + " " + izq.toString(tabla) + " " + der.toString(tabla) + ")";
                default:
                    return "(" + this.f.toString() + " " + izq.toString(tabla) + " " + der.toString(tabla) + ")";
            }
        }
    }
}
