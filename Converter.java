import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class Converter {
	private static String cochraneField = "cochraneField.txt";

	public static void convertCochrane() {
		HashMap<String, Boolean> labelMeaning = new HashMap<String, Boolean>();

		try (BufferedReader br = new BufferedReader(new FileReader(
				cochraneField))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] parts = line.split("\t");
					labelMeaning.put(parts[0], false);

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			String outputPath = "data_files/formatted";
			String cochranePath = "data_files/Cochrane";

			File folder = new File(cochranePath);
			File[] fileList = folder.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {

					File formatttedfile = new File(outputPath + "/"
							+ fileList[i].getName());

					// if file doesnt exists, then create it
					if (!formatttedfile.exists()) {

						formatttedfile.createNewFile();

					}

					BufferedReader br2 = new BufferedReader(new FileReader(
							fileList[i]));

					String line2;
					while ((line2 = br2.readLine()) != null) {

						if (!line2.isEmpty()) {
							System.out.println(line2);
							String[] tmpStr = line2.split(":");

							// String key = line2.substring(0, 2);
							// String value = line2.substring(3);

							String key = tmpStr[0];
							String value = tmpStr[1];

							for (String key1 : labelMeaning.keySet()) {

								if (key.equals(key1)) {
									labelMeaning.put(key1, true);

									if (!key.equals("PT")) {

										String formattedLine = key + "-"
												+ value;
										FileWriter fileWritter = new FileWriter(
												formatttedfile
														.getAbsoluteFile(),
												true);
										BufferedWriter bufferWritter = new BufferedWriter(
												fileWritter);
										bufferWritter.write(formattedLine
												+ "\n");
										bufferWritter.close();
									}

									else {
										String vArray[] = value.split(";");

										for (int j = 0; j < vArray.length; j++) {

											String formattedLine = key + "-"
													+ vArray[j];
											FileWriter fileWritter = new FileWriter(
													formatttedfile
															.getAbsoluteFile(),
													true);
											BufferedWriter bufferWritter = new BufferedWriter(
													fileWritter);
											bufferWritter.write(formattedLine
													+ "\n");
											bufferWritter.close();
										}
									}
								}
							}
						}

					}

				}
			}
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void convertPubmed() {
		HashMap<String, Boolean> labelMeaning = new HashMap<String, Boolean>();

		try (BufferedReader br = new BufferedReader(new FileReader(
				cochraneField))) {
			String line;
			while ((line = br.readLine()) != null) {
				if (!line.isEmpty()) {
					String[] parts = line.split("\t");
					labelMeaning.put(parts[0], false);

				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		try {
			String outputPath = "data_files/formatted";
			String cochranePath = "data_files/Pubmed";

			File folder = new File(cochranePath);
			File[] fileList = folder.listFiles();
			for (int i = 0; i < fileList.length; i++) {
				if (fileList[i].isFile()) {

					File formatttedfile = new File(outputPath + "/"
							+ fileList[i].getName());

					if (!formatttedfile.exists()) {
						formatttedfile.createNewFile();
					}

					BufferedReader br2 = new BufferedReader(new FileReader(
							fileList[i]));

					String line2;
					while ((line2 = br2.readLine()) != null) {

						if (!line2.isEmpty()) {
							System.out.println(line2);

							String key = line2.substring(0, 1);
							String value = line2.substring(5);

							for (String key1 : labelMeaning.keySet()) {

								if (key.equals(key1)) {
									labelMeaning.put(key1, true);

									String formattedLine = key + "-" + value;
									FileWriter fileWritter = new FileWriter(
											formatttedfile.getAbsoluteFile(),
											true);
									BufferedWriter bufferWritter = new BufferedWriter(
											fileWritter);
									bufferWritter.write(formattedLine + "\n");
									bufferWritter.close();

								}
							}
						}

					}

				}
			}
		} catch (IOException e) {

			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
