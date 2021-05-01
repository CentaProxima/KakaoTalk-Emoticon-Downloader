package me.proxima.kakao.emoticon;

import me.proxima.kakao.emoticon.api.EmoticonDownloader;

public final class DownloadThread extends Thread{
    private int startId;
    private int endId;
    private String type;
    private String path;

    public DownloadThread(int startId, int endId, String type){
        this(startId, endId, type, ".");
    }

    public DownloadThread(int startId, int endId, String type, String path){
        this.startId = startId;
        this.endId = endId;
        this.type = type;
        this.path = path;
    }

    @Override
    public void run() {
        for(int i=startId; i < endId; i++){
            try {
                int emotId = 4400000 + i;
                System.out.println(emotId+" downloading...");
                EmoticonDownloader downloader = new EmoticonDownloader(emotId, type, path);
                downloader.download();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}