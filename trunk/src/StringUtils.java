import java.io.UnsupportedEncodingException;

public class StringUtils {
    
  static final byte[] HEX_CHAR_TABLE = {
    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
  };    

  public static String getHexString(byte[] raw) 
    throws UnsupportedEncodingException 
  {
    byte[] hex = new byte[2 * raw.length];
    int index = 0;

    for (byte b : raw) {
      int v = b & 0xFF;
      hex[index++] = HEX_CHAR_TABLE[v >>> 4];
      hex[index++] = HEX_CHAR_TABLE[v & 0xF];
    }
    return new String(hex, "ASCII");
  }

  public static void main(String args[]) throws Exception{
    byte[] byteArray = {
      (byte)255, (byte)254, (byte)253, 
      (byte)252, (byte)251, (byte)250
    };
    
    String hex = StringUtils.getHexString(byteArray);
    System.out.println(hex);
    byte[] back = hex.getBytes();
    StringUtils.printByteArray(byteArray);
    StringUtils.printByteArray(back);
    String direct = new String(byteArray);
    System.out.println(direct);
    StringUtils.printByteArray(direct.getBytes());
    
    /*
     * output :
     *   fffefdfcfbfa
     */
    
  }
  
  public static void printByteArray(byte[] a){
	  for(int i = 0; i < a.length; i++){
		  System.out.print(a[i] + ", ");
	  }
	  System.out.println();
  }
}
