package net.andrewhatch.gfx.raytracer;

import com.google.common.eventbus.EventBus;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import net.andrewhatch.gfx.raytracer.documentreaders.AshSceneParser;
import net.andrewhatch.gfx.raytracer.documentreaders.SceneParser;

public class RayTracerModule extends AbstractModule {
  @Override
  protected void configure() {
    bind(SceneParser.class).to(AshSceneParser.class);
  }

  @Provides
  public EventBus eventBus() {
    return new EventBus();
  }
}
