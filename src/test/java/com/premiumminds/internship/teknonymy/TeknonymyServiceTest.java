package com.premiumminds.internship.teknonymy;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime; 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;
import java.lang.Math;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

@RunWith(JUnit4.class)
public class TeknonymyServiceTest {

  /**
   * The corresponding implementations to test.
   *
   * If you want, you can make others :)
   *
   */
  public TeknonymyServiceTest() {
  };

  @Test
  public void PersonNoChildrenTest() {
    Person person = new Person("John",'M',null, LocalDateTime.of(1046, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "";
    assertEquals(result, expected);
  }

  @Test
  public void PersonOneChildTest() {
    Person person = new Person(
        "John",
        'M',
        new Person[]{ new Person("Holy",'F', null, LocalDateTime.of(1046, 1, 1, 0, 0)) },
        LocalDateTime.of(1046, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "father of Holy";
    assertEquals(result, expected);
  }

  @Test
  public void PersonNull() {
    Person person = null;
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = null;
    assertEquals(result, expected);
  }

  @Test
  public void NoNullChildrenTest() {
    Person person = new Person("John",'M',new Person[]{}, LocalDateTime.of(1946,1,1,0,0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "";
    assertEquals(result, expected);
  }

  @Test 
  public void ParentChildAgeTest() {
    Person john = new Person("John",'M',null,LocalDateTime.of(1976,2,3,11,0));
    Person kyle = new Person("Kyle",'M',new Person[]{john},LocalDateTime.of(1977,12,16,5,0));
    String result = new TeknonymyService().getTeknonymy(kyle);
    String expected = "Error: descendant John is older than parent Kyle";
    assertEquals(result, expected);
  }

  @Test
  public void UnknownTeknonymySuffixNullTest() {
    Person child = new Person("Joe",'M',null,LocalDateTime.of(2003,6,6,9,0));
    Person parent = new Person("John",null,new Person[]{child},LocalDateTime.of(1970,5,14,0,0));
    String result = new TeknonymyService().getTeknonymy(parent);
    String expected = "Error: could not obtain teknonymy suffix for John from null";
    assertEquals(result, expected);
  }

  @Test
  public void UnknownTeknonymySuffixTest() {
    Person child = new Person("Joe",'M',null,LocalDateTime.of(2003,6,6,9,0));
    Person parent = new Person("John",'K',new Person[]{child},LocalDateTime.of(1970,5,14,0,0));
    String result = new TeknonymyService().getTeknonymy(parent);
    String expected = "Error: could not obtain teknonymy suffix for John from 'K' Character";
    assertEquals(result, expected);
  }

  @Test
  public void MotherFiveChildrenTest() {
    Person person = new Person("Eve", 'F', 
        new Person[]{ new Person("Jack",'M', null, LocalDateTime.of(2001,1,1,17,6)),
                      new Person("John",'M', null, LocalDateTime.of(2001,1,1,17,5)),
                      new Person("Joe",'M', null, LocalDateTime.of(2001,1,1,17,0)),
                      new Person("Jill",'F', null, LocalDateTime.of(2003,1,1,0,0)),
                      new Person("Jenna",'M', null, LocalDateTime.of(2000,1,1,0,0)),
        }, LocalDateTime.of(1977, 1, 1, 0, 0));
      String result = new TeknonymyService().getTeknonymy(person);
      String expected = "mother of Jenna";
      assertEquals(result, expected);
  }

  @Test
  public void TwinsGreatGreatGrandchildrenTest() {
    Person person = new Person("John",'M',
        new Person[]{ new Person("Hope",'F', 
        new Person[]{ new Person("Molly",'F', 
        new Person[]{ new Person("Jake",'M', 
        new Person[]{ new Person("Thomas",'M', null, LocalDateTime.of(2002,10,3,17,0)),
                      new Person("Lily",'F', null, LocalDateTime.of(2002,10,3,17,0)) }
        , LocalDateTime.of(1977,1,1,0,0)) }
        , LocalDateTime.of(1947,1,1,0,0)) }
        , LocalDateTime.of(1925,1,1,0,0)) }
        , LocalDateTime.of(1905,1,1,0,0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "great-great-grandfather of Thomas";
    assertEquals(result, expected);
  }

  @Test 
  public void PersonSubTreeTest() {
    Person john = new Person("John",'M',null,LocalDateTime.of(1999,2,3,11,0));
    Person joe = new Person("Joe",'M',null,LocalDateTime.of(2000,1,18,13,1));
    Person kyle = new Person("Kyle",'M',new Person[]{john,joe},LocalDateTime.of(1970,12,16,5,0));
    Person kate = new Person("Kate",'F',null,LocalDateTime.of(1965,10,1,20,10));
    Person lily = new Person("Lily",'F',new Person[]{kate,kyle},LocalDateTime.of(1934,7,7,7,7));
    Person laura = new Person("Laura",'F',null,LocalDateTime.of(1934,7,7,7,0));
    Person luna = new Person("Luna",'F',null,LocalDateTime.of(1936,9,17,6,59));
    Person mike = new Person("Mike",'M',new Person[]{laura,lily,luna},LocalDateTime.of(1910,1,1,0,0));

    /**
     * Assert Teknonymy Name for root ancestor
     */
    String result = new TeknonymyService().getTeknonymy(mike);
    String expected = "great-grandfather of John";
    assertEquals(result, expected);

    /**
     * Assert Teknonymy Name for 1st generation person w/ descendants
     */
    result = new TeknonymyService().getTeknonymy(lily);
    expected = "grandmother of John";
    assertEquals(result, expected);

    /**
     * Assert Teknonymy Name for 2nd generation person w/ descendants
     */
    result = new TeknonymyService().getTeknonymy(kyle);
    expected = "father of John";
    assertEquals(result, expected);

    /**
     * Assert Teknonymy Name for 1st generation person (no descendants)
     */
    result = new TeknonymyService().getTeknonymy(luna);
    expected = "";
    assertEquals(result, expected);
  }

  @Test 
  public void PersonTwentyGenTwoChildrenTest() {
    int gen = 20; //20 generations
    int year = 2006; //year of birth of youngest generation
    int lastgen = (int)Math.pow(2, gen); //each parent had 2 children
    int month = 1;
    int day = 1;
    int hour = 0;
    int minute = 0;
    Person ancestor = null; //root ancestor
    Queue<Person> current = new LinkedList<>();
    List<Person> parents = new ArrayList<>();

    while (lastgen > 0) {
      Person child1 = new Person("Ivan",'M',null,LocalDateTime.of(year,month,day,hour,minute));
      Person child2 = new Person("Ilda",'F',null,LocalDateTime.of(year,month,day,hour,minute+1));
      if (minute == 58) {
        minute = 0;
        hour = (hour == 23) ? 0 : hour + 1;
        day = (day == 28) ? 1 : day + 1;
        month = (month == 12) ? 1 : month + 1;
      } else {
        minute += 2;
      }
      current.add(child1);
      current.add(child2);
      lastgen -= 2;
    }

    year -= 20; // between generations
    while (year >= 1606) {
      Person child1 = current.poll();
      Person child2 = current.poll();
      Person parent = new Person("Isolda",'F',new Person[]{child1,child2},LocalDateTime.of(year,1,1,0,0));
      parents.add(parent);
      if (current.isEmpty()) {
        current.addAll(parents);
        parents.clear();
        year -= 20;
      }
    }

    /**
     * Assert Teknonymy Name for root ancestor.
     */
    ancestor = current.poll();
    String result = new TeknonymyService().getTeknonymy(ancestor);
    StringBuilder answer = new StringBuilder("grandmother of Ivan");
    while(gen - 2 >= 1) {
      answer.insert(0,"great-");
      gen -= 1;
    }
    String expected = answer.toString();
    assertEquals(result, expected);
  }

  @Test
  public void PersonHundredGenTest() {
    int gen = 100; //100 generations
    int year = 1966;
    Person[] lastgen = new Person[]{ //five descendants in last generation, born in 2006
      new Person("Isolda", 'F', null, LocalDateTime.of(year+40,1,5,0,0)),
      new Person("Isaac", 'M', null, LocalDateTime.of(year+40,1,4,0,0)),
      new Person("Ilda", 'F', null, LocalDateTime.of(year+40,1,1,0,0)),
      new Person("Igor", 'M', null, LocalDateTime.of(year+40,1,3,0,0)),
      new Person("Ingrid", 'F', null, LocalDateTime.of(year+40,1,2,0,0)),
    };

    Person parent = new Person("John", 'M', lastgen, LocalDateTime.of(year+20,1,1,0,0));
    Person child = parent;
    while (year >= 6) {
      parent = new Person("Adam", 'M', new Person[]{child}, LocalDateTime.of(year,1,1,0,0));
      year -= 20;
      child = parent;
    }

    /**
     * Assert Teknonymy Name for root ancestor.
     */
    String result = new TeknonymyService().getTeknonymy(parent); 
    StringBuilder answer = new StringBuilder("grandfather of Ilda");
    while(gen - 2 >= 1) {
      answer.insert(0,"great-");
      gen -= 1;
    }
    String expected = answer.toString();
    assertEquals(result, expected);

    /**
     * Assert Teknonymy Name for 50th generation sub-tree ancestor.
     */
    while (gen < 50) {
      child = parent.children()[0];
      parent = child;
      gen += 1;
    }
    result = new TeknonymyService().getTeknonymy(parent); 
    while(gen - 2 >= 1) {
      answer.delete(0, "great-".length());
      gen -= 1;
    }
    expected = answer.toString();
    assertEquals(result, expected);
  }
}