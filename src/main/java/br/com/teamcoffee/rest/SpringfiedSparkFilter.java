package br.com.teamcoffee.rest;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import br.com.teamcoffee.config.TeamCoffeeConfig;
import spark.servlet.SparkApplication;
import spark.servlet.SparkFilter;

/*
 * Based on http://www.deadcoderising.com/sparkjava-dependency-injection-in-sparkapplication-using-spring/
 */
public class SpringfiedSparkFilter extends SparkFilter {
  private static final Logger LOGGER = LoggerFactory.getLogger(SpringfiedSparkFilter.class);

  @Override
  protected SparkApplication[] getApplications(FilterConfig filterConfig) throws ServletException {
    SparkApplication[] result = null;
    try {
      AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(TeamCoffeeConfig.class);
      String appsParameter = filterConfig.getInitParameter(APPLICATION_CLASS_PARAM);
      String[] appsName = appsParameter.split(",");

      result = new SparkApplication[appsName.length];
  
      for (int i = 0; i < appsName.length; i++) {
        result[i] = (SparkApplication) ctx.getBean(Class.forName(appsName[i]));
      }
    }
    catch (ClassNotFoundException e) {
      LOGGER.error("Error on create Spark Applications.", e);
      result = new SparkApplication[0];
    }
    
    return result;
  }

}
