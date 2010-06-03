public class Coverage {
  public void m1() {
    if (false) {
      System.out.println("Never");
    } else {
      System.out.println("Once");
    }
  }
}
