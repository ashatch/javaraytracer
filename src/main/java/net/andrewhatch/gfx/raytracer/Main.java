package net.andrewhatch.gfx.raytracer;

import com.google.inject.Guice;

import net.andrewhatch.gfx.raytracer.cli.CliModule;
import net.andrewhatch.gfx.raytracer.engine.RayTracer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.IOException;

import javax.inject.Inject;

public class Main {
  @Inject
  public Main(
    final RayTracer rayTracer
  ) throws IOException {
    rayTracer.rayTraceFilePath();
  }

  public static void main(String[] args) throws ParseException {
    final Options options = new Options();

    options.addOption(
      Option.builder("s")
        .longOpt("save")
        .required(false)
        .desc("save the raytraced scene as a png")
        .build());

    options.addOption(
      Option.builder("x")
        .longOpt("headless")
        .required(false)
        .desc("headless mode: don't draw the raytraced scene in a window")
        .build());

    final CommandLineParser parser = new DefaultParser();
    final HelpFormatter helpFormatter = new HelpFormatter();

    try {
      CommandLine commandLine = parser.parse(options, args);

      if (commandLine.getArgList().size() != 1) {
        helpFormatter.printHelp(
          "javaraytracker [OPTIONS] <scene-file>", "", options, "");
        System.out.println("\nCould not find scene file: please supply one.");
        System.exit(1);
      }

      Guice.createInjector(
        new CliModule(commandLine),
        new RayTracerModule()
      ).getInstance(Main.class);
    } catch (ParseException parseException) {
      System.out.println("Could not parse the command line options");
      System.exit(1);
    }
  }
}
