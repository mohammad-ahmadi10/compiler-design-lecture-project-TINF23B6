package com.auberer.compilerdesignlectureproject.ast;

import com.auberer.compilerdesignlectureproject.lexer.TokenType;
import com.auberer.compilerdesignlectureproject.sema.Scope;

import java.util.Set;

public class ASTIfBodyNode extends ASTNode {

  private Scope scope;
  @Override
  public <T> T accept(ASTVisitor<T> visitor) {
    return visitor.visitIfBody(this);
  }

  public static Set<TokenType> getSelectionSet() {
    return Set.of(TokenType.TOK_LBRACE);
  }

  public ASTStmtLstNode getStmtLst() {
    return getChild(ASTStmtLstNode.class, 0);
  }

  public Scope getScope() {
    return scope;
  }

  public void setScope(Scope scope) {
    this.scope = scope;
  }
}
