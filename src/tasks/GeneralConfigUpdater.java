package tasks;

import com.google.gson.Gson;
import model.admin.GeneralConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import repository.GeneralConfigRepository;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


@Component
public class GeneralConfigUpdater {

    @Autowired
    private ServletContext servletContext;

    @Autowired
    private GeneralConfigRepository generalConfigRepository;

    public GeneralConfigUpdater() {

    }

    @Scheduled(fixedDelay = Integer.MAX_VALUE)
    public void applyCustomConfigsFromJSON () throws IOException {
        try {

            Reader reader = new InputStreamReader(servletContext.getResourceAsStream("/WEB-INF/general-configs.json"), "UTF-8");
            Gson gson = new Gson();
            GeneralConfig[] generalConfigsArray = gson.fromJson(reader, GeneralConfig[].class);

            for (GeneralConfig temp : generalConfigsArray) {
                System.out.println("Updating general config from file: " + temp.getDescription());
                generalConfigRepository.update(temp);
            }

        } catch (IOException error){
            System.out.println(error);
        }
    }
}
