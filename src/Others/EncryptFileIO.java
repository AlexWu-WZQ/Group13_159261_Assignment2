package Others;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * @Author : Alex Wu 20007895, Jiang Yu 20007896, Mengyao Jia 20008017, Zhibo Zhang 20007864
 * @Description :   Encrypt the ranking content
 */


public class EncryptFileIO {

    public void set(String filename,String str) throws IOException {
        byte[] b=str.getBytes();
        //Encrypt the ranking content
        for(int i=0;i<b.length;i++){
            b[i]+=(byte) i;
        }
        FileOutputStream outputStream=new FileOutputStream(filename);
        outputStream.write(b);
        outputStream.close();
    }

    public String load(String filename) throws Exception{

        FileInputStream fileInputStream=new FileInputStream(filename);
        int b;
        int i=0;
        byte[] bytes=new byte[1024];
        // decrypt the data
        while ((b=fileInputStream.read())!=-1){
            b=(byte)b-(byte)i;
            bytes[i]=(byte) b;
            i++;
        }
        String str=new String(bytes,0,i);
        fileInputStream.close();
        return str;
    }
}
