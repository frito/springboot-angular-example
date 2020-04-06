package org.example.server.api;

import io.swagger.annotations.ApiParam;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Properties;
import java.util.UUID;
import javax.validation.Valid;
import org.example.server.model.CaseInfo;
import org.example.server.model.CasesInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController public class CaseController implements CaseApi {

  private Logger logger = LoggerFactory.getLogger(CaseController.class);

  private final static String PROP_DATE = "date";
  private final static String PROP_DESC = "description";
  private final static String PROP_USER = "user";
  private final static String PROP_DURATION = "duration";

  @Override public ResponseEntity<CasesInfo> getCases() {
    File file = new File ("").getAbsoluteFile();

    logger.info("Get cases in path " + file.getAbsolutePath());

    CasesInfo casesInfo = new CasesInfo();
    casesInfo.setCases(new ArrayList<CaseInfo>());
    Integer sumDuration = 0;
    if (file.listFiles() != null) {
      for (File next : file.listFiles()) {

        if (next.isFile() && next.getName().endsWith(".case")) {
          Properties properties = new Properties();
          logger.info("Found file " + next.getAbsolutePath() + ":" + properties.toString());

          try {
            properties.load(new FileReader(next));
            CaseInfo caseInfo = new CaseInfo();
            caseInfo.setDate(properties.getProperty(PROP_DATE));
            caseInfo.setDescription(properties.getProperty(PROP_DESC));

            Integer duration = Integer.valueOf(properties.getProperty(PROP_DURATION));
            caseInfo.setDurationMinutes(duration);
            sumDuration = sumDuration + duration;

            caseInfo.setUser(properties.getProperty(PROP_USER));
            casesInfo.addCasesItem(caseInfo);

          } catch (IOException e) {
            logger.error(e.getLocalizedMessage(), e);
          }
        }

      }
    }
    else
      logger.error("No files found");

    casesInfo.setDurationMinutes(sumDuration);

    BigDecimal sumDurationInHours = new BigDecimal(sumDuration);
    sumDurationInHours = sumDurationInHours.divide(new BigDecimal(60), 2, RoundingMode.HALF_UP);
    casesInfo.setDurationHours(sumDurationInHours.doubleValue());

    String pricePerHourAsString = System.getenv("PERHOUR");
    if (pricePerHourAsString == null)
      pricePerHourAsString = "30";
    BigDecimal pricePerHour = new BigDecimal(pricePerHourAsString);

    BigDecimal totalCost = sumDurationInHours.multiply(pricePerHour);
    casesInfo.setCosts(totalCost.doubleValue());

    String caseName = System.getenv("CASENAME");
    if (caseName == null)
      caseName = "Default";

    casesInfo.setName(caseName);

    logger.info("Casename     : " + caseName);
    logger.info("Total cost   : " + totalCost.toString());

    return new ResponseEntity(casesInfo, HttpStatus.OK);
  }

  @Override public ResponseEntity<Void> sendCase(@ApiParam(value = "", required = true) @Valid @RequestBody CaseInfo mailadress) {

    logger.info("Send case in path " + new File ("").getAbsolutePath());
    logger.info("- Description : " + mailadress.getDescription());
    logger.info("- User        : " + mailadress.getUser());
    logger.info("- Duration    : " + mailadress.getDurationMinutes());

    Properties properties = new Properties();
    properties.setProperty(PROP_DATE, LocalDate.now().toString());
    properties.setProperty(PROP_DESC, mailadress.getDescription());
    properties.setProperty(PROP_USER, mailadress.getUser());
    properties.setProperty(PROP_DURATION, mailadress.getDurationMinutes().toString());
    try {
      properties.store(new FileWriter(new File(UUID.randomUUID().toString() + ".case")), "#generated");
    } catch (IOException e) {
      return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity(HttpStatus.OK);
  }
}
