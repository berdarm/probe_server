package probe;

import com.google.gson.Gson;

import spark.ResponseTransformer;

class JsonResponseTransformer implements ResponseTransformer {

  private final Gson gson = new Gson();

  @Override
  public String render(Object model) {
    return gson.toJson(model);
  }

}