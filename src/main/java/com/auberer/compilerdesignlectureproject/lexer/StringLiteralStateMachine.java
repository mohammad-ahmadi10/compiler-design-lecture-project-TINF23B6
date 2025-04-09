package com.auberer.compilerdesignlectureproject.lexer;

import com.auberer.compilerdesignlectureproject.lexer.statemachine.State;
import com.auberer.compilerdesignlectureproject.lexer.statemachine.StateMachine;

public class StringLiteralStateMachine extends StateMachine {
  @Override
  public void init() {
    // Start state
    State stateStart = new State("start");
    stateStart.setStartState(true);
    addState(stateStart);
    // Content state
    State contentState = new State("content");
    addState(contentState);
    // End state
    State stateEnd = new State("end");
    stateEnd.setAcceptState(true);
    addState(stateEnd);

    // Transitions
    addCharTransition(stateStart, contentState, '"');
    addElseTransition(contentState, contentState);
    addCharTransition(contentState, stateEnd, '"');
  }

  @Override
  public TokenType getTokenType() {
    return TokenType.TOK_STRING_LIT;
  }
}
