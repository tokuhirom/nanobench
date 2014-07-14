import java.util.List;
import java.util.ArrayList;
import java.util.LinkedList;

public class ListBenchmark {
    public void benchArrayList() {
        List<Integer> l = new ArrayList<>();
        for (int i=0; i<1_000_000; ++i) {
            l.add(i);
        }
    }

    public void benchLinkedList() {
        List<Integer> l = new LinkedList<>();
        for (int i=0; i<1_000_000; ++i) {
            l.add(i);
        }
    }
}
