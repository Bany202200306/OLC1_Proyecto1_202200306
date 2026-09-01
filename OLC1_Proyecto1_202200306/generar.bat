@echo off
echo ========================================
echo Generando analizador BattleScript
echo ========================================

if exist "src\analyzer\Lexer.java" del /Q "src\analyzer\Lexer.java"
if exist "src\analyzer\Parser.java" del /Q "src\analyzer\Parser.java"
if exist "src\analyzer\sym.java" del /Q "src\analyzer\sym.java"

echo.
echo Generando Lexer con JFlex...
java -jar "lib\jflex-full-1.9.1.jar" -d "src\analyzer" "src\analyzer\Lexer.flex"

if errorlevel 1 (
    echo Error generando Lexer.
    pause
    exit /b 1
)

echo.
echo Generando Parser con CUP...
java -cp "lib\java-cup-11b.jar" java_cup.Main -parser Parser -symbols sym -destdir "src\analyzer" "src\analyzer\Parser.cup"

if errorlevel 1 (
    echo Error generando Parser.
    pause
    exit /b 1
)

echo.
echo ========================================
echo GENERACION COMPLETADA
echo ========================================
pause
