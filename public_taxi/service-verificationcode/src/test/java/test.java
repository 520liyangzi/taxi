public class test {

  public static void main(String[] args) {
    double mathRandom = (Math.random()*9+1)*(Math.pow(10,6-1));
    int res = (int) mathRandom;
    System.out.println(mathRandom);
    System.out.println(res);
  }
}
