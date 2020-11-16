## Video probe server

A server that returns metadata of the video

### How to start server

* Clone repository
* Build image
    * `cd <project path>`
    * `docker build -t probe_server .` 
* Run container `docker run -p 4567:4567 probe_server`

### How to use server

Go to `localhost:4567` upload the file and press "Analyze the video" button

If you prefer cli interface you can simply query server from the terminal
```bash
curl -XPOST --data-binary @test_data/Big_Buck_Bunny_720_10s_2MB.mp4 http://localhost:4567/probe
```


#### You can also run server locally, if you like to go deeper

Build the project
```bash
mvn clean compile assembly:single
```

Run executable 
```bash
java -jar target/ProbeServer-1.0-jar-with-dependencies.jar
```

Hint: You should have ffmpeg installed. Executable relies on `ffprobe` binary.
In case you have `ffmpeg` installed but the path's are not standard set `FFPROBE_BINARY` env variable.
`FFPROBE_BINARY=$(which ffprobe) java -jar target/ProbeServer-1.0-jar-with-dependencies.jar` 