# A4 Write-up

**Name: Ouwen Li**
**Onyen: ooowen**
**Game URL: http://bridges-games.herokuapp.com/assignments/4/ooowen**
**Bomb rule I played with (SHRINK or GAME_OVER): GAME_OVER**

Three questions, 20 points. A few sentences each. Where a question asks for a
number, show where it came from.

---

## Question 1: The tail pointer (7 points)

Your `LinkedQueue` keeps a `tail` reference. Suppose you deleted it and found
the back of the queue by starting at `head` and following `next` until you ran
out of nodes.

What would `enqueue` cost then, in Big-O? The game calls `enqueue` once per
tick and runs about 7 ticks a second. For a snake 500 cells long, roughly how
many nodes would `enqueue` alone visit each second, and would you notice?

Now do the same for the autograder's speed test. It fills a queue with
400,000 items, then does 400,000 more rounds of dequeue-then-enqueue, so the
queue stays at 400,000. Roughly how many nodes would those enqueues visit?

```
Without the tail pointer, enqueue would need to walk from head to the last node, making it O(n). For a snake 500 cells long moving about 7 times per second, that means roughly 500 × 7 = 3,500 node visits per second, which would probably not cause noticeable lag. The autograder’s 400,000 dequeue-then-enqueue rounds would require about 400,000 × 400,000 = 160 billion node visits. Including the initial queue filling adds about 80 billion more, for roughly 240 billion total. That would be much slower than using a tail pointer.
```

---

## Question 2: The one method allowed to walk (7 points)

`contains` is O(n), and the game calls it through `snake.occupies(...)`. Read
`GameState.tick()` and `GameState.freeCell()` and find every call.

On a tick where the snake eats an apple, roughly how many nodes do those
`contains` calls visit in total, for a snake of length n on the 30 by 30
board? Give an expression in n. Then name a data structure from later in this
course that would make "is the snake on this cell?" fast, and say what it
would cost to keep it up to date as the snake moves.

```
Let n be the snake’s length before eating an apple. tick() calls contains once to check for a collision, visiting all n nodes because the apple is outside the snake. After the snake grows to n + 1 cells, freeCell() calls contains for all 900 board cells. An upper estimate is n + 900(n + 1) = 901n + 900 node visits; the actual count is lower because searches stop when they find a match. A hash set could track occupied cells and make membership checks O(1) on average. Adding the new head and removing departing tail cells would also take O(1) on average, using O(n) extra space.
```

---

## Question 3: Which end is the head? (6 points)

The snake's head is the **back** of the queue, and its tail end is the
**front**. Explain why, using what happens to the body on each tick.

Then suppose you flipped it, so the head was the front. Which operation would
each tick need that `LinkedQueue` does not have, and why is that operation hard
to make O(1) on a singly linked list?

```
The snake’s head is at the back of the queue because each move adds a new head cell there through enqueue. Its tail is at the front because dequeue removes the old tail cell. Both operations take O(1). If the snake’s head were at the front, each move would require inserting at the front and removing from the back, which this queue does not support. Removing from the back is difficult to make O(1) in a singly linked list because the last node has no link to the node before it. Finding that previous node normally requires walking from the beginning, taking O(n).
```
