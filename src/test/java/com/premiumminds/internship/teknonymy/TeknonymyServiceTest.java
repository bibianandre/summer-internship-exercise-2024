package com.premiumminds.internship.teknonymy;

import com.premiumminds.internship.teknonymy.TeknonymyService;
import com.premiumminds.internship.teknonymy.Person;

import static org.junit.Assert.assertEquals;

import java.time.LocalDateTime;
import java.beans.Transient;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
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
    String expected = "Person object is null";
    assertEquals(result, expected);
  }

  @Test
  public void PersonNoNullChildrenTest() {
    Person person = new Person("John",'M',new Person[]{}, LocalDateTime.of(1046, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "";
    assertEquals(result, expected);
  }

  @Test
  public void MotherFourChildrenTest() {
    Person person = new Person("Eve", 'F', 
        new Person[]{ new Person("Jack",'M', null, LocalDateTime.of(2001, 1, 1, 17, 6)),
                      new Person("Jane",'F', null, LocalDateTime.of(2001, 1, 1, 17, 5)),
                      new Person("Joe",'M', null, LocalDateTime.of(2001, 1, 1, 17, 0)),
                      new Person("Jill",'F', null, LocalDateTime.of(2003, 1, 1, 0, 0))
        }, LocalDateTime.of(1977, 1, 1, 0, 0));
      String result = new TeknonymyService().getTeknonymy(person);
      String expected = "mother of Joe";
      assertEquals(result, expected);
  }

  @Test
  public void FatherTenChildrenTest() {
    Person person = new Person("Mike", 'M', 
        new Person[]{ new Person("Jack",'M', null, LocalDateTime.of(2001, 1, 1, 0, 0)),
                      new Person("Jane",'F', null, LocalDateTime.of(2002, 1, 1, 0, 0)),
                      new Person("Joe",'M', null, LocalDateTime.of(1999, 1, 1, 0, 0)),
                      new Person("Jake",'M', null, LocalDateTime.of(1994, 1, 1, 0, 0)),
                      new Person("Jenna",'M', null, LocalDateTime.of(2004, 1, 1, 0, 0)),
                      new Person("Jonas",'M', null, LocalDateTime.of(1995, 1, 1, 0, 0)),
                      new Person("Jessi",'M', null, LocalDateTime.of(1997, 1, 1, 0, 0)),
                      new Person("Josh",'M', null, LocalDateTime.of(2000, 1, 1, 0, 0)),
                      new Person("Joan",'F', null, LocalDateTime.of(2004, 1, 1, 0, 0)),
                      new Person("Jill",'F', null, LocalDateTime.of(1998, 1, 1, 0, 0))
        }, LocalDateTime.of(1977, 1, 1, 0, 0));
      String result = new TeknonymyService().getTeknonymy(person);
      String expected = "father of Jake";
      assertEquals(result, expected);
  }

  @Test
  public void TwinsGreatGreatGrandchildrenTest() {
    Person person = new Person("John",'M',
        new Person[]{ new Person("Hope",'F', 
        new Person[]{ new Person("Molly",'F', 
        new Person[]{ new Person("Jake",'M', 
        new Person[]{ new Person("Thomas",'M', null, LocalDateTime.of(2002, 10, 3, 17, 0)),
                      new Person("Lily",'F', null, LocalDateTime.of(2002, 10, 3, 17, 0)) }
        , LocalDateTime.of(1977, 1, 1, 0, 0)) }
        , LocalDateTime.of(1947, 1, 1, 0, 0)) }
        , LocalDateTime.of(1925, 1, 1, 0, 0)) }
        , LocalDateTime.of(1905, 1, 1, 0, 0));
    String result = new TeknonymyService().getTeknonymy(person);
    String expected = "great-great-grandfather of Thomas";
    assertEquals(result, expected);
  }

  @Test 
  public void PersonTwentyGenTwoChildrenTest() {
    int gen = 20;//20 generations
    int year = 2006;
    int month = 1;
    int day = 1;
    int offspring = (int)Math.pow(2, gen); //each parent has 2 children
    Person ancestor = null;
    Queue<Person> current = new LinkedList<>();
    List<Person> parents = new LinkedList<>();

    while (offspring > 0) {
      Person child1 = new Person("Ivan",'M',null,LocalDateTime.of(year, month, day, 0, 0));
      Person child2 = new Person("Ilda",'F',null,LocalDateTime.of(year, month, day+1, 0, 0));
      if (day == 27) {
        day = 1;
        month = (month >= 12) ? 1 : month + 1;
      } else {
        day += 2;
      }
      current.add(child1);
      current.add(child2);
      offspring -= 2;
    }

    year -= 20;
    while (year >= 1606) {
      Person child1 = current.poll();
      Person child2 = current.poll();
      Person parent = new Person("Isolda",'F',new Person[]{child1, child2},LocalDateTime.of(year, 1, 1, 0, 0));
      parents.add(parent);
      if (current.isEmpty()) {
        current.addAll(parents);
        parents.clear();
        year -= 20;
      }
    }

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
    int year = 1986;
    Person ancestor = null;
    Person child = new Person("Ilda", 'F', null, LocalDateTime.of(year+20, 1, 1, 0, 0));

    while (year >= 6) {
      ancestor = new Person("Adam", 'M', new Person[]{child}, LocalDateTime.of(year, 1, 1, 0, 0));
      year -= 20;
      child = ancestor;
    }
    String result = new TeknonymyService().getTeknonymy(ancestor);
    StringBuilder answer = new StringBuilder("grandfather of Ilda");
    while(gen - 2 >= 1) {
      answer.insert(0,"great-");
      gen -= 1;
    }
    String expected = answer.toString();
    assertEquals(result, expected);
  }
}