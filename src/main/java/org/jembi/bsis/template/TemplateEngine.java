package org.jembi.bsis.template;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.springframework.stereotype.Service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * Engine that encapsulates the template framework used in order to provide a simple way to
 * execute a template without having to learn a new framework.
 */
@Service
public class TemplateEngine {

  private static String DEFAULT_TEMPLATE_NAME = "template";
  MustacheFactory mf = new DefaultMustacheFactory();

  /**
   * Execute the specified template given the specified data and returns the result
   * 
   * @param template String containing the template
   * @param data Object containing the data that will be used to generate the result
   * @return String output of running the template with the specified data
   * @throws IOException if any error occurs while reading the template or writing the output 
   */
  public String execute(String template, Object data) throws IOException {
    String output = null;
    try (StringReader templateReader = new StringReader(template)) {
      Mustache mustache = mf.compile(templateReader, DEFAULT_TEMPLATE_NAME);
      try (StringWriter outputWriter = new StringWriter()) {
        mustache.execute(outputWriter, data);
        outputWriter.flush();
        output = outputWriter.toString();
      }
    }
    return output;
  }
}