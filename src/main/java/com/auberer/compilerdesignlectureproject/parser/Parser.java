package com.auberer.compilerdesignlectureproject.parser;

import com.auberer.compilerdesignlectureproject.ast.ASTEntryNode;
import com.auberer.compilerdesignlectureproject.ast.ASTNode;
import com.auberer.compilerdesignlectureproject.lexer.ILexer;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Stack;

/**
 * Parser class for building the abstract syntax tree (AST).
 * Input: Token stream
 * Output: AST
 */
@Slf4j
public class Parser implements IParser {

  // Lexer interface that can be used to accept the given input
  ILexer lexer;
  // Stack to keep track of the parent nodes
  Stack<ASTNode> parentStack = new Stack<>();

  public Parser(ILexer lexer) {
    this.lexer = lexer;
  }

  /**
   * Entry point to the parser. This method should parse the input file and return the root node of the AST.
   *
   * @return AST root node
   */
  @Override
  public ASTEntryNode parse() {
    return null;
  }

}
