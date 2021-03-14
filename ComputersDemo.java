import java.util.Scanner;

class Computers {
    private String brand;
    private String serie;
    private double price;
    
    public Computers () {
        brand = "-";
        serie = "-";
        price = 0;
    }    
    
    public Computers (String brand, String serie, double price) {
        this.brand = brand;
        this.serie = serie;
        this.price = price;
    }    

    public void setBrand (String brand) {
        this.brand = brand;
    }

    public void setSerie (String serie) {
        this.serie = serie;
    }

    public void setPrice (double price) {
        this.price = price;
    }
    
    public String getBrand () {
        return brand;
    }

    public String getSerie () {
        return serie;
    }

    public double getPrice () {
        return price;
    }

	public void printComputer() {
	    System.out.printf ("%-15s %-20s $ %8.2f\n", brand, serie, price);
	}
}

class MobilePhone extends Computers {
    private String sensor;
    private String gsm;
    
    public MobilePhone () {
        super("-", "-", 0);
        this.sensor = "-";
        this.gsm = "-";
    }    
    
    public MobilePhone (String brand, String serie, double price, String sensor, String gsm) {
        super(brand, serie, price);
        this.sensor = sensor;
        this.gsm = gsm;
    }    
    
    public void setSensor (String sensor) {
        this.sensor = sensor;
    }
    
    public void setGSM (String gsm) {
        this.gsm = gsm;
    }
    
    public String getSensor () {
        return sensor;
    }
    
    public String getGSM () {
        return gsm;
    }    
    
    public void printComputer() {
	    System.out.printf ("%-15s %-20s $ %8.2f %s - %s\n", getBrand(), getSerie(), getPrice(), sensor, gsm);
	}
}

public class ComputersDemo {

    public static void main (String[] args) {
        Scanner input = new Scanner(System.in);

        System.out.println("Produc A (Mobile Phone)");

        MobilePhone obj1 = new MobilePhone();
        obj1.setBrand ("Apple");
        obj1.setSerie ("iPhone 11 Max");
        obj1.setPrice (1500);
        obj1.setSensor ("Face ID, accelerometer, gyro, proximity, compass, barometer");
        obj1.setGSM ("GSM / CDMA / HSPA / EVDO / LTE");

        obj1.printComputer();
        System.out.println("Sensor: " + obj1.getSensor());
        System.out.println("Technology: " + obj1.getGSM());

        Computers obj2 = new Computers ("ASUS", "TUF FX504GD", 800);		
        System.out.println("\nProduc B (Computer)");
        System.out.println("Brand: " + obj2.getBrand());
        System.out.println("Serie: " + obj2.getSerie());
        System.out.println("Price: $" + obj2.getPrice());
        obj2.printComputer();

        String brand = "Apel";
        String serie = "Malang";
        double price = 100;
        Computers obj3 = new MobilePhone (brand, serie, price, "Face Mask", "GSM");
        //obj3.setSensor ("Face Mask, Kompas, barometer");
        //obj3.setGSM ("GSM / CDMA / HSPA");
        obj3.printComputer();

	}
}