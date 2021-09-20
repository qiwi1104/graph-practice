package model.components;

public class Edge {
    private Vertex from;
    private Vertex to;

    private int weight;

    public Edge(String from, String to) {
        this.from = new Vertex(from);
        this.to = new Vertex(to);
    }

    public Edge(Vertex from, Vertex to) {
        this.from = from;
        this.to = to;
    }

    public Edge(String from, String to, int weight) {
        this.from = new Vertex(from);
        this.to = new Vertex(to);
        this.weight = weight;
    }

    public Edge(Vertex from, Vertex to, int weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public Vertex getFrom() {
        return from;
    }

    public Vertex getTo() {
        return to;
    }

    public void removeVertex(String label) {
        if (from.getLabel().equals(label)) {
            from = null;
        }
        if (to.getLabel().equals(label)) {
            to = null;
        }
    }
}
