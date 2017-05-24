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
    private static double porcEescoger = 0.4;
    
    public Nodo(TipoOperacion f, int valor, Nodo izq, Nodo der, Nodo cond) {
        if (f == TipoOperacion.HOJA) {
            this.terminal = valor;
        }
        this.f = f;
        this.izq = izq;
        this.der = der;
        this.cond = cond;
    }

    public int getTerminal(){
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
                    this.terminal = nuevo.terminal;
            }
        } else {
            this.f = nuevo.f;
            this.izq = nuevo.izq;
            this.der = nuevo.der;
            this.cond = nuevo.cond;
            this.terminal = nuevo.terminal;
        }
    }

    public int profundidad() {
        if (this.f == TipoOperacion.HOJA) {
            return 1;
        } else if (this.f == TipoOperacion.AND || this.f == TipoOperacion.OR) {
            return 1 + max(this.izq.profundidad(), this.der.profundidad(), 0);
        } else if (this.f == TipoOperacion.NOT) {
            return 1 + max(this.izq.profundidad(), 0, 0);
        } else {
            return 1 + max(this.izq.profundidad(), this.der.profundidad(), this.cond.profundidad());
        }
    }

    private int max(int x, int y, int z) {
        if (x >= y && y >= z) {
            return x;
        } else if (y >= z && z >= x) {
            return y;
        } else {
            return z;
        }
    }

    public Nodo terminalRandom(Random r, ArrayList<Integer> traza) {
        r = new Random();
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
        if (this.f != TipoOperacion.HOJA) {
            if (r.nextDouble() < porcEescoger) {
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
                        if (r.nextBoolean() && this.izq.f != TipoOperacion.HOJA) {
                            traza.add(0);
                            return this.izq.funcionRandom(r, traza);
                        } else if (r.nextBoolean() && this.der.f != TipoOperacion.HOJA) {
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
            return this;
        }
    }
    
    public void mutaFuncion(Random r) {
        if (r.nextBoolean()) {
            this.auxMutaf();
        } else {
            switch (f) {
                case NOT:
                    this.auxMutaf();
                    break;
                case HOJA:
                    break;
                default:
                    float val = r.nextFloat();
                    if (val < 1 / 3 && this.izq.f != TipoOperacion.HOJA) {
                        this.izq.mutaFuncion(r);
                    } else if (val < 2 / 3 && this.der.f != TipoOperacion.HOJA) {
                        this.der.mutaFuncion(r);
                    } else if (this.cond != null && this.cond.f != TipoOperacion.HOJA) {
                        this.izq.mutaFuncion(r);
                    } else {
                        this.izq.mutaFuncion(r);
                    }
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
    
    public void mutaTerminal(Random r,int tam){
        int aux = terminal;
        int rand = r.nextInt(tam);
        while(rand == terminal){
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
