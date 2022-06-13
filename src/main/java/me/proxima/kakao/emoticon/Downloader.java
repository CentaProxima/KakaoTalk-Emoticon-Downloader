package me.proxima.kakao.emoticon;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

public class Downloader {
    public static void main(String[] args) throws Exception {
        BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in));
        System.out.print("Enter Path to Download: ");
        String _path = scanner.readLine();
        String path = _path.isEmpty() ? "." : _path;

        if(!path.equals(".")){
            File dir = new File(path);
            if(!dir.isDirectory()) dir.mkdirs();
        }

        int threadCount = 6;
        int totalCount = 100000;
        int downloadPerThread = totalCount/threadCount; //전체 다운로드 갯수 / 생성 스레드 수
        for(int i=0; i<threadCount; i++){
            DownloadThread thread = path == null
                    ? new DownloadThread(i*downloadPerThread, (i+1)*downloadPerThread, "webp")
                    : new DownloadThread(i*downloadPerThread, (i+1)*downloadPerThread, "webp", path);
            thread.start();
        }
    }
}
