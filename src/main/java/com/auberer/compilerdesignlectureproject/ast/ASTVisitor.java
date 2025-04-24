package com.auberer.compilerdesignlectureproject.ast;

public class ASTVisitor<T> {

  // Generic visit methods

  T visit(ASTNode node) {
    return node.accept(this);
  }

  T visitChildren(ASTNode node) {
    for (ASTNode child : node.getChildren())
      child.accept(this);
    return null;
  }

  // Node-specific visit methods

  T visitEntry(ASTEntryNode node) {
    return visitChildren(node);
  }

  T visitPrintBuiltin(ASTPrintBuiltinCallNode node) {
    return visitChildren(node);
  }

  // ToDo(Students): Insert your visit methods here

}
