package me.proxima.kakao.emoticon.api;

import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;

import javax.net.ssl.SSLException;
import java.io.File;

public class EmoticonDownloader {
    private int emotId;
    private String type;
    private String path;
    private int retryCount = 0;

    public EmoticonDownloader(int emotId, String type, String path){
        this.emotId = emotId;
        this.type = type;
        this.path = path;
    }

    public void download() throws Exception{
        int count = 1;
        byte[] read = null;
        do{
            try{
                String url = String.format("https://item.kakaocdn.net/dw/"+emotId+".emot_%03d."+type, count);
                read = Jsoup.connect(url)
                        .ignoreContentType(true)
                        .execute().bodyAsBytes();

                EmoticonSaver saver = new EmoticonSaver(read, emotId, path, String.format(emotId+".emot_%03d."+type, count));
                saver.save();
                count++;
            }catch(HttpStatusException e){
                if(e.getStatusCode() == 404 && count <= 1){
                    File f = new File(String.valueOf(emotId));
                    if(f.isDirectory())
                        f.delete();
                }
                break;
            }catch(SSLException e){
                if(retryCount > 0){
                    e.printStackTrace();
                }else{
                    if(e.getMessage().equals("Connection reset")){
                        download();
                        retryCount += 1;
                    }
                }
            }
        }while(read != null && read.length > 0);
    }
}