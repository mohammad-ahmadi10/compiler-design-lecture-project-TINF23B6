package com.auberer.compilerdesignlectureproject;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompilerDesignLectureProject {
  private static final Logger log = LoggerFactory.getLogger(CompilerDesignLectureProject.class);

  public static void main(String[] args) {
    Options cliOptions = new Options()
        .addOption("h", "help", false, "Print this help text");

    DefaultParser cliParser = new DefaultParser();
    try {
      CommandLine cli = cliParser.parse(cliOptions, args);
      if (cli.hasOption('h')) {
        new HelpFormatter().printHelp("tinf-compiler args...", cliOptions);
        System.exit(0);
      }

      // ToDo(Marc): Implement
    } catch (ParseException e) {
      new HelpFormatter().printHelp("tinf-compiler args...", cliOptions);
    } catch (Exception e) {
      log.error("An error occurred", e);
    }

    if (args.length == 0) {
      System.out.println("No file path provided!");
      System.exit(1);
    }
  }
}