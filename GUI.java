import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;
public class GUI extends JFrame{
	private int windowHeight=850;
	private int windowWidth=800;
	private int windowStartH;
	private int windowStartW;
	private int screenHeight;
	private int screenWidth;
	private int cellHeight=70;
	private int cellWidth=70;
	private int cellrow;
	private int cellcolumn;
	private int graphicStartH;
	private int graphicStartW;
	private int graphicEndH;
	private int graphicEndW;
	private int graphicH;
	private int graphicW;
	private int borderHeight=40;
	private int borderWidth=30;
	private int cellPadding=1;
	private boolean [][] alive;
	private int [][]cellStartW;
	private int [][]cellStartH;
	private Color aliveCellColor=Color.black;
	private Color deadCellColor=Color.white;
	private int buttonStartW;
	private int buttonStartH;
	private int buttonW=100;
	private int buttonH=50;
	private int labelStartW;
	private int labelStartH;
	private int labelW=100;
	private int labelH=50;
	private int updatetimes=0;
	private JButton button;
	private String buttonText_normal="暂停";
	private String buttonText_paused="开始";
	private JLabel label;
	private String labelPrefix="演化次数：";
	private Container container;
//	private LYDClock clock;
	private JMenuBar menuBar;
	private JPanel uiPanel;
	private JPanel buttonPanel;
	private PulseEmiter pulseEmiter;
	private Graphics uiGraphics;
	Cells cells;
	
	public GUI(int row,int column,int interval_ms) {
		super("Game of Life");
		
		cells=new Cells(row, column);
		pulseEmiter=new PulseEmiter(interval_ms);
		
		setVisible(true);
		
		Dimension dimension=Toolkit.getDefaultToolkit().getScreenSize();
		screenHeight=dimension.height;
		screenWidth=dimension.width;
		
		setSize(windowWidth, windowHeight);
		
		windowStartW=(screenWidth-windowWidth)/2;
		windowStartH=(screenHeight-windowHeight)/2;
		
		setLocation(windowStartW,windowStartH);
		
		cellrow=row;
		cellcolumn=column;
		
		graphicStartW=borderWidth;
		graphicStartH=borderHeight;
		
		cellHeight=(windowHeight-graphicStartH-borderHeight-cellPadding*(cellrow-1))/cellrow;
		cellWidth=(windowWidth-graphicStartW-borderWidth-cellPadding*(cellcolumn-1))/cellcolumn;
		
		if(cellHeight>cellWidth)
			cellHeight=cellWidth;
		else
			cellWidth=cellHeight;
		
		graphicH=cellHeight*cellrow+cellPadding*(cellrow-1);
		graphicW=cellWidth*cellcolumn+cellPadding*(cellcolumn-1);
		
		graphicEndW=graphicStartW+graphicW;
		graphicEndH=graphicStartH+graphicH;
		
		alive=new boolean[cellrow][cellcolumn];
		
		cellStartH=new int[cellrow][cellcolumn];
		cellStartW=new int[cellrow][cellcolumn];
		for(int i=0;i<cellrow;i++)
		{
			for(int j=0;j<cellcolumn;j++)
			{
				cellStartH[i][j]=graphicStartH+i*(cellHeight+cellPadding);
				cellStartW[i][j]=graphicStartW+j*(cellWidth+cellPadding);
			}
		}
		this.addWindowListener(new WindowAdapter() {
				public void windowClosing(WindowEvent e) {
					System.exit(0);
				}
			}
		);
		
		buttonStartH=graphicEndH+20;
		buttonStartW=graphicEndW-20-buttonW;
		labelStartH=graphicEndH+20;
		labelStartW=graphicStartW+20;
		

		button=new JButton(buttonText_paused);
		button.setVisible(true);
		button.setPreferredSize(new Dimension(buttonW,buttonH));
		
		label=new JLabel(labelPrefix+updatetimes);
		label.setVisible(true);
		buttonPanel=new JPanel(new BorderLayout());
		uiPanel=new JPanel(new BorderLayout());
		
		buttonPanel.setPreferredSize(new Dimension(windowWidth, 30));
		
		buttonPanel.add(button,BorderLayout.EAST);
		buttonPanel.add(label,BorderLayout.WEST);
		
		this.getContentPane().add(uiPanel,BorderLayout.CENTER);
		this.getContentPane().add(buttonPanel,BorderLayout.AFTER_LAST_LINE);
		
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(button.getText().equals(buttonText_normal))
				{
					pulseEmiter.pause();
					button.setText(buttonText_paused);
				}
				else
				{
					pulseEmiter.start();
					button.setText(buttonText_normal);
				}
			}
		});
		
		
		Thread updateThread=new Thread(new Runnable() {
			
			@Override
			public void run() {
				boolean on=false;
				while(true)
				{
					try {
						Thread.sleep(1);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(on!=pulseEmiter.on)
					{
						on=pulseEmiter.on;
						cells.evaluate();
						update();
					}
				}
				
			}
		});
		updateThread.setDaemon(true);
		updateThread.start();
		
		repaint();
	}
	public void update() {
		Graphics graphics=uiPanel.getGraphics();
		
		graphics.setColor(aliveCellColor);
		for(int i=0;i<cellrow;i++)
		{
			for(int j=0;j<cellcolumn;j++)
			{
				if(cells.getAt(i, j)&&(alive[i][j]!=cells.getAt(i, j)))
				{
					graphics.fillRect(cellStartW[i][j],cellStartH[i][j],cellWidth,cellHeight);
				}
			}
		}
		graphics.setColor(deadCellColor);
		for(int i=0;i<cellrow;i++)
		{
			for(int j=0;j<cellcolumn;j++)
			{
				if(!cells.getAt(i, j)&&(alive[i][j]!=cells.getAt(i, j)))
				{
					graphics.fillRect(cellStartW[i][j],cellStartH[i][j],cellWidth,cellHeight);
				}
				alive[i][j]=cells.getAt(i, j);
			}
		}
		
		label.setText(labelPrefix+(++updatetimes));
	}
	
	public void paint(Graphics graphics) {
		super.paint(graphics);

		new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(uiPanel==null);
				Graphics panelGraphics=uiPanel.getGraphics();
				windowHeight=getSize().height;
				windowWidth=getSize().width;
				
				graphicStartW=borderWidth;
				graphicStartH=borderHeight;
				
				cellHeight=(windowHeight-graphicStartH-borderHeight-cellPadding*(cellrow-1))/cellrow;
				cellWidth=(windowWidth-graphicStartW-borderWidth-cellPadding*(cellcolumn-1))/cellcolumn;
				
				if(cellHeight>cellWidth)
					cellHeight=cellWidth;
				else
					cellWidth=cellHeight;
				
				graphicH=cellHeight*cellrow+cellPadding*(cellrow-1);
				graphicW=cellWidth*cellcolumn+cellPadding*(cellcolumn-1);
				
				graphicEndW=graphicStartW+graphicW;
				graphicEndH=graphicStartH+graphicH;
				
				for(int i=0;i<cellrow;i++)
				{
					for(int j=0;j<cellcolumn;j++)
					{
						cellStartH[i][j]=graphicStartH+i*(cellHeight+cellPadding);
						cellStartW[i][j]=graphicStartW+j*(cellWidth+cellPadding);
					}
				}
				
				for(int i=0;i<cellrow;i++)
				{
					for(int j=0;j<cellcolumn;j++)
					{
						if(alive[i][j])
						{
							panelGraphics.setColor(aliveCellColor);
							panelGraphics.fillRect(cellStartW[i][j],cellStartH[i][j],cellWidth,cellHeight);
						}
						else
						{
							panelGraphics.setColor(deadCellColor);
							panelGraphics.fillRect(cellStartW[i][j],cellStartH[i][j],cellWidth,cellHeight);
						}
					}
				}
				
			}
		}).start();
	}
}
