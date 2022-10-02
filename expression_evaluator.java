import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

interface IExpressionEvaluator {
  
/**
* Takes a symbolic/numeric infix expression as input and converts it to
* postfix notation. There is no assumption on spaces between terms or the
* length of the term (e.g., two digits symbolic or numeric term)
*
* @param expression infix expression
* @return postfix expression
*/
  
public String infixToPostfix(String expression);
  
  
/**
* Evaluate a postfix numeric expression, with a single space separator
* @param expression postfix expression
* @return the expression evaluated value
*/
  
public int evaluate(String expression);

}

 class Node {
    Object data;
    Node next;
}

 class Stack {
    Node top = null ;
    public void push(Object element){
        Node n=new Node();
        n.data=element;
        n.next=top;
        top=n;
    }
    public void display(){
        Node n=new Node();
        n=top;
        System.out.print("[");
            while(n!=null)
            { if(n.next!=null)
            {
                System.out.print(n.data+", ");
            }
            else {
                System.out.print(n.data);
            }
                n=n.next;}
        System.out.print("]");
    }
        public Object pop(){
          Node n=new Node();
            if(top==null){
              return -99;
            }
            else{
           n=top;
            top=top.next;
            return n.data;}
        }
        public Object peek(){
            Node n=new Node();
            if(!isEmpty()){
            n=top;
            return n.data;}
            else{
                return -99;
            }
        }
        public boolean isEmpty(){
          return size()==0;
        }
        public int size(){
            int size=1;
            Node n=new Node();
            n=top;
            if(top==null){
                return 0;
            }
            while(n.next!=null){
                n=n.next;
                size++;
            }
            return size;
        }
}



public class Evaluator implements IExpressionEvaluator {
  static String a,b,c;
  Stack stack=new Stack();
    public static void main(String[] args) {
            Evaluator eval =new Evaluator();
        String infix_expression,postfix_expression;
        Scanner scanner =new Scanner(System.in);
       int answer;
        
       infix_expression=scanner.nextLine();
        
       a=scanner.nextLine().replace("a=","");
       b=scanner.nextLine().replace("b=","");
       c=scanner.nextLine().replace("c=","");
        if(eval.error_checker(infix_expression)==0){
            System.out.print("Error");
        }
        else{
        postfix_expression=eval.infixToPostfix(infix_expression);
        System.out.println(postfix_expression);
       answer=eval.evaluate(postfix_expression);
             
       System.out.print(answer);}
    }
    public String infixToPostfix(String expression) {
        String postfix="";
        
         for(int i=0;i<expression.length();i++) {
             if(expression.charAt(i)=='(') 
                 stack.push(expression.charAt(i));
             else if(expression.charAt(i)==')') {
                 while((char)stack.peek()!='(') {
                     postfix+=(char)stack.pop();
                 }
                 stack.pop();
                     }
            else if(expression.charAt(i)=='a'||expression.charAt(i)=='b'||expression.charAt(i)=='c') {
                postfix+=expression.charAt(i);
            }
              else if((expression.charAt(i)=='-'  && i!=expression.length()-1 && expression.charAt(i+1)=='-' && i==0)
                        ||(expression.charAt(i)=='-'  && i!=expression.length()-1 && expression.charAt(i+1)=='-' && i!=0 &&(expression.charAt(i-1)!='a'
                        && expression.charAt(i-1)!='b'&& expression.charAt(i-1)!='c' )))
                       
                {
                    i++;
                }
                else if(expression.charAt(i)=='+'  && i!=expression.length()-1 && expression.charAt(i+1)=='+' && i==0 )
                {
                    i++;
                }
                else if(expression.charAt(i)=='-'  && i!=expression.length()-1 && expression.charAt(i+1)=='-'  && i!=0 && (expression.charAt(i-1)=='a'
                        || expression.charAt(i-1)=='b'|| expression.charAt(i-1)=='c'))
                {
                    expression=expression.substring(0, i)+'+'+expression.substring(i+2);
                    i--;
                }
                else if((expression.charAt(i)=='-'  && i!=expression.length()-1 && expression.charAt(i+1)=='+'))
                 {
                    expression.replace("-+", "-");
                   
                }
                else if(expression.charAt(i)=='+'  && i!=expression.length()-1 && expression.charAt(i+1)=='-')
                {
                    expression.replace("+-", "-");
                   
                }
          
            else if(expression.charAt(i)=='+'||expression.charAt(i)=='*'||expression.charAt(i)=='^'||expression.charAt(i)=='-'||expression.charAt(i)=='/'){
                if(!stack.isEmpty()&&(precedence((char)stack.peek())>precedence(expression.charAt(i)))) {
                    postfix+=(char)stack.pop();
                    stack.push(expression.charAt(i));
                }
                else    if(!stack.isEmpty()&&(precedence((char)stack.peek())==precedence(expression.charAt(i)))) {
                    postfix+=(char)stack.pop();
                    stack.push(expression.charAt(i));
                }
                else    if(!stack.isEmpty()&&(precedence((char)stack.peek())<precedence(expression.charAt(i)))) {
                    stack.push(expression.charAt(i));
                }
                else  {
                    stack.push(expression.charAt(i));
                }
            }
             
         }
         while(!stack.isEmpty()){
                postfix+=(char)stack.pop();
                }
         
        return postfix; 
    
    }
    public int error_checker(String expression){
        int last_index =expression.length()-1;
        if(expression.charAt(last_index)=='+'||expression.charAt(last_index)=='-'||expression.charAt(last_index)=='*'||expression.charAt(last_index)=='/'||expression.charAt(last_index)=='^'||expression.charAt(0)=='^'||expression.charAt(0)=='*'||expression.charAt(0)=='/'){
            return 0;
        }
        Stack temp =new Stack();
        for(int i=0;i<expression.length();i++){
            if(expression.charAt(i)=='('||expression.charAt(i)=='['||expression.charAt(i)=='{')
                temp.push(expression.charAt(i));
                else if(expression.charAt(i)==')'||expression.charAt(i)==']'||expression.charAt(i)=='}'){
                   if(temp.isEmpty()){
                       return 0;}
                    else if((char)temp.peek()!='('){
                        return 0;
                    }
                    else if((char)temp.peek()=='(')
                        temp.pop();
                    else if((expression.charAt(i-1)=='+' ||expression.charAt(i-1)=='-'
                     || expression.charAt(i-1)=='/' || expression.charAt(i-1)=='*'
                     || expression.charAt(i-1)=='^')&&expression.charAt(i)==')'){
                        return 0;
                    }
                }
        }
        if(temp.isEmpty())
            return 1;
        else
            return 0;
    }
    public int evaluate(String expression){
        int result=0,x,y;
        String[] s = expression.replaceAll("a","a,")
                .replaceAll("b","b,").replaceAll("c","c,").replaceAll("\\+","+,").replaceAll("-","-,")
                .replaceAll("/","/,").replaceAll("\\*","*,").replaceAll("\\^","^,").split(",");
        for(int i=0;i<s.length;i++)
        { if(s[i].equals("a")) { s[i]=a;
        }
        else if(s[i].equals("b")) {s[i]=b;
        }
        else if(s[i].equals("c")) {s[i]=c;
        }
        }
     for(int i=0;i<expression.length();i++){
         if(expression.charAt(i)=='a'||expression.charAt(i)=='b'||expression.charAt(i)=='c')
             stack.push(Integer.parseInt(s[i]));
         else {
             x=(int)stack.pop();
             if(!stack.isEmpty())
                 y=(int)stack.pop();
             else{y=0;}
             if(expression.charAt(i)=='+'){
                 result =y+x;
                 stack.push(result);
             }
             else if(expression.charAt(i)=='*'){                 result=y*x;
                 stack.push(result);}
             else if(expression.charAt(i)=='/'){                 result=y/x;
                 stack.push(result);}
             else if(expression.charAt(i)=='^'){                 result=(int)Math.pow(y,x);
                 stack.push(result);}
             else if(expression.charAt(i)=='-'){                 result =y-x;
                 stack.push(result);}    
         }
     }
        result=(int)stack.pop();
     return result;
    }
    public int precedence(char t) {
        if(t=='^')
            return 3;
        else if(t=='*'||t=='/')
            return 2;
        else if(t=='+'||t=='-')
            return 1;
        else
            return 0;
    }
r}