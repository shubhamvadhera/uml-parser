
public class Bank {
    AtmMachine atm = new AtmMachine();
    public void accessAccount(String cardNumber){
        System.out.println("Card Number is "+cardNumber);
        atm.openMoneySlot();
    }
    
    public void depMoney(int i){
        System.out.println("Bank receives money" + i);
    }
    
}
