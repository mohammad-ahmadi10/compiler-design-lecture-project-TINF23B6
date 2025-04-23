// Grammar for the TINF programming language
grammar tinf;

entry: fctDef+;
fctDef: type IDENTIFIER COLON ASSIGN LPAREN paramLst? RPAREN LBRACE stmtLst RBRACE;
paramLst: param (SEMICOLON param)*;
param: type IDENTIFIER (ASSIGN atomicExpr)?;
stmtLst: stmt*;
stmt: exprStmt | varDeclStmt | returnStmt | ifStmt | whileLoop | doWhileLoop | forLoop | switchCaseStmt | anonymousBlockStmt;
exprStmt: assignStmt SEMICOLON;
varDeclStmt: type IDENTIFIER ASSIGN ternaryExpr SEMICOLON;
assignStmt: (IDENTIFIER ASSIGN)? ternaryExpr;

ifStmt: IF LPAREN ternaryExpr RPAREN ifBody elseStmt?;
ifBody: LBRACE stmtLst RBRACE;
elseStmt: ELSE (ifBody | ifStmt);

whileLoop: WHILE LPAREN ternaryExpr RPAREN LBRACE stmtLst RBRACE;

doWhileLoop: DO LBRACE stmtLst RBRACE WHILE LPAREN ternaryExpr RPAREN SEMICOLON;

forLoop: FOR LPAREN varDeclStmt ternaryExpr SEMICOLON assignStmt RPAREN LBRACE stmtLst RBRACE;

switchCaseStmt: SWITCH LPAREN ternaryExpr RPAREN LBRACE caseBlockLst defaultBlock? RBRACE;
caseBlockLst: caseBlock+;
caseBlock: CASE literal COLON stmtLst;
defaultBlock: DEFAULT COLON stmtLst;

anonymousBlockStmt: LBRACE stmtLst RBRACE;

ternaryExpr: equalityExpr (QUESTION_MARK equalityExpr COLON equalityExpr)?;
equalityExpr: additiveExpr ((EQUALS | NOT_EQUALS) additiveExpr)?;
additiveExpr: multiplicativeExpr ((PLUS | MINUS) multiplicativeExpr)*;
multiplicativeExpr: atomicExpr ((MUL | DIV) atomicExpr)*;
atomicExpr: literal | fctCall | IDENTIFIER | LPAREN ternaryExpr RPAREN;

returnStmt: RET ternaryExpr SEMICOLON;

fctCall: IDENTIFIER LPAREN argLst RPAREN;
argLst: atomicExpr (SEMICOLON atomicExpr)*;

literal: INT_LIT | DOUBLE_LIT | STRING_LIT;
type: TYPE_INT | TYPE_DOUBLE | TYPE_STRING | TYPE_BOOL;

QUESTION_MARK: '?';
COLON: ':';
ASSIGN: '=';
EQUALS: '==';
NOT_EQUALS: '!=';
PLUS: '+';
MINUS: '-';
MUL: '*';
DIV: '/';
LPAREN: '(';
RPAREN: ')';
LBRACE: '{';
RBRACE: '}';
SEMICOLON: ';';
IF: 'if';
ELSE: 'else';
WHILE: 'while';
DO: 'do';
FOR: 'for';
SWITCH: 'switch';
CASE: 'case';
DEFAULT: 'default';
RET: 'ret';
TYPE_INT: 'int';
TYPE_DOUBLE: 'double';
TYPE_STRING: 'string';
TYPE_BOOL: 'bool';

INT_LIT: [1-9][0-9]* | '0';
DOUBLE_LIT: ([1-9][0-9]* | '0')[.][0-9]+;
STRING_LIT: '"' (~["\\\r\n])* '"';
IDENTIFIER: [a-z_][a-zA-Z0-9_]*;

// Skipped tokens
LINE_COMMENT: '#' ~[\r\n]* -> skip;
WS: [ \t\r\n]+ -> skip;
