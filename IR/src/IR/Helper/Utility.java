package IR.Helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class Utility {


	public static List<String> getPathForAllFiles(String path) {

		List<String> listOfFilePath = new ArrayList<String>();

		try {
			Files.walk(Paths.get(path)).forEach(folderPath -> {

				File file = new File(folderPath.toString());

				if (file.isFile() && !file.getName().contains("read")) {

					listOfFilePath.add(folderPath.toString());
				}
			});
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return listOfFilePath;
	}

}
