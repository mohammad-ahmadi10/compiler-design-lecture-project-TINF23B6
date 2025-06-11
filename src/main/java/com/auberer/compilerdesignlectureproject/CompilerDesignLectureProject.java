package com.auberer.compilerdesignlectureproject;

import com.auberer.compilerdesignlectureproject.antlr.ASTBuilder;
import com.auberer.compilerdesignlectureproject.antlr.gen.TInfLexer;
import com.auberer.compilerdesignlectureproject.antlr.gen.TInfParser;
import com.auberer.compilerdesignlectureproject.ast.ASTEntryNode;
import com.auberer.compilerdesignlectureproject.ast.ASTVisualizer;
import com.auberer.compilerdesignlectureproject.codegen.CodeGenerator;
import com.auberer.compilerdesignlectureproject.codegen.Module;
import com.auberer.compilerdesignlectureproject.interpreter.InterpreterEnvironment;
import com.auberer.compilerdesignlectureproject.lexer.Lexer;
import com.auberer.compilerdesignlectureproject.parser.Parser;
import com.auberer.compilerdesignlectureproject.reader.Reader;
import com.auberer.compilerdesignlectureproject.sema.SymbolTableBuilder;
import com.auberer.compilerdesignlectureproject.sema.TypeChecker;
import com.auberer.compilerdesignlectureproject.util.CustomJSONPrettyPrinter;
import com.auberer.compilerdesignlectureproject.util.CustomUncaughtExceptionHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CompilerDesignLectureProject {
  private static final Logger log = LoggerFactory.getLogger(CompilerDesignLectureProject.class);

  public static void main(String[] args) {
    Options cliOptions = new Options()
        .addOption("h", "help", false, "Print this help text")
        .addOption("c", "comparable-output", false, "Only produce comparable output (e.g. no line numbers in exception stack traces, etc.)")
        .addOption("antlr", "use-antlr-parser", false, "Use ANTLR generated parser")
        .addOption("tokens", "dump-tokens", false, "Dump the lexed tokens")
        .addOption("ast", "dump-ast", false, "Dump the AST as dot file")
        .addOption("symtab", "dump-symbol-table", false, "Dump the symbol table along with the scopes")
        .addOption("types", "dump-typed-symbol-table", false, "Dump the symbol table along with the scopes and the types")
        .addOption("ir", "dump-ir", false, "Dump the intermediate representation")
        .addOption(null, "disable-verifier", false, "Disable the IR verifier")
        .addOption("t", "trace", false, "Enables tracing while interpreting the generated IR");

    DefaultParser cliParser = new DefaultParser();
    try {
      CommandLine cli = cliParser.parse(cliOptions, args);
      if (cli.hasOption('h')) {
        new HelpFormatter().printHelp("tinf-compiler args...", cliOptions);
        System.exit(0);
      }

      if (cli.hasOption('c')) {
        Thread.setDefaultUncaughtExceptionHandler(new CustomUncaughtExceptionHandler());
      }

      if (args.length == 0) {
        System.out.println("No file path provided!");
        System.exit(1);
      }
      // Convert the first command line argument to a Path
      String[] positionalArgs = cli.getArgs();
      Path path = Paths.get(positionalArgs[0]).toAbsolutePath();

      // Read, lex and parse the input file
      ASTEntryNode ast;
      if (cli.hasOption("antlr")) {
        System.out.println("Compiling with ANTLR parser...");
        ast = parseWithANTLRParser(path);

      } else {
        System.out.println("Compiling with own parser...");

        boolean dumpTokens = cli.hasOption("tokens");
        ast = parseWithOwnParser(path, dumpTokens);

        if(dumpTokens) return;
      }
      assert ast != null;

      // Dump AST
      if (cli.hasOption("ast")) {
        System.out.println("Dumping AST ...");
        ASTVisualizer visualizer = new ASTVisualizer();
        String dot = visualizer.visitEntry(ast);
        System.out.println(dot);
        return;
      }

      SymbolTableBuilder symbolTableBuilder = new SymbolTableBuilder();
      symbolTableBuilder.visitEntry(ast);

      // Dump symbol table
      if (cli.hasOption("symtab")) {
        System.out.println("Dumping scopes with symbol tables ...");
        ObjectMapper mapper = new ObjectMapper();
        CustomJSONPrettyPrinter pp = new CustomJSONPrettyPrinter();
        System.out.println(mapper.writer(pp).writeValueAsString(ast.getRootScope()));
      }

      TypeChecker typeChecker = new TypeChecker();
      typeChecker.visitEntry(ast);

      // Dump symbol table after type implemented
      if (cli.hasOption("types")) {
        System.out.println("Dumping scopes with symbol tables with type ...");
        ObjectMapper mapper = new ObjectMapper();
        CustomJSONPrettyPrinter pp = new CustomJSONPrettyPrinter();
        System.out.println(mapper.writer(pp).writeValueAsString(ast.getRootScope()));
      }

      // Generate IR
      String moduleName = path.getFileName().toString();
      CodeGenerator codeGenerator = new CodeGenerator(moduleName);
      codeGenerator.visit(ast);
      Module irModule = codeGenerator.getModule();

      // Dump IR
      if (cli.hasOption("ir")) {
        System.out.println("Dumping IR ...");
        StringBuilder sb = new StringBuilder();
        irModule.dumpIR(sb);
        System.out.println(sb);
      }

      // Verify IR
      if (!cli.hasOption("disable-verifier")) {
        irModule.verify();
      }

      // Interpret
      boolean doTracing = cli.hasOption("trace");
      InterpreterEnvironment environment = new InterpreterEnvironment(irModule, doTracing);
      environment.interpret();

      System.out.println("Compilation successful!");
    } catch (ParseException e) {
      new HelpFormatter().printHelp("tinf-compiler args...", cliOptions);
    } catch (JsonProcessingException e) {
      log.error("An error occurred", e);
    } catch (IOException e) {
      log.error("An IO error occurred", e);
    }
  }

  static ASTEntryNode parseWithOwnParser(Path path, boolean dumpTokens) {
    // Create a new Reader object with the given file path
    Reader reader = new Reader(path);

    // Create lexer and parser
    Lexer lexer = new Lexer(reader, dumpTokens);
    Parser parser = new Parser(lexer);

    // Parse the input file
    return parser.parse();
  }

  static ASTEntryNode parseWithANTLRParser(Path path) throws IOException {
    // Setup ANTLR
    ANTLRInputStream input = new ANTLRFileStream(path.toString());

    // Lex
    TInfLexer lexer = new TInfLexer(input);
    CommonTokenStream tokens = new CommonTokenStream(lexer);

    // Parse
    TInfParser parser = new TInfParser(tokens);
    TInfParser.EntryContext entryContext = parser.entry();

    // Transform parsetree to AST
    ASTBuilder astBuilder = new ASTBuilder();
    return (ASTEntryNode) astBuilder.visitEntry(entryContext);
  }
}