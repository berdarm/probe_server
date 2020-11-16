package probe;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import probe.videometa.VideoMetadataClient;
import probe.videometa.VideoProbe;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class VideoMetaController {
  public static Logger logger = LoggerFactory.getLogger(VideoMetaController.class);

  private final Random random = new Random();
  private final File tempDir;


  public VideoMetaController() {
    tempDir = new File("temp");
    tempDir.mkdir();
  }

  public ModelAndView processAnalyzeRequestHtml(Request req, Response response) throws IOException, ServletException {
    logger.info("Received a file from a form submit to analyze");

    File tempFile = null;
    try {
      tempFile = File.createTempFile("tmp_probe_", String.valueOf(random.nextInt()), tempDir);

      req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/temp"));
      try (InputStream input = req.raw().getPart("video").getInputStream()) {
        Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }

      VideoProbe videoProbe = new VideoMetadataClient().getVideoMetadata(tempFile.toPath());
      final HashMap<Object, Object> model = new HashMap<>();
      model.put("response", new ObjectMapper().convertValue(videoProbe, Map.class));
      return new ModelAndView(model, "probe");
    } finally {
      if (tempFile != null) {
        tempFile.delete();
      }
    }
  }

  public VideoProbe processAnalyzeRequestJson(Request request, Response response) throws IOException {
    logger.info("Received a file to analyze");

    File tempFile = null;
    try {
      tempFile = File.createTempFile("tmp_probe_", String.valueOf(random.nextInt()), tempDir);
      try (InputStream input = new ByteArrayInputStream(request.bodyAsBytes())) {
        Files.copy(input, tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
      }
      return new VideoMetadataClient().getVideoMetadata(tempFile.toPath());
    } finally {
      if (tempFile != null) {
        tempFile.delete();
      }
    }

  }

}
