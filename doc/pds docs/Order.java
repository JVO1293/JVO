import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Obaseki idemudia
 */

/**
 * 
 */
@SuppressWarnings("serial")
public class Order extends ArrayList<Food> {
private int orderNumber;
private Customers customer;
private String destination;
private int timeToDeliver = -1;
private static int orderCount=0;
private int orderStatus;

private Scanner scan=new Scanner(System.in);
	public Order(Customers customer) {
		setOrderNumber(orderCount);
		setCustomer(customer);
        setDestination(customer);
        setTimeToDeliver(destination);
        orderCount++;
        orderStatus=0;
	}
	public Order(int i,Customers customer) {
		setOrderNumber(i);
		setCustomer(customer);
        setDestination(customer);
        setTimeToDeliver(destination);
        orderStatus=0;
       
	}

	public void incrementStatus(){
		orderStatus++;
	}
	public int getOrderStatus(){
		return orderStatus;
	}
	
	public boolean complete(){
		int i =0;
		boolean cooked=true;
		for(i=0;i<this.size();i++){
			if(this.get(i).isCooked()==false){
				cooked=false;
			}
		}
		return cooked;
		
	}

	public void setOrderNumber(int orderNumber) {
		this.orderNumber = orderNumber;
	}

	public int getOrderNumber() {
		return orderNumber;
	}
	
	public ArrayList<Food> getFood(){
		return this;
	}

	public Customers getCustomer() {
		return customer;
	}

	public void setCustomer(Customers customer) {
		this.customer = customer;
	}
	
	public void setDestination(Customers c){
		if (c != null)
			this.destination = c.getDestination().trim();
    }
    
    public String getDestination(){
        return destination;
    }
    
    public int getDeliveryTime(){
        return timeToDeliver;
    }
    
    public double getTotal(){
    	double temp=0;
    	for(Food i:this){
    		temp+=i.getPrice();
    	}
    	return temp;
    }
    @Override
    public boolean equals(Object in){
    	if(((Order) in).getOrderNumber()==this.getOrderNumber()){
    		return true;
    	}else{
    		return false;
    	}
    }
	public Order createOrder(Customers customer,ArrayList<Food> foodMenu,int orderCount,int foodId){
		Order newOrder=new Order(customer);
		//Register.incrementOrderCount();
		int i =0;
		int input=0;
		while(true){
			System.out.println("Select a item to add to the order and press ENTER");
			for(i=0;i<foodMenu.size();i++){
				System.out.print(i+":");
				System.out.println(foodMenu.get(i).getFoodName());
			}
			System.out.println(i+":Finish order");
			input=Integer.parseInt(scan.nextLine());
			if(input==i){
				break;
			}else{
				Food toAdd = new Food(foodMenu.get(input).getFoodName(), foodMenu.get(input).getPrice(),
						foodMenu.get(input).getPrepTime(), foodMenu.get(input).getCookTime(), foodMenu.get(input).getOvenSpace(),
						foodMenu.get(input).getToppingPrice());
				//Register.incrementFoodId();
				newOrder.add(toAdd);
			}
		}
		return newOrder;
	}
	
   public String toString(){
	   String temp="";
	   for(Food f:this){
		   temp+=f+"\n";
	   }
	   return "Order Number: "+orderNumber;
   }
	
	public void setTimeToDeliver(String dest){
        //FileInputStream fstream = new FileInputStream("destinations.txt");
        //DataInputStream in = new DataInputStream(fstream);
        try{
            Scanner scan = new Scanner(new File("src/destinations.txt"));
            do {
                if (scan.next().trim().toLowerCase().equals(dest.trim().toLowerCase())){
                    timeToDeliver = scan.nextInt();
                    break;
                } else scan.nextLine();
            } while(scan.hasNext());
            if (timeToDeliver == -1){
                System.err.println("Not a valid location.");
            }
        } catch (Exception e) {
            System.err.println("X");
        }
    }

}


//list of food objects
//completed boolean for full order