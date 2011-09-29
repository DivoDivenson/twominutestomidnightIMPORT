package docketSystem;

import java.awt.event.WindowAdapter;

import javax.swing.Action;

public class EditDocForm extends docketSystem.NewDocForm {
	String[] args;
	public EditDocForm(Interface interRef, String[] args){
		super(interRef);
		//System.out.println(args.length);
		this.args = args;
		init();
	}
	
	private void init(){
		deliver.setText(args[2]);
		collect.setText(args[3]);
		description.setText(args[4]);
		seal.setText(args[5]);
		customer.setText(args[6]);
		equip.setText(args[7]);
		returnEmpty.setText(args[8]);
		//Well that took fooking ages, and I'm not even going to finish it proper yet
		if(args[9] != null){
			checkListener.simulateAction();
			name.setText(args[9]);
			un.setText(args[10]);
		}
		size.setText(args[11]);
		weight.setText(args[12]);
		berth.setText(args[13]);
		/*this.addWindowListener(new WindowAdapter(){
			public void windowClosing(java.awt.event.WindowEvent e){
				cancelButtonActionPerformed();
				
			}
		});*/
	}
	
	protected void cancelButtonActionPerformed(){
		if(save){
			interfaceRef.updateActionPreformed(args[0]);
		}
		this.dispose();
	}
	
	
	
	//Override print to update here

}
