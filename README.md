# YoutubeStream
- gets video data details for Youtube via a link

## Initialization
```kotlin
    YoutubeStream.init(context)
```

## Usage
- Fetch data by id
```kotlin

    // https://www.youtube.com/watch?v=0vxOhd4qlnA where id -> 0vxOhd4qlnA
    YoutubeStream.getVideoDetailsById(id = "0vxOhd4qlnA")
    YoutubeStream.getVideoDataById(id = "0vxOhd4qlnA")

```
- Fetch data by link
```kotlin
    // https://www.youtube.com/watch?v=0vxOhd4qlnA
    YoutubeStream.getVideoDetailsById(link = "https://www.youtube.com/watch?v=0vxOhd4qlnA")
    YoutubeStream.getVideoDataById(link = "https://www.youtube.com/watch?v=0vxOhd4qlnA")
```