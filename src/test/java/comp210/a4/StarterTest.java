package comp210.a4;

import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

/**
 * A few starter tests. Right-click this file in IntelliJ and choose Run.
 *
 * These are a sample, not the whole autograder. Gradescope runs many more,
 * including empty queues, long queues, and whole scripted games. Passing
 * everything here is a good sign, not a guarantee. Add your own tests below.
 */
class StarterTest {

    @Test
    void itemsComeOutInTheOrderTheyWentIn() {
        LinkedQueue<String> q = new LinkedQueue<>();
        q.enqueue("apple");
        q.enqueue("bomb");
        q.enqueue("snake");
        assertEquals("apple", q.dequeue());
        assertEquals("bomb", q.dequeue());
        assertEquals("snake", q.dequeue());
    }

    @Test
    void peekAndPeekLastSeeBothEnds() {
        LinkedQueue<Integer> q = new LinkedQueue<>();
        q.enqueue(1);
        q.enqueue(2);
        q.enqueue(3);
        assertEquals(1, q.peek());
        assertEquals(3, q.peekLast());
        assertEquals(3, q.size(), "peeking should not remove anything");
    }

    @Test
    void emptyQueueRefusesToDequeue() {
        LinkedQueue<Integer> q = new LinkedQueue<>();
        assertTrue(q.isEmpty());
        assertThrows(NoSuchElementException.class, q::dequeue);
        assertThrows(NoSuchElementException.class, q::peek);
        assertThrows(NoSuchElementException.class, q::peekLast);
        assertThrows(IllegalArgumentException.class, () -> q.enqueue(null));
        assertEquals(0, q.size());
    }

    @Test
    void containsComparesWithEquals() {
        LinkedQueue<Cell> q = new LinkedQueue<>();
        q.enqueue(new Cell(4, 7));
        assertTrue(q.contains(new Cell(4, 7)), "a different Cell object with the same x and y should count");
        assertFalse(q.contains(new Cell(7, 4)));
    }

    @Test
    void snakeMovesWithoutGrowing() {
        Snake s = new Snake(new Cell(5, 5), 3, Direction.EAST);
        s.advance(new Cell(6, 5), false);
        assertEquals(new Cell(6, 5), s.head());
        assertEquals(3, s.length());
        assertEquals(new Cell(4, 5), s.tail(), "the old tail cell (3, 5) should have left");
    }

    @Test
    void snakeGrowsWhenItEats() {
        Snake s = new Snake(new Cell(5, 5), 3, Direction.EAST);
        s.advance(new Cell(6, 5), true);
        assertEquals(4, s.length());
        assertEquals(new Cell(3, 5), s.tail(), "nothing should leave when growing");
    }

    @Test
    void queueCanBeReusedAfterRemovingItsLastItem() {
        LinkedQueue<Integer> q = new LinkedQueue<>();
        q.enqueue(1);
        assertEquals(1, q.dequeue());
        assertTrue(q.isEmpty());
        q.enqueue(2);
        assertEquals(2, q.peek());
        assertEquals(2, q.peekLast());
        assertEquals(1, q.size());
        assertEquals(2, q.dequeue());
    }

    @Test
    void snakeShrinksFromItsTailUntilGone() {
        Snake s = new Snake(new Cell(5, 5), 3, Direction.EAST);
        assertTrue(s.shrink());
        assertEquals(new Cell(4, 5), s.tail());
        assertEquals(new Cell(5, 5), s.head());
        assertEquals(2, s.length());
        assertTrue(s.shrink());
        assertFalse(s.shrink());
        assertEquals(0, s.length());
        assertFalse(s.shrink());
    }

    @Test
    void gameOverBombEndsTheGameImmediately() {
        GameState state = new GameState(30, 30, new Random(0), GameState.BombRule.GAME_OVER);
        Cell next = state.getSnake().head().step(Direction.EAST, 30, 30);
        state.placeApple(new Cell(0, 0));
        state.placeBomb(0, next);
        assertEquals(GameState.Status.BLOWN_UP, state.tick());
        assertEquals(3, state.getSnake().length());
    }
}
