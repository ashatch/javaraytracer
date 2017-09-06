package net.andrewhatch.gfx.raytracer.cli;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.util.Optional;

import javax.inject.Named;

public class CliModule extends AbstractModule {
  private static final String OPTION_SAVE = "save";
  private static final String OPTION_SAVE_SHORT = "s";
  private static final String OPTION_DISPLAY = "display";
  private static final String OPTION_DISPLAY_SHORT = "d";

  private final String[] args;

  public CliModule(final String[] args) {
    this.args = args;
  }

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  @Named("sourceFile")
  public Optional<String> sourceFile(final CommandLine commandLine) {
    if (commandLine.getArgList().size() > 0) {
      return Optional.of(commandLine.getArgList().get(0));
    }
    return Optional.empty();
  }

  @Provides
  @Singleton
  @Named("saveImage")
  public boolean saveImage(final CommandLine commandLine) {
    return commandLine.hasOption(OPTION_SAVE);
  }

  @Provides
  @Singleton
  @Named("displayImage")
  public boolean displayImage(final CommandLine commandLine) {
    return commandLine.hasOption(OPTION_SAVE);
  }

  @Provides
  @Singleton
  public CommandLine commandLine(final Options options) throws ParseException {
    final CommandLineParser parser = new DefaultParser();

    return parser.parse(options, args);
  }

  @Provides
  @Singleton
  public Options options() {
    final Options options = new Options();

    options.addOption(Option.builder(OPTION_SAVE_SHORT)
        .longOpt(OPTION_SAVE)
        .build());

    options.addOption(Option.builder(OPTION_DISPLAY_SHORT)
        .longOpt(OPTION_DISPLAY)
        .build());

    return options;
  }

  @Provides
  @Singleton
  public HelpFormatter helpFormatter() {
    return new HelpFormatter();
  }
}
