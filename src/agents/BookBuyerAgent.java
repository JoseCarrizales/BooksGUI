/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package agents;

import jade.core.Agent;
import behaviours.RequestPerformer;
import jade.core.AID;
import jade.core.behaviours.*;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;

import gui.BookBuyerGui;

public class BookBuyerAgent extends Agent {
  private String bookTitle;
  private AID[] sellerAgents;
  private int ticker_timer = 10000;
  private BookBuyerAgent this_agent = this;
  private BookBuyerGui gui;
  
  protected void setup(){
      gui = new BookBuyerGui(this);
      gui.showGui();
	  
	  DFAgentDescription dfd = new DFAgentDescription();
	  dfd.setName(getAID());
	  
	  ServiceDescription sd = new ServiceDescription();
	  sd.setType("book-buying");
	  sd.setName("book-trading2");
	  dfd.addServices(sd);
  }
  
  public void sendData(final String title){
      System.out.println("Buyer agent " + getAID().getName() + " is ready");
      if(title != null && title.length() > 0){
        bookTitle = (String)title;
        System.out.println("Book: " + bookTitle);
        
        addBehaviour(new TickerBehaviour(this, ticker_timer) {
            public void onTick() {
                System.out.println("Trying to buy " + bookTitle);
          
                DFAgentDescription template = new DFAgentDescription();
                ServiceDescription sd = new ServiceDescription();
                sd.setType("book-selling");
                template.addServices(sd);
          
                try {
                    DFAgentDescription[] result = DFService.search(myAgent, template);
                    System.out.println("Found the following seller agents:");
                    sellerAgents = new AID[result.length];
                    for(int i = 0; i < result.length; i++) {
                        sellerAgents[i] = result[i].getName();
                        System.out.println(sellerAgents[i].getName());
                    }
            
                }catch(FIPAException fe) {
                    fe.printStackTrace();
                }
          
            myAgent.addBehaviour(new RequestPerformer(this_agent));
        }
    }); 
    } else {
        System.out.println("No target book title specified");
        doDelete();
    }
  }
    protected void takeDown() {
        System.out.println("Buyer agent " + getAID().getName() + " terminating");
    }
  
    public AID[] getSellerAgents() {
        return sellerAgents;
    }
  
    public String getBookTitle() {
        return bookTitle;
    }
}
  