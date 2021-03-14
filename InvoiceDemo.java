interface Payable {
    double getPaymentAmount();
}

abstract class Employee implements Payable {
    String name;
    String surname;
    String SSN;
    
    public Employee(String fn, String ln, String soc){
        name = fn;
        surname = ln;
        SSN = soc;
    }
    
    public String toString(){
        return String.format("%s %s\nSocial SN : %s",name,surname,SSN);
    }
}

class Invoice implements Payable {
    String partNumber;
    String partDescription;
    int quantity;
    double pricePerItem;
    
    public Invoice(String pn, String pd, int quan, double ppi) {
        partNumber = pn;
        partDescription = pd;
        quantity = quan;
        pricePerItem = ppi;
    }
    
    public double getPaymentAmount(){
        return quantity * pricePerItem;
    }

    
    public String toString(){
        return String.format("Invoice:\nPart number : %s (%s)\nQuantity : %d\nPrice per item : $%,.2f",partNumber,partDescription,quantity,pricePerItem);
    }
}

class SalariedEmployee extends Employee {
    double weeklySalary;
    
    public SalariedEmployee(String fn, String ln, String soc, double salary){
        super(fn,ln,soc);
        weeklySalary = salary;
    }
    
    public double getPaymentAmount(){
        return weeklySalary;
    }
    
    public String toString(){
        return String.format("Salaried Employee : %s\nWeekly salary %.2f",super.toString(),getPaymentAmount());
    }
}

public class InvoiceDemo {
    public static void main(String[] args){
        //int n = 4;
        Payable payabaleObjects[] = new Payable[4];
        
        double total = 0;
        payabaleObjects[0] = new Invoice("111","thingamagic",3,100.00);
        payabaleObjects[1] = new Invoice("112","thingbmagic",4,100.00);
        payabaleObjects[2] = new Invoice("113","thingcmagic",5,100.00);
        
        payabaleObjects[3] = new SalariedEmployee("Russell","Saerang","123-AAA-ZZZ",10000.00);
        
        System.out.println("Polymorphism go!:\n");
        for (int i=0;i<4;i++){
            System.out.printf("%s\nPayment due : $%,.2f\n\n",payabaleObjects[i].toString(),payabaleObjects[i].getPaymentAmount());
            total += payabaleObjects[i].getPaymentAmount();
        }
        System.out.printf("Total = $%,.2f\n\n",total);
    }
}

