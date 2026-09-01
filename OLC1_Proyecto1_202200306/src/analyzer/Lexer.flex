package analyzer;

import java_cup.runtime.Symbol;
import java.util.ArrayList;
import java.util.List;
import model.Action;

%%

%public
%class Lexer
%unicode
%cup
%line
%column
%state COMMENT

%{

private final List<TokenInfo> tokens = new ArrayList<>();
private final List<CompileError> errors = new ArrayList<>();

private int commentLine;
private int commentColumn;

public List<TokenInfo> getTokens() {
    return tokens;
}

public List<CompileError> getErrors() {
    return errors;
}

private Symbol token(int id, String type) {
    String lexeme = yytext();

    tokens.add(new TokenInfo(
        tokens.size() + 1,
        lexeme,
        type,
        yyline + 1,
        yycolumn + 1
    ));

    return new Symbol(
        id,
        yyline,
        yycolumn,
        lexeme
    );
}

private Symbol token(int id, String type, Object value) {
    String lexeme = yytext();

    tokens.add(new TokenInfo(
        tokens.size() + 1,
        lexeme,
        type,
        yyline + 1,
        yycolumn + 1
    ));

    return new Symbol(
        id,
        yyline,
        yycolumn,
        value
    );
}

private Symbol integerToken() {

    try {
        Integer value = Integer.valueOf(yytext());
        return token(sym.INT, "Integer", value);
    } catch (NumberFormatException ex) {

        errors.add(new CompileError(
            "Léxico",
            "Número entero fuera de rango: " + yytext(),
            yyline + 1,
            yycolumn + 1
        ));

        return token(sym.INT, "Integer", 0);
    }
}

private Symbol floatToken() {

    try {
        Double value = Double.valueOf(yytext());
        return token(sym.FLOAT, "Float", value);
    } catch (NumberFormatException ex) {

        errors.add(new CompileError(
            "Léxico",
            "Número decimal inválido: " + yytext(),
            yyline + 1,
            yycolumn + 1
        ));

        return token(sym.FLOAT, "Float", 0.0);
    }
}

%}

LineTerminator = \r\n|\r|\n
WhiteSpace = {LineTerminator}|[ \t\f]
Identifier = [A-Za-z_][A-Za-z0-9_]*
Integer = -?[0-9]+
Float = -?[0-9]+\.[0-9]+

%%

<YYINITIAL> {

    {WhiteSpace}+               { }

    "//"[^\r\n]*                { }

    "/*"                        {
                                    commentLine = yyline + 1;
                                    commentColumn = yycolumn + 1;
                                    yybegin(COMMENT);
                                }

    "mage"                      { return token(sym.MAGE, "MAGE"); }
    "warrior"                   { return token(sym.WARRIOR, "WARRIOR"); }
    "match"                     { return token(sym.MATCH, "MATCH"); }
    "main"                      { return token(sym.MAIN, "MAIN"); }
    "run"                       { return token(sym.RUN, "RUN"); }
    "with"                      { return token(sym.WITH, "WITH"); }

    "initial"                   { return token(sym.INITIAL, "INITIAL"); }
    "rules"                     { return token(sym.RULES, "RULES"); }
    "if"                        { return token(sym.IF, "IF"); }
    "then"                      { return token(sym.THEN, "THEN"); }
    "else"                      { return token(sym.ELSE, "ELSE"); }

    "players"                   { return token(sym.PLAYERS, "PLAYERS"); }
    "rounds"                    { return token(sym.ROUNDS, "ROUNDS"); }
    "scoring"                   { return token(sym.SCORING, "SCORING"); }
    "bonuses"                   { return token(sym.BONUSES, "BONUSES"); }
    "seed"                      { return token(sym.SEED, "SEED"); }

    "damage_point"              { return token(sym.DAMAGE_POINT, "DAMAGE_POINT"); }
    "healing_point"             { return token(sym.HEALING_POINT, "HEALING_POINT"); }
    "successful_defense"        { return token(sym.SUCCESSFUL_DEFENSE, "SUCCESSFUL_DEFENSE"); }
    "victory_bonus"             { return token(sym.VICTORY_BONUS, "VICTORY_BONUS"); }
    "failed_action_penalty"     { return token(sym.FAILED_ACTION_PENALTY, "FAILED_ACTION_PENALTY"); }

    "mage_combo"                { return token(sym.MAGE_COMBO, "MAGE_COMBO"); }
    "mage_combo_points"         { return token(sym.MAGE_COMBO_POINTS, "MAGE_COMBO_POINTS"); }
    "warrior_combo"             { return token(sym.WARRIOR_COMBO, "WARRIOR_COMBO"); }
    "warrior_combo_points"      { return token(sym.WARRIOR_COMBO_POINTS, "WARRIOR_COMBO_POINTS"); }
    "low_health_victory"        { return token(sym.LOW_HEALTH_VICTORY, "LOW_HEALTH_VICTORY"); }

    "round_number"              { return token(sym.ROUND_NUMBER, "ROUND_NUMBER"); }
    "total_rounds"              { return token(sym.TOTAL_ROUNDS, "TOTAL_ROUNDS"); }
    "self_health"               { return token(sym.SELF_HEALTH, "SELF_HEALTH"); }
    "opponent_health"           { return token(sym.OPPONENT_HEALTH, "OPPONENT_HEALTH"); }
    "self_resource"             { return token(sym.SELF_RESOURCE, "SELF_RESOURCE"); }
    "opponent_resource"         { return token(sym.OPPONENT_RESOURCE, "OPPONENT_RESOURCE"); }
    "self_score"                { return token(sym.SELF_SCORE, "SELF_SCORE"); }
    "opponent_score"            { return token(sym.OPPONENT_SCORE, "OPPONENT_SCORE"); }
    "self_history"              { return token(sym.SELF_HISTORY, "SELF_HISTORY"); }
    "opponent_history"          { return token(sym.OPPONENT_HISTORY, "OPPONENT_HISTORY"); }
    "random"                    { return token(sym.RANDOM, "RANDOM"); }

    "get_move"                  { return token(sym.GET_MOVE, "GET_MOVE"); }
    "last_move"                 { return token(sym.LAST_MOVE, "LAST_MOVE"); }
    "get_moves_count"           { return token(sym.GET_MOVES_COUNT, "GET_MOVES_COUNT"); }
    "get_last_n_moves"          { return token(sym.GET_LAST_N_MOVES, "GET_LAST_N_MOVES"); }

    "ARCANE_BOLT"               { return token(sym.ARCANE_BOLT, "ACTION", Action.ARCANE_BOLT); }
    "FIREBALL"                  { return token(sym.FIREBALL, "ACTION", Action.FIREBALL); }
    "MAGIC_BARRIER"             { return token(sym.MAGIC_BARRIER, "ACTION", Action.MAGIC_BARRIER); }
    "HEALING_RUNE"              { return token(sym.HEALING_RUNE, "ACTION", Action.HEALING_RUNE); }
    "MEDITATE"                  { return token(sym.MEDITATE, "ACTION", Action.MEDITATE); }

    "SLASH"                     { return token(sym.SLASH, "ACTION", Action.SLASH); }
    "HEAVY_STRIKE"              { return token(sym.HEAVY_STRIKE, "ACTION", Action.HEAVY_STRIKE); }
    "SHIELD_BLOCK"              { return token(sym.SHIELD_BLOCK, "ACTION", Action.SHIELD_BLOCK); }
    "WAR_CRY"                   { return token(sym.WAR_CRY, "ACTION", Action.WAR_CRY); }
    "REST"                      { return token(sym.REST, "ACTION", Action.REST); }

    "=="                        { return token(sym.EQ, "EQ"); }
    "!="                        { return token(sym.NEQ, "NEQ"); }
    ">="                        { return token(sym.GTE, "GTE"); }
    "<="                        { return token(sym.LTE, "LTE"); }
    ">"                         { return token(sym.GT, "GT"); }
    "<"                         { return token(sym.LT, "LT"); }

    "&&"                        { return token(sym.AND, "AND"); }
    "||"                        { return token(sym.OR, "OR"); }
    "!"                         { return token(sym.NOT, "NOT"); }

    "{"                         { return token(sym.LBRACE, "LBRACE"); }
    "}"                         { return token(sym.RBRACE, "RBRACE"); }
    "["                         { return token(sym.LBRACKET, "LBRACKET"); }
    "]"                         { return token(sym.RBRACKET, "RBRACKET"); }
    "("                         { return token(sym.LPAREN, "LPAREN"); }
    ")"                         { return token(sym.RPAREN, "RPAREN"); }
    ":"                         { return token(sym.COLON, "COLON"); }
    ","                         { return token(sym.COMMA, "COMMA"); }

    {Float}                     { return floatToken(); }
    {Integer}                   { return integerToken(); }

    {Identifier}                { return token(sym.ID, "ID", yytext()); }

    .                           {
                                    errors.add(new CompileError(
                                        "Léxico",
                                        "El carácter \"" + yytext() + "\" no pertenece al lenguaje.",
                                        yyline + 1,
                                        yycolumn + 1
                                    ));
                                }
}

<COMMENT> {

    "*/"                        {
                                    yybegin(YYINITIAL);
                                }

    [^*\r\n]+                   { }

    "*"                         { }

    {LineTerminator}            { }

    <<EOF>>                     {
                                    errors.add(new CompileError(
                                        "Léxico",
                                        "Comentario multilínea sin cerrar.",
                                        commentLine,
                                        commentColumn
                                    ));

                                    throw new LexicalException(
                                        "Comentario multilínea sin cerrar."
                                    );
                                }
}
