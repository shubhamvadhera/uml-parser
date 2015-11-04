
public class Customer {
    private int balance;
    private void depositMoney() {
        AtmMachine atm = new AtmMachine();
        atm.insertCard("5458885452123659");
        atm.insertMoney(5000);
        atm.getReceipt();
}
    public void addBalance(int amount){
        balance+=amount;
        System.out.println("I am richer by "+amount);
    }
    
}
