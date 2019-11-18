import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.ForkJoinPool;

public class Main {
	private static final int THREADS_COUNT = Runtime.getRuntime().availableProcessors() * 2;

	public static void main(String[] args) {
		URI rootLinkAddress = UI.getURIToParse("Type URI of the site you'd like to parse");
		Link rootLink = new Link(rootLinkAddress, null);
		new ForkJoinPool(THREADS_COUNT).invoke(new LinkAction(rootLink));
		if (rootLink.getChildren().size() > 0) {
			UI.println("Successfully parsed");
			while (true) {
				String saveToFile = UI.userInput("Type in the path of file to save the site map");
				try {
					FileWriter mapFile = new FileWriter(saveToFile);
					mapFile.write(rootLink.export());
					mapFile.flush();
					mapFile.close();
					UI.println("File " + saveToFile + " saved successfully");
					break;
				} catch (IOException e) {
					System.err.println("Some problems with file.");
				}
			}
		} else {
			UI.println("Nothing to save. Exiting");
		}
	}
}
