
import java.util.LinkedList;
import java.util.Scanner; 

public class MoreEfficientIntegerList{

    public static void  main(String[] args){

        Scanner in = new Scanner(System.in); 
        int numCases = in.nextInt();
        in.nextLine();
            
        for (int i = 0; i < numCases; i++){
            String commands = in.nextLine();
            try {
            int numItems = in.nextInt();
            in.nextLine();
            } catch (Exception e){
                System.out.println("error");
            }
            String stringArray = in.nextLine();
            if (stringArray.equals("[]") && !commands.contains("D")) {
                System.out.println("[]");
                continue; 
            }
            LinkedList<Integer> ll = StringToLinkedList(stringArray);
            if (ll == null){
                System.out.println("error");
            } else {
                
                LinkedList<Integer> deletedLinkedList = reverseCountAndDelete(commands, ll); 
                if (deletedLinkedList == null){
                    System.out.println("error"); 
                } else {
                    if (toReverse(commands) == true){
                        LinkedList<Integer> finalLinkedList = deletedLinkedList.reversed();
                        String z = finalLinkedList.toString();
                        z = z.replaceAll("\\s", "");
                        System.out.println(z); 
                    }
                    if (toReverse(commands) == false){
                        String z = deletedLinkedList.toString();
                        z = z.replaceAll("\\s", "");
                        System.out.println(z);
                    }
                }

            }
        }
        in.close();

        }

    public static LinkedList<Integer> reverseCountAndDelete (String commands, LinkedList<Integer> orig){
        boolean reversed = false; 
        for (int i = 0; i < commands.length(); i ++){
            if (commands.charAt(i) == 'R'){
                    reversed = !reversed; 
            }
            else if (commands.charAt(i) == 'D'){
                try {
                    if (!reversed){
                    orig.removeFirst();
                    } else {
                    orig.removeLast();
                    }
                }catch (Exception e){
                    return null; 
                    }
                } else return null; 
                  
        }
        return orig;
        }

        public static LinkedList<Integer> StringToLinkedList (String read){
        String[] myArray = read.replaceAll("[\\[\\]\\s]", "").split(",");
        LinkedList<Integer> y = new LinkedList<>();
            if (myArray.length < 1){
                return y; 
            } else{
                try {
                    for (String s : myArray){
                        y.add(Integer.parseInt(s));
                    }
                    return y; 
                } catch (Exception e) {
                    return null; 
                    }
            }
                
    }

        public static boolean toReverse (String commands){
            boolean reverse = false; 
            for (int i = 0; i < commands.length(); i++){
                if (commands.charAt(i) == 'R'){
                    reverse = !reverse; 
                }
            }
            return reverse; 
        }
}