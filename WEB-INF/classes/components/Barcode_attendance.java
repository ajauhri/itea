package components;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

public class Barcode_attendance {
	public String gr_nos;

	public Barcode_attendance(String date, String subject_id, String slot,
			String path, String filename) throws IOException {
		try {
			String fName = path + filename;
			String thisLine;
			int flag = 0;
			FileInputStream fis = new FileInputStream(fName);
//			DataInputStream myInput = new DataInputStream(fis);
			BufferedReader myInput= new BufferedReader(new InputStreamReader(fis));
			String gr_list = "";
			
			while ((thisLine = myInput.readLine()) != null) {
				String strar[] = thisLine.split(",");
				if (strar[0].equals(date)) {
					if (strar[1].equals(slot)) {
						flag = 1;
						for (int j = 2; j < strar.length; j++) {
							gr_list += strar[j] + ",";
						}
						break;
					}
				}
			}
			if (flag == 1)
				gr_nos = gr_list;
		} catch (FileNotFoundException e) {
			// TODO error
			System.out.println(e.toString());
		}
	}
}
