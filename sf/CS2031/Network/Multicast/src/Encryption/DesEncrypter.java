package Encryption;
public class DesEncrypter {
	int key;


    public DesEncrypter(int key)  {
    	
            this.key = key;
    }

    public String encrypt(String str)  {
            byte[] encode = str.getBytes();
            for (int i=0; i<encode.length; i++){
            	encode[i]=(byte) (encode[i]+key);
            }
            String encoded = new String(encode);
            return encoded;
    }

    public String decrypt(String str)  {
    	byte[] decode = str.getBytes();
        for (int i=0; i<decode.length; i++){
        	decode[i]=(byte) (decode[i]-key);
        }
        String decoded = new String(decode);
        return decoded;
    }  
}
