package org.jembi.bsis.importer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.repository.UserRepository;
import org.jembi.bsis.security.BsisUserDetails;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class DataImporter {

  public static void main(String[] args) throws EncryptedDocumentException, InvalidFormatException, IOException {

    // Validate arguments
    String errorInUsageMessage =
        "\n\nUsage: \n\njava -jar bsis-bin.jar path_to_spreadsheet username boolean_validation_only\n\n"
        + "path_to_spreadsheet: Is the path to the spreadsheet where the import data is included. This argument is required.\n"
        + "username: User for auditing purposes. This argument is required.\n"
            + "boolean_validation_only: Must be true or false. This argument is optional, and it's false by default.\n";

    if (args.length < 2 || args.length > 3) {
      System.out.println(errorInUsageMessage);
      System.exit(1);
    }
    boolean validationOnly = false;
    if (args.length == 3) {
      if (args[2].equalsIgnoreCase("true")) {
        validationOnly = true;
      } else if (args[2].equalsIgnoreCase("false")) {
        validationOnly = false;
      } else {
        System.out.println("Invalid boolean_validation_only: " + args[2] + errorInUsageMessage);
        System.exit(1);
      }
    }
    
    // Load the application context
    ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("/bsis-servlet.xml");

    // Get services
    DataImportService importService = applicationContext.getBean(DataImportService.class);
    UserRepository userRepository = applicationContext.getBean(UserRepository.class);

    // Set security user in context
    String username = args[1];
    User user = userRepository.findUser(username);
    if (user == null) {
      System.err.println("Invalid username: " + username + errorInUsageMessage);
      System.exit(1);
    }
    BsisUserDetails bsisUser = new BsisUserDetails(user);
    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(bsisUser, "Credentials");
    SecurityContextHolder.getContext().setAuthentication(auth);

    // Read spreadsheet and import the data
    try (FileInputStream fileInputStream = new FileInputStream(args[0])) {
      Workbook workbook = WorkbookFactory.create(fileInputStream);
      importService.importData(workbook, validationOnly);
    } catch (DataImportService.RollbackException e) {
      // ignore this
    } catch (FileNotFoundException e) {
      System.err.println("Invalid path_to_spreadsheet: " + args[0] + errorInUsageMessage);
      System.exit(1);
    } catch (Exception e) {
      String action = validationOnly ? "Validation" : "Import";
      System.err.println(action + " failed");
      e.printStackTrace();
      System.exit(1);
    }
    
    // Close the application context
    applicationContext.close();
  }

}
