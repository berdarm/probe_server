package probe.videometa;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Builder
@ToString
@Data
public class VideoStream {
  private double duration;
  private int width;
  private int height;
  private String codec;
  private String displayAspectRatio;
  private String avgFrameRate;
  private long bitrate;
  private long maxBitrate;
  private int channels;
  private int bitsPerSample;
  private String nalLength;
  private int hasBFrames;
  private long durationTs;
  private int level;
  private String pixFmt;
  private int sampleRate;
  private String sampleFormat;
  private String timeBase;
  private String isAvc;
  private double startTime;
  private long startPts;
}