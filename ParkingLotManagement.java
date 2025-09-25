import java.util.*;
import java.time.*;

class ParkedVehicle {
    String number;
    String kind;    
    LocalDateTime inTime;
    LocalDateTime outTime;
    double bill;

    ParkedVehicle(String number, String kind) {
        this.number = number;
        this.kind = kind;
        this.inTime = LocalDateTime.now();
    }

    void leaveLot() {
        outTime = LocalDateTime.now();
        bill = computeBill();
    }

    private double computeBill() {
        long mins = Duration.between(inTime, outTime).toMinutes();
        long hrs  = Math.max(1,(mins+59)/60);
        double rate = kind.equalsIgnoreCase("car") ? 50 : 20;
        return hrs * rate;
    }

    public String toString() {
        String state = (outTime==null) ? "IN" : "OUT";
        return String.format("%-10s %-5s Entry:%s Exit:%s Status:%s Charge:₹%.2f",
                number, kind,
                inTime.toLocalTime().withSecond(0).withNano(0),
                (outTime==null ? "--" : outTime.toLocalTime().withSecond(0).withNano(0)),
                state, bill);
    }
}

class ParkingArea {
    int maxSlots;
    List<ParkedVehicle> inside = new ArrayList<>();
    List<ParkedVehicle> record = new ArrayList<>();

    ParkingArea(int maxSlots){
        this.maxSlots = maxSlots;
    }

    void addVehicle(String num,String kind){
        if(inside.size()>=maxSlots){
            System.out.println("Parking Full. No space left.");
            return;
        }
        ParkedVehicle v = new ParkedVehicle(num,kind);
        inside.add(v);
        record.add(v);
        System.out.println("Vehicle "+num+" entered at "+v.inTime.toLocalTime().withSecond(0).withNano(0));
    }

    void removeVehicle(String num){
        for(Iterator<ParkedVehicle> it=inside.iterator(); it.hasNext();){
            ParkedVehicle v = it.next();
            if(v.number.equalsIgnoreCase(num)){
                v.leaveLot();
                it.remove();
                System.out.println("Vehicle "+num+" exited at "+v.outTime.toLocalTime().withSecond(0).withNano(0));
                System.out.println("Charge: ₹"+v.bill);
                return;
            }
        }
        System.out.println("Vehicle not found.");
    }

    void freeSlots(){
        System.out.println("Total:"+maxSlots+
                " Occupied:"+inside.size()+
                " Free:"+(maxSlots - inside.size()));
    }

    void showInside(){
        if(inside.isEmpty()){
            System.out.println("No vehicles currently parked.");
            return;
        }
        System.out.println("Vehicles inside:");
        for(ParkedVehicle v: inside) System.out.println(v);
    }

    void showHistory(){
        if(record.isEmpty()){
            System.out.println("No entries yet.");
            return;
        }
        System.out.println("Complete record:");
        for(ParkedVehicle v: record) System.out.println(v);
    }
}

public class ParkingLotManagement {
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
        System.out.print("Enter total slots: ");
        int slots = sc.nextInt(); sc.nextLine();
        ParkingArea lot = new ParkingArea(slots);

        int choice;
        do{
            System.out.println("\n1.Entry  2.Exit  3.Free Slots  4.Current  5.History  0.Quit");
            System.out.print("Select: ");
            while(!sc.hasNextInt()){ sc.next(); System.out.print("Number please: "); }
            choice = sc.nextInt(); sc.nextLine();

            switch(choice){
                case 1 -> {
                    System.out.print("Vehicle Number: ");
                    String num = sc.nextLine();
                    System.out.print("Type (Car/Bike): ");
                    String kind = sc.nextLine();
                    lot.addVehicle(num,kind);
                }
                case 2 -> {
                    System.out.print("Vehicle Number: ");
                    String num = sc.nextLine();
                    lot.removeVehicle(num);
                }
                case 3 -> lot.freeSlots();
                case 4 -> lot.showInside();
                case 5 -> lot.showHistory();
                case 0 -> System.out.println("Goodbye.");
                default -> System.out.println("Invalid option.");
            }
        }while(choice!=0);
    }
}
