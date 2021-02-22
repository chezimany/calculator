public class BadInputException extends Exception{

    public BadInputException(){}

    public BadInputException(String msg){
        System.err.println(msg);
    }
}
