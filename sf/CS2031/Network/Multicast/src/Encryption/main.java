package Encryption;



public class main {
	public static void main (String args[]){
			String password = "password";
			DesEncrypter encrypter = new DesEncrypter(9);
			String encrypted = encrypter.encrypt("i fucking hate this fucking shite");
			System.out.println(encrypted);
			String decrypted = encrypter.decrypt(encrypted);
			System.out.println(decrypted);
		 
	}

}
