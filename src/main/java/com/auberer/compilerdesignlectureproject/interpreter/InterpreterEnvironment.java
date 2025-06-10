package com.auberer.compilerdesignlectureproject.interpreter;

import com.auberer.compilerdesignlectureproject.codegen.Module;
import com.auberer.compilerdesignlectureproject.codegen.instructions.Instruction;
import lombok.Getter;
import lombok.Setter;

import java.util.ListIterator;
import java.util.Stack;

public class InterpreterEnvironment {

  private final Module module;
  private final boolean doTracing;
  @Setter
  @Getter
  private ListIterator<Instruction> instructionIterator;
  private final Stack<ListIterator<Instruction>> callStack = new Stack<>();

  public InterpreterEnvironment(Module module, boolean doTracing) {
    this.module = module;
    this.doTracing = doTracing;
  }

  public void interpret() {
    // ToDo(Marc): Implement
  }

}
