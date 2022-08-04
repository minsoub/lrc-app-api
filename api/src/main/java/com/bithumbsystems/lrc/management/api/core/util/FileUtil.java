package com.bithumbsystems.lrc.management.api.core.util;

import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class FileUtil {

  public static String readResourceFile(String path) throws IOException {
    ClassPathResource resources = new ClassPathResource(path);
    try (InputStream inputStream = resources.getInputStream()) {
      String string = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
      System.out.println(string);
      return string;
    }
  }
}
