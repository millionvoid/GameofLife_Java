
public class PulseEmiter {
	int interval;
	public boolean on;
	boolean paused=false;
	public PulseEmiter(int ms) {
		interval = ms;
	}
	
	public void start() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				paused=false;
				while(true)
				{
					try {
						Thread.sleep(interval);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(paused)
						break;
					on=!on;
				}
			}
		}).start();
	}
	
	public void pause() {
		paused=true;
	}
	
	public void setInterval(int ms) {
		interval = ms;
	}
}
