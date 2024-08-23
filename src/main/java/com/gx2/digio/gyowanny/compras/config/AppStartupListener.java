package com.gx2.digio.gyowanny.compras.config;

import com.gx2.digio.gyowanny.compras.loader.DataLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class AppStartupListener {

  @Autowired
  private DataLoader dataLoader;

  @EventListener(ApplicationReadyEvent.class)
  public void onApplicationEvent() {
    dataLoader.run();
  }
}

