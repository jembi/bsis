package org.jembi.bsis.template;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheException;
import com.github.mustachejava.MustacheFactory;

/**
 * Engine that encapsulates the template framework used in order to provide a simple way to
 * execute a template without having to learn a new framework.
 */
@Service
public class TemplateEngine {

  private static final Logger LOGGER = Logger.getLogger(TemplateEngine.class);

  MustacheFactory mf = new DefaultMustacheFactory();

  /**
   * Execute the specified template given the specified data and returns the result
   *
   * @param templateName String identifying the template (will be used for caching)
   * @param template String containing the template
   * @param data Object containing the data that will be used to generate the result
   * @return String output of running the template with the specified data
   * @throws IOException if any error occurs while reading the template or writing the output 
   */
  public String execute(String templateName, String template, Object data) throws IOException {
    String output = null;
    try (StringReader templateReader = new StringReader(template)) {
      try {
        Mustache mustache = mf.compile(templateReader, templateName);
        try (StringWriter outputWriter = new StringWriter()) {
            mustache.execute(outputWriter, data);
            outputWriter.flush();
            output = outputWriter.toString();
        }
      } catch (MustacheException e) {
        LOGGER.error("Error thrown while parsing or executing template '" + templateName + "'", e);
        output = "";
      }
    }
    return output;
  }
  
  public String execute(String templateName, String template, Map<String, String> map) throws IOException {
    String output = null;
    try (StringReader templateReader = new StringReader(template)) {
      Mustache mustache = mf.compile(templateReader, templateName);
      try (StringWriter outputWriter = new StringWriter()) {
        mustache.execute(outputWriter, map);
        outputWriter.flush();
        output = outputWriter.toString();
      }
    } catch (MustacheException e) {
      LOGGER.error("Error thrown while parsing or executing template '" + templateName + "'" + "For a HashMap", e);
    }
    return output;
  }
}