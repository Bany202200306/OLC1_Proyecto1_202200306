# Gramática BNF — BattleScript

**Proyecto 1 — Organización de Lenguajes y Compiladores 1**

---

## 1. Convenciones

En este documento:

- Los elementos escritos entre `< >` representan **no terminales**.
- Los elementos escritos entre comillas representan **terminales literales**.
- `ε` representa una producción vacía.
- `|` representa alternativas.

La presente gramática está escrita en BNF de forma conceptual y ordenada. No contiene las acciones Java utilizadas internamente por CUP.

---

## 2. Programa completo

```bnf
<program> ::= <strategy_list> <match_list> "main" "{" <run_list> "}"
```

El programa se divide en:

1. Una o más estrategias.
2. Una o más partidas.
3. Un único bloque `main`.

---

## 3. Lista de estrategias

```bnf
<strategy_list> ::= <strategy>
                  | <strategy_list> <strategy>
```

```bnf
<strategy> ::= <mage_strategy>
             | <warrior_strategy>
```

---

## 4. Estrategia de mago

```bnf
<mage_strategy> ::= "mage" <identifier> "{"
                    "initial" ":" <mage_action>
                    "rules" ":" "["
                    <mage_rules>
                    "else" <mage_action>
                    "]"
                    "}"
```

```bnf
<mage_rules> ::= ε
               | <mage_rules> <mage_rule> ","
```

```bnf
<mage_rule> ::= "if" <condition> "then" <mage_action>
```

---

## 5. Estrategia de guerrero

```bnf
<warrior_strategy> ::= "warrior" <identifier> "{"
                       "initial" ":" <warrior_action>
                       "rules" ":" "["
                       <warrior_rules>
                       "else" <warrior_action>
                       "]"
                       "}"
```

```bnf
<warrior_rules> ::= ε
                  | <warrior_rules> <warrior_rule> ","
```

```bnf
<warrior_rule> ::= "if" <condition> "then" <warrior_action>
```

### Restricción estructural

`else` debe aparecer exactamente una vez y siempre como última alternativa dentro del bloque `rules`.

---

## 6. Acciones

### Acciones de mago

```bnf
<mage_action> ::= "ARCANE_BOLT"
                | "FIREBALL"
                | "MAGIC_BARRIER"
                | "HEALING_RUNE"
                | "MEDITATE"
```

### Acciones de guerrero

```bnf
<warrior_action> ::= "SLASH"
                   | "HEAVY_STRIKE"
                   | "SHIELD_BLOCK"
                   | "WAR_CRY"
                   | "REST"
```

### Cualquier acción

```bnf
<any_action> ::= <mage_action>
               | <warrior_action>
```

---

## 7. Partidas

```bnf
<match_list> ::= <match>
               | <match_list> <match>
```

```bnf
<match> ::= "match" <identifier> "{"
            "players" ":" "[" <identifier> "," <identifier> "]"
            "rounds" ":" <integer>
            "scoring" ":" <scoring>
            "bonuses" ":" <bonuses>
            "}"
```

---

## 8. Sistema de puntuación

```bnf
<scoring> ::= "{" <scoring_properties> "}"
```

```bnf
<scoring_properties> ::= <scoring_property>
                       | <scoring_properties> "," <scoring_property>
```

```bnf
<scoring_property> ::= "damage_point" ":" <integer>
                     | "healing_point" ":" <integer>
                     | "successful_defense" ":" <integer>
                     | "victory_bonus" ":" <integer>
                     | "failed_action_penalty" ":" <integer>
```

### Restricciones de `scoring`

Las cinco propiedades son obligatorias.

Cada propiedad debe aparecer exactamente una vez.

El orden de las propiedades es libre.

No se permiten propiedades adicionales.

`damage_point` debe ser mayor que cero y los demás valores deben ser enteros no negativos.

---

## 9. Bonificaciones

```bnf
<bonuses> ::= "{" <bonus_properties> "}"
```

```bnf
<bonus_properties> ::= <bonus_property>
                     | <bonus_properties> "," <bonus_property>
```

```bnf
<bonus_property> ::= "mage_combo" ":" "[" <mage_action_list> "]"
                   | "mage_combo_points" ":" <integer>
                   | "warrior_combo" ":" "[" <warrior_action_list> "]"
                   | "warrior_combo_points" ":" <integer>
                   | "low_health_victory" ":" <integer>
```

```bnf
<mage_action_list> ::= <mage_action>
                     | <mage_action_list> "," <mage_action>
```

```bnf
<warrior_action_list> ::= <warrior_action>
                        | <warrior_action_list> "," <warrior_action>
```

### Restricciones de `bonuses`

Las cinco propiedades son obligatorias.

Cada propiedad debe aparecer exactamente una vez.

El orden de las propiedades es libre.

`mage_combo` solamente admite acciones de mago.

`warrior_combo` solamente admite acciones de guerrero.

---

## 10. Punto de entrada

```bnf
<run_list> ::= <run>
             | <run_list> <run>
```

```bnf
<run> ::= "run"
          "[" <identifier_list> "]"
          "with"
          "{"
          "seed" ":" <integer>
          "}"
```

```bnf
<identifier_list> ::= <identifier>
                    | <identifier_list> "," <identifier>
```

### Restricciones

La lista de partidas debe contener al menos un identificador.

La semilla debe ser un entero mayor que cero.

Se pueden declarar varias instrucciones `run` dentro de `main`.

---

## 11. Condiciones booleanas

Para representar correctamente la precedencia, la gramática de condiciones se divide en niveles.

```bnf
<condition> ::= <or_expression>
```

### Disyunción

```bnf
<or_expression> ::= <and_expression>
                  | <or_expression> "||" <and_expression>
```

### Conjunción

```bnf
<and_expression> ::= <not_expression>
                   | <and_expression> "&&" <not_expression>
```

### Negación y agrupación

```bnf
<not_expression> ::= "!" <not_expression>
                   | "(" <condition> ")"
                   | <comparison>
```

### Comparaciones

```bnf
<comparison> ::= <numeric_expression> <comparison_operator> <numeric_expression>
               | <action_expression> <equality_operator> <action_expression>
               | <list_expression> <equality_operator> <action_list_expression>
```

---

## 12. Operadores

```bnf
<comparison_operator> ::= "=="
                        | "!="
                        | ">"
                        | "<"
                        | ">="
                        | "<="
```

```bnf
<equality_operator> ::= "=="
                      | "!="
```

### Precedencia conceptual

De mayor a menor prioridad:

```text
1. Paréntesis y funciones
2. Negación !
3. Comparaciones
4. Conjunción &&
5. Disyunción ||
```

---

## 13. Expresiones numéricas

```bnf
<numeric_expression> ::= <integer>
                       | <float>
                       | "round_number"
                       | "total_rounds"
                       | "self_health"
                       | "opponent_health"
                       | "self_resource"
                       | "opponent_resource"
                       | "self_score"
                       | "opponent_score"
                       | "random"
                       | <moves_count_function>
```

---

## 14. Historiales

```bnf
<history> ::= "self_history"
            | "opponent_history"
```

```bnf
<history_source> ::= <history>
                   | <list_expression>
```

La forma `<history_source>` permite utilizar una lista generada por `get_last_n_moves` como entrada de `get_moves_count`.

---

## 15. Función `get_move`

```bnf
<get_move_function> ::= "get_move"
                        "("
                        <history>
                        ","
                        <integer>
                        ")"
```

Restricciones:

- El índice comienza en cero.
- El índice no puede ser negativo.
- El índice debe ser menor que el tamaño del historial.

---

## 16. Función `last_move`

```bnf
<last_move_function> ::= "last_move"
                         "("
                         <history>
                         ")"
```

La función requiere que el historial contenga al menos una acción.

---

## 17. Función `get_moves_count`

```bnf
<moves_count_function> ::= "get_moves_count"
                           "("
                           <history_source>
                           ","
                           <any_action>
                           ")"
```

Ejemplo de función anidada:

```text
get_moves_count(
    get_last_n_moves(opponent_history, 3),
    HEAVY_STRIKE
)
```

---

## 18. Función `get_last_n_moves`

```bnf
<list_expression> ::= "get_last_n_moves"
                      "("
                      <history>
                      ","
                      <integer>
                      ")"
```

Restricciones:

- `n` debe ser mayor que cero.
- `n` no puede superar la cantidad de acciones disponibles.

---

## 19. Expresiones de acción

```bnf
<action_expression> ::= <any_action>
                      | <get_move_function>
                      | <last_move_function>
```

---

## 20. Lista literal de acciones

```bnf
<action_list_expression> ::= "[" <any_action_list> "]"
```

```bnf
<any_action_list> ::= <any_action>
                    | <any_action_list> "," <any_action>
```

Ejemplo:

```text
[ARCANE_BOLT, ARCANE_BOLT, FIREBALL]
```

---

## 21. Comparaciones de movimientos

Ejemplo válido:

```text
last_move(opponent_history) == FIREBALL
```

Se describe mediante:

```bnf
<action_expression> <equality_operator> <action_expression>
```

---

## 22. Comparaciones de secuencias

Ejemplo:

```text
get_last_n_moves(self_history, 3)
== [ARCANE_BOLT, ARCANE_BOLT, FIREBALL]
```

Se describe mediante:

```bnf
<list_expression> <equality_operator> <action_list_expression>
```

---

## 23. Identificadores

Forma léxica conceptual:

```bnf
<identifier> ::= <letter_or_underscore> <identifier_tail>
```

```bnf
<identifier_tail> ::= ε
                    | <identifier_tail> <letter>
                    | <identifier_tail> <digit>
                    | <identifier_tail> "_"
```

Un identificador debe comenzar con una letra o guion bajo y posteriormente puede contener letras, números o guiones bajos.

Expresión regular equivalente:

```text
[A-Za-z_][A-Za-z0-9_]*
```

---

## 24. Enteros

```bnf
<integer> ::= <unsigned_integer>
            | "-" <unsigned_integer>
```

```bnf
<unsigned_integer> ::= <digit>
                     | <unsigned_integer> <digit>
```

---

## 25. Decimales

```bnf
<float> ::= <integer_part> "." <unsigned_integer>
```

```bnf
<integer_part> ::= <unsigned_integer>
                 | "-" <unsigned_integer>
```

Ejemplos:

```text
0.25
3.14
-2.50
```

---

## 26. Elementos léxicos ignorados

Los espacios, tabulaciones y saltos de línea se ignoran cuando no tienen significado sintáctico.

### Comentario de una línea

```text
// comentario hasta el fin de línea
```

### Comentario multilínea

```text
/* comentario de varias líneas */
```

Los comentarios son procesados exclusivamente por el analizador léxico y no forman parte del flujo de tokens enviado al parser.

Un comentario `/*` sin su correspondiente `*/` produce un error léxico y detiene el análisis.

---

## 27. Ejemplo derivable por la gramática

```text
mage Merlin {
    initial: ARCANE_BOLT
    rules: [
        if self_health <= 30 && self_resource >= 30 then HEALING_RUNE,
        if self_resource <= 20 then MEDITATE,
        else ARCANE_BOLT
    ]
}

warrior Ragnar {
    initial: SLASH
    rules: [
        if self_resource <= 20 then REST,
        else SLASH
    ]
}

match DueloFinal {
    players: [Merlin, Ragnar]
    rounds: 8

    scoring: {
        victory_bonus: 100,
        damage_point: 1,
        failed_action_penalty: 10,
        successful_defense: 20,
        healing_point: 1
    }

    bonuses: {
        warrior_combo_points: 40,
        mage_combo: [ARCANE_BOLT, ARCANE_BOLT, FIREBALL],
        low_health_victory: 25,
        warrior_combo: [SLASH, SLASH, HEAVY_STRIKE],
        mage_combo_points: 35
    }
}

main {
    run [DueloFinal] with {
        seed: 42
    }
}
```

El ejemplo demuestra que las propiedades internas de `scoring` y `bonuses` pueden presentarse en diferente orden mientras aparezcan exactamente una vez.

---

## 28. Resumen de restricciones externas a la BNF pura

Algunas condiciones se comprueban mediante validaciones adicionales porque dependen de valores o referencias construidas:

- Los nombres de estrategias no deben repetirse.
- Los nombres de partidas no deben repetirse.
- Los jugadores indicados por una partida deben existir.
- Las partidas indicadas en `run` deben existir.
- `rounds` debe ser mayor que cero.
- `seed` debe ser mayor que cero.
- `damage_point` debe ser mayor que cero.
- Los demás valores de puntuación no pueden ser negativos.
- Los combos deben pertenecer a la clase correspondiente.
- Los índices de historial deben estar dentro de rango.
- `last_move` requiere un historial no vacío.
- `get_last_n_moves` requiere una cantidad disponible suficiente.

Estas comprobaciones complementan la gramática sin introducir una fase semántica independiente.

