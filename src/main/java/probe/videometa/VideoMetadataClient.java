package probe.videometa;

import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Optional;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegError;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;


public class VideoMetadataClient {

  public static final String DEFAULT_FFPROBE_PATH = "/usr/local/bin/ffprobe";

  public VideoProbe getVideoMetadata(Path filePath) throws IOException {
    Optional<FFprobe> ffProbeOptional = getFFProbe();
    final FFprobe ffProbe =
        ffProbeOptional.orElseThrow(() -> new RuntimeException("Failed to find `ffprobe` binary. Please specify correct path"));

    FFmpegProbeResult probeResult = ffProbe.probe(filePath.toString());

    FFmpegFormat ffmpegFormat = probeResult.getFormat();


    final VideoFormat format = VideoFormat.builder()
        .longName(ffmpegFormat.format_long_name)
        .duration(ffmpegFormat.duration)
        .bitrate(ffmpegFormat.bit_rate)
        .streams(ffmpegFormat.nb_streams)
        .size(ffmpegFormat.size)
        .build();


    final ArrayList<VideoStream> streams = new ArrayList<>();
    for (FFmpegStream stream : probeResult.getStreams()) {
      final VideoStream streamInfo = VideoStream.builder()
          .width(stream.width).height(stream.height)
          .duration(stream.duration).codec(stream.codec_long_name).bitrate(stream.bit_rate)
          .avgFrameRate(stream.avg_frame_rate.toProperString())
          .bitsPerSample(stream.bits_per_sample).channels(stream.channels)
          .displayAspectRatio(stream.display_aspect_ratio).durationTs(stream.duration_ts)
          .level(stream.level).maxBitrate(stream.max_bit_rate)
          .hasBFrames(stream.has_b_frames).nalLength(stream.nal_length_size)
          .pixFmt(stream.pix_fmt).sampleRate(stream.sample_rate).sampleFormat(stream.sample_fmt)
          .timeBase(stream.time_base.toProperString()).isAvc(stream.is_avc)
          .startTime(stream.start_time).startPts(stream.start_pts)
          .build();
      streams.add(streamInfo);
    }

    final VideoProbe.VideoProbeBuilder probeBuilder = VideoProbe.builder()
        .format(format)
        .streams(streams);

    final FFmpegError probeError = probeResult.getError();
    if (probeError != null) {
      probeBuilder.error(String.format("code: %d. %s", probeError.code, probeError.string));
    }

    return probeBuilder.build();
  }


  private Optional<FFprobe> getFFProbe() throws IOException {
    FFprobe ffprobe;
    try {
      String ffprobeBinaryPath = System.getenv("FFPROBE_BINARY");
      if (ffprobeBinaryPath != null) {
        ffprobe = new FFprobe(ffprobeBinaryPath);
        ffprobe.version();
        return Optional.of(ffprobe);
      }
    } catch (Exception e) {
    }
    try {
      ffprobe = new FFprobe(DEFAULT_FFPROBE_PATH);
      ffprobe.version();
      return Optional.of(ffprobe);
    } catch (Exception e) {
    }
    return Optional.empty();
  }

}
