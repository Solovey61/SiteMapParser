import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.RecursiveAction;

class LinkAction extends RecursiveAction {
	private static final int TIME_MILLIS_TO_SLEEP = 100;

	private static TreeSet<URI> visited = new TreeSet<>();
	private Link link;

	public LinkAction(Link link) {
		this.link = link;
		if (link.getDepth() == 0)
			visited.add(link.getUri());
	}

	public static TreeSet<URI> getVisited() {
		return visited;
	}

	@Override
	protected void compute() {
		try {
			Thread.sleep(TIME_MILLIS_TO_SLEEP);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Parsing " + link.getUri());
		addChildrenToLink();
		if (!link.getChildren().isEmpty()) {
			List<LinkAction> actions = new ArrayList<>();
			link.getChildren().forEach(child -> {
				actions.add(new LinkAction(child));
			});
			invokeAll(actions);
		}
	}

	private void addChildrenToLink() {
		try {
			Elements elements = Jsoup.connect(link.getUri().toString()).get().select("a");
			HashSet<URI> hrefs = new HashSet<>();
			elements.forEach(element -> {
				URI href = URI.create(element.attr("href").trim());
				if (!href.isAbsolute())
					href = link.getUri().resolve(href);
				if (!href.isOpaque() && !href.toString().contains("#")) {
					try {
						href = new URI(href.getScheme(), href.getAuthority(), href.getPath().replaceAll("/+$", ""),
								null,
								href.getFragment());
					} catch (URISyntaxException ignored) {
					}
					if (!isVisited(href)
							&& href.getHost().equals(link.getUri().getHost())
							&& href.getScheme().equals(link.getUri().getScheme())
					) {
						visited.add(href);
						link.addChild(href, link);
					}
				}
			});
		} catch (Exception ignored) {
		}
	}

	private boolean isVisited(URI uri) {
		return visited.contains(uri);
	}
}
