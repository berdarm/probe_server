package probe.videometa;

import java.util.List;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class VideoProbe {
  private VideoFormat format;
  private List<VideoStream> streams;
  private String error;
}