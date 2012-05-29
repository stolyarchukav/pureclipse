package name.slide.pureclipse.license;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import name.slide.pureclipse.utils.StringUtils;

import org.eclipse.ui.PlatformUI;

public class LicenseManager {

	private static final int TRIAL_DAYS = 30;
	
	private static LicenseManager licenseManager;
	
	private int daysLeft;
	private Date lastLaunch;
	private boolean licensed;
	private boolean tempLicense;
	private boolean showWarning = true;
	private File file;
	private String serialNumber;
	
	private LicenseManager() {
		try {
			lastLaunch = new Date();
			file = new File(System.getProperty("user.home") + "/update.pel");
			if (! file.exists()) {
				startTrial(false);
				return;
			}
			BufferedReader reader = new BufferedReader(new FileReader(file));
			while (reader.ready()) {
				String line = StringUtils.decode(reader.readLine());
				if (line.startsWith("key")) {
					String key = line.substring(3);
					String[] params = key.split("&");
					licensed = (params.length == 2 && LicenseValidator.validate(params[0], params[1]) == LicenseType.FULL);
					if (licensed) {
						serialNumber = params[1];
					}
				}
				else {
					String[] array = line.split(":");
					daysLeft = Integer.valueOf(array[1]);
					lastLaunch = new Date();
					lastLaunch.setTime(Long.valueOf(array[0]));
					Date date = new Date();
					if (isDateChange(date)) {
						lastLaunch = date;
					}
					tempLicense = array.length > 2;
					writeTrialStatus(daysLeft, lastLaunch, tempLicense);
				}
			}
			if (! licensed) {
				new DateListenerThread().start();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean startTrial(boolean tempLicense) {
		if (! this.tempLicense) {
			try {
				file.createNewFile();
				writeTrialStatus(TRIAL_DAYS, lastLaunch, tempLicense);
				daysLeft = TRIAL_DAYS;
				this.tempLicense = tempLicense;
				return true;
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	public int getDaysLeft() {
		return daysLeft;
	}

	public String getSerialNumber() {
		return serialNumber;
	}
	
	private boolean isDateChange(Date date) {
		int oldValue = daysLeft;
		long change = date.getTime() - lastLaunch.getTime();
		if (change < 0) {
			daysLeft--;
		}
		else {
			daysLeft -= change /1000 / 60 / 60 / 24;
		}
		return daysLeft != oldValue;
	}
	
	public static LicenseManager getInstance() {
		if (licenseManager == null) {
			synchronized (LicenseManager.class) {
				if (licenseManager == null) {
					licenseManager = new LicenseManager();
				}
			}
		}
		return licenseManager;
	}
	
	private void writeTrialStatus(int days, Date lastLaunch, boolean tempLicense) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(StringUtils.encode(lastLaunch.getTime() + ":" + days + (tempLicense ? ":T" : "")));
		writer.close();
	}
	
	public void writeSerialNumber(String email, String serialNumber) {
		try {
			FileWriter writer = new FileWriter(file);
			writer.write(StringUtils.encode("key" + email + "&" + serialNumber));
			writer.close();
			licensed = true;
			this.serialNumber = serialNumber;
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean checkLicense() {
		if (! licensed) {
			return canContinue();
		}
		return true;
	}
	
	private class DateListenerThread extends Thread {
		@Override
		public void run() {
			while (! isInterrupted()) {
				try {
					sleep(50000);
					Date date = new Date();
					if (isDateChange(date)) {
						lastLaunch = date;
						showWarning = true;
						try {
							writeTrialStatus(daysLeft, lastLaunch, tempLicense);
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
				catch (InterruptedException e) {
					interrupt();
				}
			}
		}
	}
	
	private boolean canContinue() {
		if (daysLeft <= 0 || showWarning) {
			TrialDialog dialog = new TrialDialog(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), 
					daysLeft, null, true);
			dialog.open();
			showWarning = false;
			return licensed || daysLeft > 0;
		}
		return true;
	}
	
}
