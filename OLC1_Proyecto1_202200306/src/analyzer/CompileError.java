
package analyzer;

/**
 *
 * @author baqzc
 */
public class CompileError {
    
    private final String tipo;
    private final String descripicion;
    private final int linea;
    private final int columna;
    
    public CompileError(String tipo, String descripcion, int linea, int columna){
        this.tipo = tipo;
        this.descripicion = descripcion;
        this.linea = linea;
        this.columna = columna;
    }

    public String getTipo() {
        return tipo;
    }

    public String getDescripicion() {
        return descripicion;
    }

    public int getLinea() {
        return linea;
    }

    public int getColumna() {
        return columna;
    }
    
    
}
