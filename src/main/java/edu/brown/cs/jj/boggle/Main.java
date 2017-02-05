package edu.brown.cs.jj.boggle;

import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;

import freemarker.template.Configuration;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.regex.Pattern;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import spark.ExceptionHandler;
import spark.ModelAndView;
import spark.QueryParamsMap;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.TemplateViewRoute;
import spark.template.freemarker.FreeMarkerEngine;

/**
 * Entrypoint for invoking Boggle game. A REPL will start with instructions.
 */
public abstract class Main {

  private static final int PORT_NUM = 4567;
  private static final Gson GSON = new Gson();
  private static Set<String> legal = new TreeSet<>();

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

  //TODO: create static runSparkServer() method
  
  // TODO: create a PlayHandler
  
  // TODO: create a SubmitHandler
  
  
  // Handlers below here will be used starting from HTTP/AJAX/JS lab

  private static class ValidateHandler implements Route {
    @Override
    public String handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      String word = qm.value("word");

      int score = 0;
      boolean isValid = false;
      if (legal.contains(word)) {
        score += Board.score(word);
        isValid = true;
      }
      Map<String, Object> variables =
          ImmutableMap.of("score", score, "isValid", isValid);
      return GSON.toJson(variables);
    }
  }

  private static class ResultsHandler implements TemplateViewRoute {
    @Override
    public ModelAndView handle(Request req, Response res) {

      QueryParamsMap qm = req.queryMap();
      Board board = new Board(qm.value("board"));
      Iterable<String> guesses =
          BREAKWORDS.split(qm.value("guesses").toLowerCase());

      // We prefer to specify the more general interface types when
      // declaring variables, even though we must instantiate a more
      // specific, concrete type (TreeSet).
      // We also prefer to elide the types with "diamond operator". Why?
      SortedSet<String> good = new TreeSet<>();
      SortedSet<String> bad = new TreeSet<>();

      int score = 0;
      for (String word : guesses) {
        if (legal.contains(word)) {
          score += Board.score(word);
          good.add(word);
        } else {
          bad.add(word);
        }
      }

      // We know that 'missed' will also give results in sorted order, based on:
      // http://guava-libraries.googlecode.com/svn/tags/release04/javadoc/com/google/common/collect/Sets.html#difference(java.util.Set, java.util.Set)
      // What does that documentation mean by a "view"?
      // Why don't we declare 'missed' as a SortedSet?
      Set<String> missed = new TreeSet<>(legal);
      missed.removeAll(good);

      // We can't use ImmutableMap.of(), as in PlayHandler. Why not?
      // This is the "Builder" pattern which is a nice way to create
      // complicated immutable objects.
      Map<String, Object> variables = new ImmutableMap.Builder<String,Object>()
          .put("title", "Boggle: Results")
          .put("board", board)
          .put("score", score)
          .put("good", good)
          .put("bad", bad)
          .put("missed", missed).build();

      return new ModelAndView(variables, "results.ftl");
    }

    private static final Splitter BREAKWORDS =
        Splitter.on(Pattern.compile("\\W+")).omitEmptyStrings();
  }

  // Below here, there isn't much for a new CS32 student to worry
  // about understanding.

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.\n",
          templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private static final int INTERNAL_SERVER_ERROR = 500;
  /** A handler to print an Exception as text into the Response.
   */
  private static class ExceptionPrinter implements ExceptionHandler {
    @Override
    public void handle(Exception e, Request req, Response res) {
      res.status(INTERNAL_SERVER_ERROR);
      StringWriter stacktrace = new StringWriter();
      try (PrintWriter pw = new PrintWriter(stacktrace)) {
        pw.println("<pre>");
        e.printStackTrace(pw);
        pw.println("</pre>");
      }
      res.body(stacktrace.toString());
    }
  }
}
