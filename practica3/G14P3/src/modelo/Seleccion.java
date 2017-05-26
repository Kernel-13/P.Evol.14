/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modelo;

import java.util.ArrayList;

/**
 *
 */
public abstract class Seleccion {
    abstract Cromosoma[] selecciona(Cromosoma[] pob,int nvars,ArrayList<ArrayList<Boolean>> casos);
}
