package me.proxima.kakao.emoticon.api;

import java.io.*;

public class EmoticonSaver {

    public byte[] encrypted;
    public byte[] decrypted;
    public long emotId;
    public String filename;
    public File file;
    public BufferedOutputStream fileOutputStream;

    public EmoticonSaver(byte[] data, long emotId, String path, String filename) throws FileNotFoundException {
        this.encrypted = data;
        this.decrypted = data;
        this.emotId = emotId;
        this.filename = filename;
        this.file = new File(path+"/"+emotId+"/"+filename);
    }

    public void save() throws Exception{
        decryptAndWrite();
        fileOutputStream.close();
    }

    private void decryptAndWrite() throws IOException {
        if(!file.getParentFile().isDirectory())
            file.getParentFile().mkdirs();

        this.fileOutputStream = new BufferedOutputStream(new FileOutputStream(file));

        if(filename.endsWith(".webp")){
            byte[] header = new byte[128];
            System.arraycopy(encrypted, 0, header, 0, 128);
            byte[] decoded = EmoticonDecryptor.decryptBytes(header, "a271730728cbe141e47fd9d677e9006d");

            for(int i=0; i<decoded.length; i++) {
                decrypted[i] = decoded[i];
            }

            fileOutputStream.write(decrypted);
            fileOutputStream.flush();
        }else{
            fileOutputStream.write(encrypted);
            fileOutputStream.flush();
        }
    }
}
