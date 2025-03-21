package com.auberer.compilerdesignlectureproject.reader;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CodeLoc implements Cloneable, Comparable<CodeLoc> {
  long line;
  long column;

  public String toString() {
    return "L" + line + "C" + column;
  }

  @Override
  public CodeLoc clone() {
    try {
      CodeLoc clone = (CodeLoc) super.clone();
      clone.line = line;
      clone.column = column;
      return clone;
    } catch (CloneNotSupportedException e) {
      throw new AssertionError();
    }
  }

  @Override
  public int compareTo(CodeLoc other) {
    if (line == other.line && column == other.column)
      return 0;
    else if (line == other.line)
      return column < other.column ? -1 : 1;
    else
      return line < other.line ? -1 : 1;
  }
}
