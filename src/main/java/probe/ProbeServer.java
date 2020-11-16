package probe;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.redirect;
import static spark.Spark.staticFiles;

import java.util.Collections;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import spark.ModelAndView;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class ProbeServer {

  public static Logger logger = LoggerFactory.getLogger(ProbeServer.class);
  private final VideoMetaController videoMetaController;


  public static void main(String[] args) {
    new ProbeServer().configureAndStart();
  }

  public ProbeServer() {
    initServer();
    videoMetaController = new VideoMetaController();
  }

  public void configureAndStart() {
    redirect.get("/", "probe");
    redirect.get("/index", "/probe");
    get("/probe", (req, res) ->
        new ModelAndView(Collections.emptyMap(), "probe"), new ThymeleafTemplateEngine());
    post("/probe", "text/html",
        videoMetaController::processAnalyzeRequestHtml, new ThymeleafTemplateEngine());
    post("/probe",
        videoMetaController::processAnalyzeRequestJson, new JsonResponseTransformer());
  }


  private void initServer() {
// init resource dirs
    staticFiles.location("public");
    logger.info("Server successfully initialized");
  }
}