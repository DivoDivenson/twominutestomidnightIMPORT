package Encryption;



public class main {
	public static void main (String args[]){
			String password = "password";
			DesEncrypter encrypter = new DesEncrypter(9);
			String encrypted = encrypter.encrypt("test");
			System.out.println(encrypted);
			String decrypted = encrypter.decrypt(encrypted);
			System.out.println(decrypted);
		 
	}

}
