package org.rossedth.adaptive_fsm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import org.jeasy.states.api.AbstractEvent;
import org.rossedth.adaptive_fsm.RecognizerFSM.IListener;
import org.rossedth.adaptive_logic.Memory;
import org.rossedth.adaptive_logic.Monitor;

public class Monitor_FSM extends Monitor {
	private IListener listener;
	
	public Monitor_FSM() {};
	public Monitor_FSM (Memory mem) {
		super(mem);
	}
	
	public void sense() {
		System.out.println("Establishing listening from Monitor");
		final RecognizerFSM sys_U=(RecognizerFSM)this.getSysU();
		
    	listener=new RecognizerFSM.IListener() {
			
			@Override
			public void onInvalidEntry(AbstractEvent event) {
				System.out.println("Invalid input for current state "+ sys_U.getFSM().getCurrentState().getName() +" from Monitor");
				//saveData(new FSMData(sys_U.getFSM().getCurrentState(),event));
				//sendData();
			}
			
			@Override
			public void onUnidentifiedEntry(AbstractEvent event) {
				System.out.println("Unidentified input " +event.getName()+" detected at state "+ sys_U.getFSM().getCurrentState().getName()+" reported from Monitor");
//				if (event.getName().equalsIgnoreCase("K")) {
//					int delay=5000;
//					onTimer(delay);
//				}
				saveData(new FSMData(sys_U.getFSM().getCurrentState(),event));
				sendData();
			}
			
			public void onTimer(final int delay) {
				Timer timer=sys_U.getTimer();
				if (timer==null){
					timer=new Timer(delay,null);
					sys_U.setTimer(timer);
				}
				timer.setDelay(delay);
				timer.addActionListener(new ActionListener() {
				    public void actionPerformed(ActionEvent evt) {
				    	System.out.println(delay+" seconds have passed since K was detected as input");
				    	sys_U.getTimer().stop();
						saveData(new FSMData(sys_U.getFSM().getCurrentState(),new NNEvent("TimeEvent") {
						}));
						sendData();
				    }
				});
				
				timer.start();
			}
		};
		
		sys_U.setListener(listener);
	}

}
