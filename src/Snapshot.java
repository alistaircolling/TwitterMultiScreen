import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Map.Entry;

//

public class Snapshot {

	
//time of snapshot
	private Date time;
	//all of the data
	private ArrayList<Entry<String, Integer>> array;
	//the index in the arraylist (in case we need to know here)
	private int index;

	public Snapshot(Date theTime, ArrayList<Entry<String, Integer>> mapList, int theInd) {
		setTime(theTime);
		setArray(mapList);
		setIndex(theInd);
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}

	public ArrayList<Entry<String, Integer>> getArray() {
		return array;
	}

	public void setArray(ArrayList<Entry<String, Integer>> array) {
		this.array = array;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

}
