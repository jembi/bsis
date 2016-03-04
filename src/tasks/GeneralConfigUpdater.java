package tasks;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import model.admin.DataType;
import model.admin.GeneralConfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import repository.DataTypeRepository;
import repository.GeneralConfigRepository;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import utils.LoggerUtil;


@Component
public class GeneralConfigUpdater {

  @Autowired
  private ApplicationContext servletContext;

  @Autowired
  private GeneralConfigRepository generalConfigRepository;

  @Autowired
  private DataTypeRepository dataTypeRepository;

  public GeneralConfigUpdater() {

  }

  @Scheduled(fixedDelay = Integer.MAX_VALUE)
  public void applyCustomConfigsFromJSON() throws IOException {
    try {
      Resource resource = servletContext.getResource("/WEB-INF/classes/general-configs.json");
      if (resource.exists()) {
        Reader reader = new InputStreamReader(resource.getInputStream(), "UTF-8");
        Gson gson = new Gson();
        GeneralConfigFile[] generalConfigsArray = gson.fromJson(reader, GeneralConfigFile[].class);
        if (generalConfigsArray != null) {
          for (GeneralConfigFile temp : generalConfigsArray) {
            // Check if there is an existing config
            GeneralConfig existingConfig = generalConfigRepository.getGeneralConfigByName(temp.getName());

            // If config exists, update the value of the existing config property
            if (existingConfig != null) {
              System.out.println("Updating general config: " + temp.getName());
              existingConfig.setValue(temp.getValue());
              generalConfigRepository.update(existingConfig);
            }

            // If config doesn't exist, create a new config property
            else {
              DataType dataType = dataTypeRepository.getDataTypeByName(temp.getDataType());
              // get the dataType
              if (dataType != null) {
                System.out.println("Adding new general config from file: " + temp.getName());
                GeneralConfig generalConfig = new GeneralConfig();
                generalConfig.setDataType(dataType);
                generalConfig.setDescription(temp.getDescription());
                generalConfig.setName(temp.getName());
                generalConfig.setValue(temp.getValue());
                generalConfigRepository.save(generalConfig);
              } else {
                System.out.println("Please check your dataType: " + temp.getDataType());
              }
            }
          }
        } else {
          System.out.println("There are no configs to update");
        }
      } else {
        System.out.println("Could not find the config file in the path");
      }

    } catch (IOException error) {
      System.out.println(error.getMessage());
    } catch (JsonSyntaxException error) {
      System.out.println("Please check the syntax of your config file");
      System.out.println(error.getMessage());
    }
  }

  @Scheduled(fixedDelay = Integer.MAX_VALUE)
  public void initializeGeneralConfigs() {
    //Set the application root log level at startup
    GeneralConfig generalConfig = generalConfigRepository.getGeneralConfigByName("log.level");
    LoggerUtil.setLogLevel(generalConfig.getValue());
  }


}
