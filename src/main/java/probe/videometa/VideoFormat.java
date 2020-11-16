package probe.videometa;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class VideoFormat {
  private String longName;
  private double duration;
  private int streams;
  private long bitrate;
  private long size;
}