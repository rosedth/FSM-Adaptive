package org.rossedth.adaptive_fsm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.nio.file.Paths;

import javax.swing.Timer;

import org.jeasy.states.api.AbstractEvent;
import org.rossedth.adaptable_fsm.NNEvent;
import org.rossedth.adaptable_fsm.RecognizerFSM;
import org.rossedth.adaptive_logic.Memory;
import org.rossedth.adaptive_logic.Monitor;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class Monitor_FSM extends Monitor {
	private RecognizerFSM sys_U;
	public Monitor_FSM() {};
	public Monitor_FSM (Memory mem) {
		super(mem);
	}
	
	public void sense() {
		sys_U=(RecognizerFSM)this.getSysU();
		
		/*
		 * Setting a new listener for invalid entries on the underlying system (FSM)
		 */
		sys_U.setInvalidEntryListener(new RecognizerFSM.InvalidEntryListener() {
			
			@Override
			public void onInvalidEntry(AbstractEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Invalid input for current state "+ sys_U.getFSM().getCurrentState().getName() +" from InvalidEntryListener");				
				saveData(new FSMData(sys_U.getFSM().getCurrentState(),event));
				saveDataToFile();
				sendData();

			}
		});
		

		/*
		 * Setting a new listener for unidentified entries on the underlying system (FSM)
		 */		
		sys_U.setUndefinedEntryListener(new RecognizerFSM.UndefinedEntryListener() {
			
			@Override
			public void onUnidentifiedEntry(AbstractEvent event) {
				// TODO Auto-generated method stub
				System.out.println("Unidentified input " +event.getName()+" detected at state "+ sys_U.getFSM().getCurrentState().getName()+" reported from UnidentifiedEntryListener");
				saveData(new FSMData(sys_U.getFSM().getCurrentState(),event));
				saveDataToFile();
				sendData();
			}
		});
		
		/*
		 * Setting a new listener timing events on the underlying system (FSM)
		 */
		
		sys_U.setTimerListener(new RecognizerFSM.TimerListener() {
			
			@Override
			public void onTimer(int delay) {
				// TODO Auto-generated method stub
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
						saveDataToFile();
						sendData();
				    }
				});
				
				timer.start();
			}
		});
		
	}
	
	public void saveDataToFile() {
		 ObjectMapper mapper = new ObjectMapper();
		mapper.enable(SerializationFeature.INDENT_OUTPUT);
		 try {
			mapper.writeValue(Paths.get("Monitor_Data.json").toFile(), this.getData());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
