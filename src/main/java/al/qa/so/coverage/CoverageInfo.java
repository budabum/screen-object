package al.qa.so.coverage;

/**
 * @author Alexey Lyanguzov.
 */
abstract class CoverageInfo {
    private final String name;
    private int hits = 0;

    CoverageInfo(String name) {
        this.name = name;
    }

    String getName() {
        return name;
    }

    public int getHits() {
        return hits;
    }

    public void hit(){
        hits++;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CoverageInfo that = (CoverageInfo) o;

        return name != null ? name.equals(that.name) : that.name == null;

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return String.format("%s(%s)", name, hits);
    }
}
