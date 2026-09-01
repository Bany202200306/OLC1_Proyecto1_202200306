/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package analyzer;

/**
 *
 * @author baqzc
 */
public class Tokeninfo {
    
    private final int numero;
    private final String lexema;
    private final String tipo;
    private final int linea;
    private final int columna;
    
    public Tokeninfo(int numero,String lexema, String tipo, int linea, int columna){
        this.numero = numero;
        this.lexema = lexema;
        this.tipo = tipo;
        this.linea = linea;
        this.columna = columna;
    }
    
    public int getNumero(){
        return numero;
    }

    public String getLexema() {
        return lexema;
    }

    public String getTipo() {
        return tipo;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }
    
}
