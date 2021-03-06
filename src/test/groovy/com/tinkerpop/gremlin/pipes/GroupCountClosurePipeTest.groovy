package com.tinkerpop.gremlin.pipes

import com.tinkerpop.blueprints.pgm.Graph
import com.tinkerpop.blueprints.pgm.impls.tg.TinkerGraphFactory
import com.tinkerpop.gremlin.Gremlin
import junit.framework.TestCase
import com.tinkerpop.blueprints.pgm.Vertex

/**
 * @author Marko A. Rodriguez (http://markorodriguez.com)
 */
class GroupCountClosurePipeTest extends TestCase {

  public void testGroupCountClosure() {
    Gremlin.load();
    Graph g = TinkerGraphFactory.createTinkerGraph();
    def m = [:].withDefault {0};
    g.v(1).outE.inV.groupCount(m) {it + 1}.outE.inV.groupCount(m) {it + 3} >> -1
    assertEquals(m.size(), 4)
    assertEquals(m.get(g.v(2)), 1);
    assertEquals(m.get(g.v(4)), 1);
    assertEquals(m.get(g.v(3)), 4);
    assertEquals(m.get(g.v(5)), 3);

    m = [:].withDefault {0};
    g.v(1).outE.inV.groupCount(m) {it + 1}.outE.inV.groupCount(m) {3.0d} >> -1
    assertEquals(m.size(), 4)
    assertEquals(m.get(g.v(2)), 1);
    assertEquals(m.get(g.v(4)), 1);
    assertEquals(m.get(g.v(3)), 3.0d);
    assertEquals(m.get(g.v(5)), 3.0d);
  }

  public void testGroupCountMixing() {
    Gremlin.load();
    Graph g = TinkerGraphFactory.createTinkerGraph();
    def m = [:].withDefault {0};
    g.v(1).outE.inV.groupCount(m).outE.inV.groupCount(m) {it + 3.0d} >> -1
    assertEquals(m.size(), 4)
    assertEquals(m.get(g.v(2)), 1);
    assertEquals(m.get(g.v(4)), 1);
    assertEquals(m.get(g.v(3)), 4.0d);
    assertEquals(m.get(g.v(5)), 3.0d);
  }

  public void testGroupCountNoCap() throws Exception {
    Gremlin.load();
    Graph g = TinkerGraphFactory.createTinkerGraph();
    def m = [:]
    assertEquals(g.v(1).outE.inV.groupCount(m), g.v(1).outE.inV);
    assertEquals(m.size(), 3);
    m.each {key, value -> assertEquals(value, 1); assertTrue(key instanceof Vertex)};
  }

  public void testGroupCountCap() throws Exception {
    Gremlin.load();
    Graph g = TinkerGraphFactory.createTinkerGraph();
    def m = g.E.bothV.groupCount.cap >> 1
    def n = m.clone()
    g.E.bothV.groupCount(n).cap >> -1

    m.each {key, value -> assertEquals(value / 1, n[key] / 2)};
  }
}
