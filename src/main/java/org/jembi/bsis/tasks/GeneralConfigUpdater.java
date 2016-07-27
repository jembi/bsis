package org.jembi.bsis.tasks;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jembi.bsis.model.admin.DataType;
import org.jembi.bsis.model.admin.GeneralConfig;
import org.jembi.bsis.repository.DataTypeRepository;
import org.jembi.bsis.repository.GeneralConfigRepository;
import org.jembi.bsis.utils.LoggerUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


@Component
public class GeneralConfigUpdater {
  
  private static final Logger LOGGER = Logger.getLogger(GeneralConfigUpdater.class);

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
              LOGGER.debug("Updating general config: " + temp.getName());
              existingConfig.setValue(temp.getValue());
              generalConfigRepository.update(existingConfig);
            }

            // If config doesn't exist, create a new config property
            else {
              DataType dataType = dataTypeRepository.getDataTypeByName(temp.getDataType());
              // get the dataType
              if (dataType != null) {
                LOGGER.debug("Adding new general config from file: " + temp.getName());
                GeneralConfig generalConfig = new GeneralConfig();
                generalConfig.setDataType(dataType);
                generalConfig.setDescription(temp.getDescription());
                generalConfig.setName(temp.getName());
                generalConfig.setValue(temp.getValue());
                generalConfigRepository.save(generalConfig);
              } else {
                LOGGER.warn("General config '" + temp.getName() + " has an unknown dataType: " + temp.getDataType());
              }
            }
          }
        } else {
          LOGGER.trace("There are no configs to update");
        }
      } else {
        LOGGER.trace("Could not find the config file in the path");
      }

    } catch (IOException error) {
      LOGGER.error(error.getMessage());
    } catch (JsonSyntaxException error) {
      LOGGER.error("Please check the syntax of your general config file", error);
    }
  }

  @Scheduled(fixedDelay = Integer.MAX_VALUE)
  public void initializeGeneralConfigs() {
    GeneralConfig generalConfig = generalConfigRepository.getGeneralConfigByName("log.level");
    if (generalConfig != null && StringUtils.isNotBlank(generalConfig.getValue())) {
      LOGGER.info("Set the application root log level to: " + generalConfig.getValue());
      LoggerUtil.setLogLevel(generalConfig.getValue());
    }
  }


}
