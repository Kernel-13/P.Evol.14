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

    public Nodo(TipoOperacion f, int valor, Nodo izq, Nodo der, Nodo cond) {
        if (f== TipoOperacion.HOJA){
             this.terminal = valor;
        }
        this.f = f;
        this.izq = izq;
        this.der = der;
        this.cond = cond;
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

    public void setNodo(Nodo nuevo, ArrayList<Integer> traza) {
        if(!traza.isEmpty())switch (traza.get(0)) {
            case 0:
                traza.remove(0);
                this.izq.setNodo(nuevo, traza);
                break;
            case 1:
                traza.remove(0);
                this.der.setNodo(nuevo, traza);
                break;
            case 2:
                traza.remove(0);
                this.cond.setNodo(nuevo, traza);
                break;
            default:
                this.f = nuevo.f;
                this.izq = nuevo.izq;
                this.der = nuevo.der;
                this.cond = nuevo.cond;
                this.terminal = nuevo.terminal;
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
                    return this;
                }
            } else {
                double rand = r.nextDouble();
                if (rand < 1 / 3) {
                    traza.add(0);
                    return this.izq.terminalRandom(r, traza);
                } else if (rand < 2 / 3) {
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
        if (this.f != TipoOperacion.HOJA) {
            if (r.nextBoolean()) {
                return this;
            } else {
                switch (f) {
                    case NOT:
                        if (this.izq.f != TipoOperacion.HOJA) {
                            traza.add(0);
                            return this.izq.funcionRandom(r, traza);
                        } else {
                            return this;
                        }
                    default:
                        double rand = r.nextDouble();
                        if (rand < 1 / 3 && this.izq.f != TipoOperacion.HOJA) {
                            traza.add(0);
                            return this.izq.funcionRandom(r, traza);
                        } else if (rand < 2 / 3 && this.der.f != TipoOperacion.HOJA) {
                            traza.add(1);
                            return this.der.funcionRandom(r, traza);
                        } else if (this.cond != null && this.cond.f != TipoOperacion.HOJA) {
                            traza.add(2);
                            return this.cond.funcionRandom(r, traza);
                        } else {
                            return this;
                        }
                }
            }
        } else {
            return null;
        }
    }
    
    public void setFuncion(TipoOperacion func) {
        this.f = func;
    }
    
    public TipoOperacion getFuncion() {
        return this.f;
    }
    
    public void setTerminal(int x){
        this.terminal = x;
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
