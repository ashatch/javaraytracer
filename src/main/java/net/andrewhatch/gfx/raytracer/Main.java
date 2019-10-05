package net.andrewhatch.gfx.raytracer;

import com.google.inject.Guice;

import net.andrewhatch.gfx.raytracer.cli.CliModule;
import net.andrewhatch.gfx.raytracer.engine.RayTracer;

import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

public class Main {
  @Inject
  public Main(
    final RayTracer rayTracer,
    @Named("sourceFile") final Optional<String> sourceFile,
    final HelpFormatter helpFormatter,
    final Options options
  ) throws IOException {
    if (sourceFile.isPresent()) {
      rayTracer.rayTraceFilePath();
    } else {
      helpFormatter.printHelp(Main.class.getName(), options);
    }
  }

  public static void main(String[] args) {
    Guice.createInjector(
      new CliModule(args),
      new RayTracerModule()
    ).getInstance(Main.class);
  }
}
