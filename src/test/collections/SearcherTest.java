package collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import stas.collections.SimpleList;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SearcherTest {

    private SimpleList<TestObject> list;
    private SimpleList.Searcher<TestObject> searcher;

    private record TestObject(String name, int age) {}

    @BeforeEach
    void setUp() {
        list = new SimpleList<>();
        list.add(new TestObject("John", 25));
        list.add(new TestObject("Jane", 30));
        list.add(new TestObject("Alice", 35));
        list.add(new TestObject("Bob", 40));

        searcher = list.searcher();
    }

    @Test
    void getFieldValue_shouldReturnFieldValue_whenFieldExists() {
        searcher.search("name", list.get(0).name);
        assertEquals(searcher.getRelevant().get(0), list.get(0));
    }

    @Test
    void search_shouldNotAddNonMatchingElementsToRelevantList() {
        searcher.search("name", "invalidName");
        assertEquals(searcher.getRelevant().size(), 0);
    }

    @Test
    void search_shouldAddMatchingElementsToRelevantList() {
        searcher.search("name", "John");
        SimpleList<TestObject> relevantList = searcher.getRelevant();
        assertEquals(1, relevantList.size());
        assertEquals("John", relevantList.get(0).name());
    }

    @Test
    void search_shouldIgnoreCaseWhenComparingSample() {
        searcher.search("name", "jane");
        SimpleList<TestObject> relevantList = searcher.getRelevant();
        assertEquals(1, relevantList.size());
        assertEquals("Jane", relevantList.get(0).name());
    }

    @Test
    void search_shouldAddMatchingElementsByAgeToRelevantList() {
        searcher.search("age", "35");
        SimpleList<TestObject> relevantList = searcher.getRelevant();
        assertEquals(1, relevantList.size());
    }
}