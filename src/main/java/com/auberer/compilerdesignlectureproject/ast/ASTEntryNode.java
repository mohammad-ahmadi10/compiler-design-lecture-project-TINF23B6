package com.auberer.compilerdesignlectureproject.ast;

import java.util.List;

public class ASTEntryNode extends ASTNode implements IVisitable {

  @Override
  public <T> T accept(ASTVisitor<T> visitor) {
    return visitor.visitEntry(this);
  }

  public List<ASTFunctionDefNode> getFunctions() {
    return getChildren(ASTFunctionDefNode.class);
  }
}
