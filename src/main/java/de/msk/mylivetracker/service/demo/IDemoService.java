package de.msk.mylivetracker.service.demo;


/**
 * IDemoService.
 * 
 * @author michael skerwiderski, (c)2011
 * 
 * @version 000
 * 
 * history
 * 000 initial 2011-08-11
 * 
 */
public interface IDemoService {
	
	/**
	 * run the demo.
	 */
	public void runDemo();
		
	/**
	 * stop the demo.
	 */
	public void stopDemo();

	public DemoStatus getDemoStatus();
	
	public static class DemoStatus {
		private boolean running;
		private int countTracks;
		public DemoStatus(boolean running, int countTracks) {
			this.running = running;
			this.countTracks = countTracks;
		}
		public boolean isRunning() {
			return running;
		}
		public int getCountTracks() {
			return countTracks;
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#toString()
		 */
		@Override
		public String toString() {
			return "DemoStatus [running=" + running + ", countTracks="
				+ countTracks + "]";
		}
	}
	
	public int insertTrackAsDemoTrack(String trackId);
}
