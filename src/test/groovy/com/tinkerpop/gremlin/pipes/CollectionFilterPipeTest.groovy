package com.tinkerpop.gremlin.pipes

import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory
import com.tinkerpop.gremlin.Gremlin
import junit.framework.TestCase

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
class CollectionFilterPipeTest extends TestCase {

  public void testExceptPattern() {
    Gremlin.load();
    Graph g = TinkerGraphFactory.createTinkerGraph();

    def x = [] as Set
    def results = []
    g.v(1).outE.inV.aggregate(x).outE.inV.except(x) >> results
    assertEquals(results.size(), 1)
    assertTrue(results.contains(g.v(5)))

  }

  public void testExceptWithClosureFilter() {
    Gremlin.load();
    Graph g = TinkerGraphFactory.createTinkerGraph();

    def x = []
    def results = []
    g.v(1).outE.inV.aggregate(x).outE.inV.except(x) {it.id == 5} >> results
    assertEquals(results.size(), 0)
  }

  public void testRetainPattern() {
    Gremlin.load();
    Graph g = TinkerGraphFactory.createTinkerGraph();

    def x = [] as Set
    def results = []
    g.v(1).outE.inV.aggregate(x).outE.inV.retain(x) >> results
    assertEquals(results.size(), 1)
    assertTrue(results.contains(g.v(3)))
  }

  public void testRetainWithClosureFilter() {
    Gremlin.load();
    Graph g = TinkerGraphFactory.createTinkerGraph();

    def x = []
    def results = []
    g.v(1).outE.inV.aggregate(x).outE.inV.retain(x) {it.id == 3} >> results
    assertEquals(results.size(), 0)
  }

}
