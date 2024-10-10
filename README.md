# YoutubeStream
- gets video data details for Youtube via a link or video id

## Usage
- Fetch data by id
```kotlin

    // https://www.youtube.com/watch?v=0vxOhd4qlnA where id -> 0vxOhd4qlnA
    YoutubeStream.getVideoDetailsById(id = "0vxOhd4qlnA")
```
- Fetch data by link
```kotlin
    // where link is https://www.youtube.com/watch?v=0vxOhd4qlnA
    YoutubeStream.getVideoDetailsByLink(link = "https://www.youtube.com/watch?v=0vxOhd4qlnA")
```