package model.components;

import java.util.Objects;

public class Edge implements Cloneable {
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

    @Override
    public Edge clone() {
        Edge copy = null;

        try {
            copy = (Edge) super.clone();
            copy.from = from.clone();
            copy.to = to.clone();
            copy.weight = weight;
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }

        return copy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Edge edge = (Edge) o;
        return from.equals(edge.from) &&
                to.equals(edge.to);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to);
    }
}
