import java.net.URI;
import java.util.HashSet;

public class Link {
	private URI uri;
	private int depth;
	private Link parent;
	private HashSet<Link> children;

	public Link(URI uri, Link parent) {
		this.uri = uri;
		this.parent = parent;
		this.depth = parent == null ? 0 : parent.depth + 1;
		this.children = new HashSet<>();
	}

	public int getDepth() {
		return depth;
	}

	public URI getUri() {
		return uri;
	}

	public void addChild(URI uri, Link parent) {
		children.add(new Link(uri, parent));
	}

	public HashSet<Link> getChildren() {
		return children;
	}

	public String export() {
		StringBuilder sb = new StringBuilder();
		sb.append("\t".repeat(depth)).append(this).append("\n");
		children.forEach(child -> {
			sb.append(child.export());
		});
		return sb.toString();
	}

	@Override
	public String toString() {
		return "\t".repeat(depth) + uri.toString();
	}
}
