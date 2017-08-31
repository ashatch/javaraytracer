package net.andrewhatch.gfx.raytracer;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import javax.inject.Named;

public class CliModule extends AbstractModule {
  private final String[] args;

  public CliModule(final String[] args) {
    this.args = args;
  }

  @Override
  protected void configure() {}

  @Provides
  @Named("sourceFile")
  public String sourceFile() throws ParseException {
    final Options options = new Options();
    final CommandLineParser parser = new DefaultParser();
    final CommandLine cmd = parser.parse(options, args);
    return cmd.getArgList().get(0);
  }
}
