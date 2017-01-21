package edu.brown.cs.jj.boggle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedHashSet;
import java.util.Set;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import spark.Spark;

/**
 * Entrypoint for invoking Boggle game. A REPL will start with instructions.
 */
public abstract class Main {

  private static final int PORT_NUM = 4567;

  /**
   * Method entrypoint for CLI invocation.
   *
   * @param args
   *          Arguments passed on the command line.
   */
  public static void main(String[] args) {
    OptionParser parser = new OptionParser();

    parser.accepts("generate");
    parser.accepts("gui");

    OptionSpec<String> solveSpec = parser.accepts("solve").withRequiredArg()
        .ofType(String.class);

    OptionSpec<String> scoreSpec = parser.accepts("score").withRequiredArg()
        .ofType(String.class);

    OptionSpec<Integer> portSpec = parser.accepts("port").withRequiredArg()
        .ofType(Integer.class);

    OptionSet options = parser.parse(args);

    if (options.has("generate")) {
      System.out.print(new Board().toString());
    } else if (options.has(solveSpec)) {
      Board provided = new Board(options.valueOf(solveSpec));
      for (String w : provided.play()) {
        System.out.printf("%s%n", w);
      }
    } else if (options.has(scoreSpec)) {
      Board provided = new Board(options.valueOf(scoreSpec));
      Set<String> legal = provided.play();

      Set<String> guesses = new LinkedHashSet<>();
      try (BufferedReader br = new BufferedReader(
          new InputStreamReader(System.in))) {
        String guess;
        while ((guess = br.readLine()) != null) {
          guesses.add(guess);
        }
        int score = 0;
        for (String word : guesses) {
          if (legal.contains(word)) {
            int s = Board.score(word);
            score += s;
            System.out.printf("%d %s%n", s, word);
          }
        }
        System.out.printf("Your score was %d%n", score);
      } catch (IOException ioe) {
        // Not possible. No error message can make sense of this.
        ioe.printStackTrace();
      }
    } else if (options.has("gui")) {
      if (options.has(portSpec)) {
        Spark.port(options.valueOf(portSpec));
      } else {
        Spark.port(PORT_NUM);
      }

      // TODO: Run Spark server.

    } else {
      System.out.println(
          "Use one of: --generate, --solve <board>, --gui [--port <number>],"
              + "--score <board>");
    }
  }

}
