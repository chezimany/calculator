import com.google.gson.Gson;
import org.json.simple.JSONObject;

import java.util.HashSet;
import java.util.Map;

public class Calculator {
    /**
     * a hash set to contain all types of operators
     */
    private final HashSet<String> operators;

    public Calculator(){
        this.operators = new HashSet<>();
        this.operators.add("+");
        this.operators.add("-");
        this.operators.add("=");
        this.operators.add("*");
        this.operators.add("/");
    }

    /**
     * deals with digit as an input.
     * @param jsonMap - map to update
     * @param input - input to update the map.
     * @return - a string representation of the updated map.
     */
    private String insertDigit(Map<String, String> jsonMap, char input){
        if (!jsonMap.get("lastInsertionWasOp").equals("true")){     // if last insertion wasn't an operator
            jsonMap.put("display", jsonMap.get("display") + input);
        }
        else {
            jsonMap.put("display", String.valueOf(input));
            jsonMap.put("lastInsertionWasOp", "false");
        }
        return new JSONObject(jsonMap).toJSONString();
    }

    /**
     * deals with operator as an input
     * @param jsonMap - a map to be updated.
     * @param operator - an operator to update the map by.
     * @return - updated map.
     */
    private String insertOperator(Map<String, String> jsonMap, String operator) throws BadInputException{
        if (jsonMap.get("lastInsertionWasOp").equals("true")){
            throw new BadInputException("Inserted two operators in a row.");
        }
        if (jsonMap.get("result") != null){   // it is not the first operator given by the user
            double currentVal = Double.parseDouble(jsonMap.get("result"));
            double displayVal = Double.parseDouble(jsonMap.get("display"));
            switch (jsonMap.get("operator")){
                case "+":
                    jsonMap.put("result", String.valueOf(currentVal + displayVal));
                    break;
                case "*":
                    jsonMap.put("result", String.valueOf(currentVal * displayVal));
                    break;
                case "-":
                    jsonMap.put("result", String.valueOf(currentVal - displayVal));
                    break;
                case "/":
                    jsonMap.put("result", String.valueOf(currentVal / displayVal));
                    break;
            }
        }
        else {
            jsonMap.put("result", jsonMap.get("display"));
        }
        jsonMap.put("operator", operator);
        if (operator.equals("=")){
            jsonMap.put("display", jsonMap.get("result"));
        }
        jsonMap.put("lastInsertionWasOp", "true");
        return new JSONObject(jsonMap).toJSONString();

    }

    /**
     * a function representing a calculator.
     * @param jsonState - be a legal JSON object (e.g. ‘{...}’), which will always contain the field “display”.
     * This field will be the string that the calculator will display.
     * @param input -the next character that the user inputs.
     * @return - a string which is also a legal JSON object.
     * @throws BadInputException
     */
    public String calculateNextState(String jsonState, String input) throws BadInputException {
        if (input.length() > 1 || (!this.operators.contains(input) && !Character.isDigit(input.charAt(0)) && !input.equals("."))){
            throw new BadInputException("input should be a digit, '.', or a mathematical operator");
        }

        if (input.isEmpty()){   // if input is a string, commit no changes.
            return jsonState;
        }

       if (jsonState == null){
           if (!Character.isDigit(input.charAt(0))){
               throw new BadInputException("first input has to be a digit.");
           }
           JSONObject jsonObject = new JSONObject();
           jsonObject.put("display", input);
           jsonObject.put("operator", null);
           jsonObject.put("result", null);
           jsonObject.put("lastInsertionWasOp", "false");
           return jsonObject.toJSONString();
       }
       Map<String, String> jsonMap = new Gson().fromJson(jsonState, Map.class);

       if (Character.isDigit(input.charAt(0)) || input.equals(".")){
           return this.insertDigit(jsonMap, input.charAt(0));
       }
       return this.insertOperator(jsonMap, input);
    }
}