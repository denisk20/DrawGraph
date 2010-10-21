package com.drawgraph.parser.callbacks;

import com.drawgraph.model.Line;
import com.drawgraph.model.LineImpl;
import com.drawgraph.model.Node;
import org.xml.sax.Attributes;

import java.util.HashSet;
import java.util.Map;

/**
 * Date: Oct 21, 2010
 * Time: 11:48:38 PM
 *
 * @author denisk
 */
public class LineCallback implements Callback {
	private GraphCallback parent;

	private HashSet<LineSkeleton> skeletons = new HashSet<LineSkeleton>();
	private final static String ID = "id";
	private final static String SOURCE = "source";
	private final static String TARGET = "target";

	public LineCallback(GraphCallback parent) {
		this.parent = parent;
	}

	public void startElement(String name, Attributes atts) {
		String id = atts.getValue(ID);
		String sourceId = atts.getValue(SOURCE);
		String targetId = atts.getValue(TARGET);

		LineSkeleton skeleton = new LineSkeleton(id, sourceId, targetId);
		skeletons.add(skeleton);
	}

	public void endElement(String name) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public void characters(String chars) {
		//To change body of implemented methods use File | Settings | File Templates.
	}

	public Callback getChildCallback() {
		return null;
	}

	public Callback getParentCallback() {
		return parent;
	}

	public HashSet<Line> getLines(Map<String, Node> nodes) {
		HashSet<Line> lines = new HashSet<Line>();
		for (LineSkeleton skeleton : skeletons) {
			String sourceId = skeleton.getSource();
			Node source = nodes.get(sourceId);
			if (source == null) {
				throw new IllegalStateException("can't create line - no node with id: " + sourceId);
			}
			String targetId = skeleton.getTarget();
			Node target = nodes.get(targetId);
			if (target == null) {
				throw new IllegalStateException("can't create line - no node with id: " + targetId);
			}

			LineImpl line = new LineImpl(source, target, skeleton.getId());

			lines.add(line);
		}

		return lines;
	}


	public static class LineSkeleton {
		private String id;
		private String source;
		private String target;

		public LineSkeleton(String id, String source, String target) {
			this.id = id;
			this.source = source;
			this.target = target;
		}

		public String getId() {
			return id;
		}

		public String getSource() {
			return source;
		}

		public String getTarget() {
			return target;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) {
				return true;
			}
			if (!(o instanceof LineSkeleton)) {
				return false;
			}

			final LineSkeleton that = (LineSkeleton) o;

			if (id != null ? !id.equals(that.id) : that.id != null) {
				return false;
			}
			if (source != null ? !source.equals(that.source) : that.source != null) {
				return false;
			}
			if (target != null ? !target.equals(that.target) : that.target != null) {
				return false;
			}

			return true;
		}

		@Override
		public int hashCode() {
			int result = id != null ? id.hashCode() : 0;
			result = 31 * result + (source != null ? source.hashCode() : 0);
			result = 31 * result + (target != null ? target.hashCode() : 0);
			return result;
		}

		@Override
		public String toString() {
			return "LineSkeleton{" + "id='" + id + '\'' + ", source='" + source + '\'' + ", target='" + target + '\'' + '}';
		}
	}
}
