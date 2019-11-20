import java.awt.Color;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileReader;
import java.io.FileWriter;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LatestGUI extends JFrame implements ActionListener 
{
	private static final long serialVersionUID = 1L;
	JComboBox<String> toolsCombo = null, dataComboServ = null,dataComboEJ = null, pentaCombo = null;
	Container c;
	JPanel datameerPanel = null, pentahoPanel = null;
	JButton bDataGen = null, bPentaGen = null, bReset = null; 
	
	JSONParser parser=new JSONParser();
	
	LatestGUI()
	{
		//this.setBounds(20,20, 350, 300);
		setSize(400,400);
		setVisible(true);
		setTitle("Dragonfly DataFactory");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		c = getContentPane();
		
		toolsCombo = new JComboBox<String>();
		toolsCombo.addItem("Select");
		toolsCombo.addItem("Datameer");
		toolsCombo.addItem("Pentaho");
		toolsCombo.addActionListener(this);
	    
	    datameerPanel = new JPanel(new FlowLayout());
	    datameerPanel.setBackground(Color.blue);
	    pentahoPanel = new JPanel(new FlowLayout());
	    pentahoPanel.setBackground(Color.white);
	    datameerPanel.setVisible(false);
	    pentahoPanel.setVisible(false);
	    
	    dataComboServ = new JComboBox<String>();	    
	    dataComboServ.addItem("NOAA");
	    dataComboServ.addItem("Twitter");
	    
	    dataComboEJ = new JComboBox<String>(); 
	    dataComboEJ.setEnabled(false);	    
	    dataComboEJ.addItem("With Avro");
	    dataComboEJ.addItem("With out Avro");
	    
	    datameerPanel.add(dataComboServ);	    
	    datameerPanel.add(dataComboEJ);
	    bDataGen = new JButton("Generate Input JSON");
	    bDataGen.setEnabled(false);	    
	    datameerPanel.add(bDataGen);
	    
	    pentaCombo = new JComboBox<String>();	    
	    pentaCombo.addItem("Equal 35 Degree");
	    pentaCombo.addItem("Less than 40 Degree");
	    
	    pentahoPanel.add(pentaCombo);	    
	    bPentaGen = new JButton("Generate Input JSON");
	    bPentaGen.setEnabled(false);
	    pentahoPanel.add(bPentaGen);
	    
	    bReset = new JButton("Reset");
	    
	    dataComboServ.addActionListener(this);	    
	    dataComboEJ.addActionListener(this);
	    pentaCombo.addActionListener(this);
	    
	    bDataGen.addActionListener(this);
	    bPentaGen.addActionListener(this);
	    bReset.addActionListener(this);
	    
	    c.setLayout(new FlowLayout());
	    c.add(toolsCombo);
	    c.add(datameerPanel);
	    c.add(pentahoPanel);
	    c.add(bReset);
	    pack();
	    validate();
	}
	
	@SuppressWarnings("unchecked")
	public void actionPerformed(ActionEvent ae) 
	{
		if(ae.getSource() == toolsCombo)
		{
			System.out.println("Selected index=" + toolsCombo.getSelectedIndex() + " Selected item=" + toolsCombo.getSelectedItem());
			if("Datameer".equals(toolsCombo.getSelectedItem()))
			{
				datameerPanel.setVisible(true);
				pentahoPanel.setVisible(false);				
			}
			else if("Pentaho".equals(toolsCombo.getSelectedItem()))
			{
				datameerPanel.setVisible(false);
				pentahoPanel.setVisible(true);
			}			
			else if("Select".equals(toolsCombo.getSelectedItem()))
			{
				datameerPanel.setVisible(false);
				pentahoPanel.setVisible(false);
			}
		}
		
		if(ae.getSource() == dataComboServ)
		{
			System.out.println("Selected index=" + dataComboServ.getSelectedIndex() + " Selected item=" + dataComboServ.getSelectedItem());
			dataComboEJ.setEnabled(true);			
		}
		
		if(ae.getSource() == dataComboEJ)
		{
			System.out.println("Selected index=" + dataComboEJ.getSelectedIndex() + " Selected item=" + dataComboEJ.getSelectedItem());
			bDataGen.setEnabled(true);
		}
		
		if(ae.getSource() == pentaCombo)
		{
			System.out.println("Selected index=" + pentaCombo.getSelectedIndex() + " Selected item=" + pentaCombo.getSelectedItem());
			bPentaGen.setEnabled(true);
		}
		
		if(ae.getSource() == bDataGen)
		{			
			try{
				Object objFile = parser.parse(new FileReader("Datafactory.json"));        
				JSONArray msg = (JSONArray) objFile;
				
				JSONObject objData1 = (JSONObject) msg.get(0);
				JSONObject objData2 = (JSONObject) msg.get(1);
				
				if("NOAA".equals(dataComboServ.getSelectedItem()))
				{
					objData1.put("NoaaFlag", true);
					if("With out Avro".equals(dataComboEJ.getSelectedItem()))
					{
						objData1.put("id3", "59");
					}
					else
					{
						objData1.put("id3", "51");
					}
				}
				else if("Twitter".equals(dataComboServ.getSelectedItem()))
				{
					objData2.put("TwitFlag", true);
					if("With out Avro".equals(dataComboEJ.getSelectedItem()))
					{
						objData2.put("id3", "76");
					}
					else
					{
						objData2.put("id3", "75");
					}
				}
						
				FileWriter file = new FileWriter("Datafactory.json");				
				file.write(msg.toString());
				file.flush();
				file.close();
			}catch(Exception e){
				System.out.println("Exception:"+e);
			}
		}
		
		if(ae.getSource() == bPentaGen)
		{			
			try{
				Object objFile = parser.parse(new FileReader("Datafactory.json"));        
				JSONArray msg = (JSONArray) objFile;
				
				JSONObject objData = (JSONObject) msg.get(2);
				
				objData.put("PentFlag", true);
				
				if("Less than 40 Degree".equals(pentaCombo.getSelectedItem()))
				{					
					objData.put("file", "J2.kjb");	
				}
				else
				{
					objData.put("file", "J1.kjb");
				}
						
				FileWriter file = new FileWriter("Datafactory.json");				
				file.write(msg.toString());
				file.flush();
				file.close();
			}catch(Exception e){
				System.out.println("Exception:"+e);
			}
		}
		
		if(ae.getSource() == bReset)
		{
			try{
				Object objFile = parser.parse(new FileReader("Datafactory.json"));        
				JSONArray msg = (JSONArray) objFile;
				
				JSONObject objData1 = (JSONObject) msg.get(0);
				JSONObject objData2 = (JSONObject) msg.get(1);
				JSONObject objData3 = (JSONObject) msg.get(2);
				
				objData1.put("NoaaFlag", false);
				objData1.put("id1", "49");
				objData1.put("id2", "72");
				objData1.put("id3", "51");
				
				objData2.put("TwitFlag", false);
				objData2.put("id1", "73");
				objData2.put("id2", "74");
				objData2.put("id3", "75");
				
				
				objData3.put("PentFlag", false);
				objData3.put("file", "J1.kjb");
						
				FileWriter file = new FileWriter("Datafactory.json");				
				file.write(msg.toString());
				file.flush();
				file.close();
			}catch(Exception e){
				System.out.println("Exception:"+e);
			}
		}
    }    

	public static void main(String arg[])
	{
		new LatestGUI();
	}
}
