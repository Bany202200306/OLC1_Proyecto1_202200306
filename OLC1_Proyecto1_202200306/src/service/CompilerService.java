
package service;

import analyzer.CompileError;
import analyzer.Lexer;
import analyzer.LexicalException;
import analyzer.ParseAbortException;
import analyzer.Parser;
import analyzer.Tokeninfo;
import analyzer.sym;
import engine.BattleEngine;
import engine.BattleScriptExecutionException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import java_cup.runtime.Symbol;
import model.ProgramModel;

/**
 *
 * @author baqzc
 */
public class CompilerService {
    public Result execute (String source){
        List<Tokeninfo> tokens = new ArrayList<>();
        List<CompileError> errors = new ArrayList<>();
        Lexer lexicalLexer = new Lexer(new StringReader(source));
        
        try{
            Symbol current;
            
            do{
                current = lexicalLexer.next_token();
            } while(current.sym != sym.EOF);
        } catch(LexicalException ex){
            
        } catch(Exception ex){
            errors.add(new CompileError("Lexico", ex.getMessage(), 0, 0));
        }
        tokens.addAll(lexicalLexer.getTokens());
        errors.addAll(lexicalLexer.getErrors());
        
        if(!errors.isEmpty()){
            return new Result(tokens, errors, "Analisis se detuvo por errores lexicos");
        }
        Parser parser = null;
        
         try {

            Lexer parserLexer = new Lexer(
                    new StringReader(source)
            );

            parser = new Parser(parserLexer);

            parser.parse();

        } catch (ParseAbortException ex) {

        } catch (Exception ex) {

            errors.add(new CompileError(
                    "Sintáctico",
                    ex.getMessage() == null
                            ? "Error durante el análisis sintáctico."
                            : ex.getMessage(),
                    0,
                    0
            ));
        }

        if (parser != null) {
            errors.addAll(parser.getErrors());
        }

        if (!errors.isEmpty()) {

            return new Result(
                    tokens,
                    errors,
                    "Análisis detenido por errores sintácticos."
            );
        }

        ProgramModel program = parser.getProgram();

        if (program == null) {

            errors.add(new CompileError(
                    "Sintáctico",
                    "No fue posible construir el programa.",
                    0,
                    0
            ));

            return new Result(
                    tokens,
                    errors,
                    "No fue posible construir el programa."
            );
        }

        try {

            BattleEngine engine = new BattleEngine();

            String output = engine.execute(program);

            return new Result(
                    tokens,
                    errors,
                    "ANÁLISIS LÉXICO CORRECTO\n"
                    + "ANÁLISIS SINTÁCTICO CORRECTO\n"
                    + "EJECUCIÓN CORRECTA\n\n"
                    + output
            );

        } catch (BattleScriptExecutionException ex) {

            errors.add(new CompileError(
                    "Ejecución",
                    ex.getMessage(),
                    0,
                    0
            ));

            return new Result(
                    tokens,
                    errors,
                    "Ejecución detenida:\n" + ex.getMessage()
            );

        } catch (RuntimeException ex) {

            errors.add(new CompileError(
                    "Ejecución",
                    ex.getMessage(),
                    0,
                    0
            ));

            return new Result(
                    tokens,
                    errors,
                    "Error de ejecución:\n" + ex.getMessage()
            );
        }
    }

    public static class Result {

        private final List<Tokeninfo> tokens;
        private final List<CompileError> errors;
        private final String output;

        public Result(
                List<Tokeninfo> tokens,
                List<CompileError> errors,
                String output
        ) {
            this.tokens = tokens;
            this.errors = errors;
            this.output = output;
        }

        public List<Tokeninfo> getTokens() {
            return tokens;
        }

        public List<CompileError> getErrors() {
            return errors;
        }

        public String getOutput() {
            return output;
        }

        public boolean isSuccess() {
            return errors.isEmpty();
        }
    }
}
