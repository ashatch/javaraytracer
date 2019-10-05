package net.andrewhatch.gfx.raytracer.cli;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

import org.apache.commons.cli.CommandLine;

import javax.inject.Named;

public class CliModule extends AbstractModule {

  private final CommandLine commandLine;

  public CliModule(final CommandLine commandLine) {
    this.commandLine = commandLine;
  }

  @Override
  protected void configure() {
  }

  @Provides
  @Singleton
  @Named("sourceFile")
  public String sourceFile() {
    return commandLine.getArgList().get(0);
  }

  @Provides
  @Singleton
  @Named("saveImage")
  public boolean saveImage() {
    return commandLine.hasOption("s");
  }

  @Provides
  @Singleton
  @Named("headless")
  public boolean displayImage() {
    return commandLine.hasOption("x");
  }
}
