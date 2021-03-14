import java.util.Scanner;

class Gadgets {
    // instance variables
    String brand;
    String serie;
    double price;
    
    // default constructor
    public Gadgets () {
        brand = "-";
        serie = "-";
        price = 0;
    }    
    
    // parameterized constructor
    public Gadgets (String brand, String serie, double price) {
        this.brand = brand;
        this.serie = serie;
        this.price = price;
    }    

    // setter methods
    public void setBrand (String brand) {
        this.brand = brand;
    }

    public void setSerie (String serie) {
        this.serie = serie;
    }

    public void setPrice (double price) {
        this.price = price;
    }
    
    // getter methods
    public String getBrand () {
        return brand;
    }

    public String getSerie () {
        return serie;
    }

    public double getPrice () {
        return price;
    }
    
    public void printProduct() {
        System.out.printf ("%-15s %-20s %8.2f\n", brand, serie, price);
    }
}

public class GadgetsDemo {
    // method

    public static void main (String[] args) {
        Scanner input = new Scanner(System.in);
        String brand, serie;
        double price;

        // Object obj1 (Not recommended)
        Gadgets obj1 = new Gadgets();
        obj1.brand = "Apple";
        obj1.serie = "iPhone 11 Plus";
        obj1.price = 1500;

        // Object obj2
        Gadgets obj2 = new Gadgets("Samsung", "Galaxy S10 Plus", 1000);		

        // Object obj3
        brand = "Huawei";
        serie = "P10";
        price = 1200;
        Gadgets obj3 = new Gadgets(brand, serie, price);		

        // Object obj4 (Encapsulation)
        Gadgets obj4 = new Gadgets();	
        System.out.print("Brand   : ");
        brand = input.nextLine();
        System.out.print("Serie   : ");
        serie = input.nextLine();
        System.out.print("Price $ : ");
        price = input.nextDouble();

        obj4.setBrand (brand);
        obj4.setSerie (serie);
        obj4.setPrice (price);

        System.out.println("\nProduct List");
        obj1.printProduct();
        obj2.printProduct();
        obj3.printProduct();
        System.out.printf("%-15s %-20s %8.2f \n", obj4.getBrand(), obj4.getSerie(), obj4.getPrice());
    }
}