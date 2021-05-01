package me.proxima.kakao.emoticon;

import java.io.File;
import java.util.Scanner;

public class Downloader {
    public static void main(String[] args) throws Exception {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Path to Download: ");
        String path_ = scanner.nextLine();
        String path = path_.isEmpty() ? "." : path_;

        if(!path.equals(".")){
            File dir = new File(path);
            if(!dir.isDirectory()) dir.mkdirs();
        }

        int threadCount = 10;
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
