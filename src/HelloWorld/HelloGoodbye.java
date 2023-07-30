package HelloWorld;



public class HelloGoodbye {
    public static void main (String[] args){
        StringBuilder sb = new StringBuilder();
        String name1 = args[0];
        var name2 = args[1];
        sb.append("Hello, ");
        sb.append(name1);
        sb.append(",");
        sb.append(name2);
        System.out.println(sb.toString());
    }
}
