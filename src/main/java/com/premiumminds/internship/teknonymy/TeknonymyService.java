package com.premiumminds.internship.teknonymy;

import com.premiumminds.internship.teknonymy.Person;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

class TeknonymyService implements ITeknonymyService {

  /**
   * Method to get a Person Teknonymy Name
   * 
   * @param Person person
   * @return String which is the Teknonymy Name 
   */
  public String getTeknonymy(Person person) {
    if (person == null)
      return "Person object is null";
    if (!hasChildren(person))
      return "";

    String relation = (person.sex() == 'M') ? "father" : "mother";
    return getDescendant(person, relation);
  }

  /**
   * Method to obtain a Person's oldest direct descendant from the youngest generation. 
   * 
   * An iterative BFS-like algorithm is used to obtain all first direct descendants 
   * of each enqueued parent from the current generation. Once the parents' queue
   * becomes empty, enqueue all the obtained children (the next parents) for the 
   * next interation. If the parents' queue empties and no children were listed, 
   * the last generation was reached along the generational distance.
   * 
   * The descendant is the oldest person among the children of the last generation.
   * 
   * @param person to which define the Teknonymy Name.
   * @param relation the relation suffix according to the person's gender to 
   *                 build the Teknonymy Name.
   * @return String which is the Teknonymy Name.
   */
  private String getDescendant(Person person, String relation) {
    Queue<Person> parents = new LinkedList<>();
    List<Person> next = new LinkedList<>();
    List<Person> explored = new LinkedList<>();
    int generation = 1;

    parents.addAll(List.of(person.children()));

    while (true) {
      Person parent = parents.poll();
      explored.add(parent);
      if (hasChildren(parent)) {
        for (Person child : parent.children())
          next.add(child);
      }
      if (parents.isEmpty()) {
        if (next.isEmpty()) {
          break; //last generation was reached
        } else {
          parents.addAll(next); //enqueue children for next iteration
          explored.clear();
          next.clear();
          generation++;
        }
      }
    }

    Person first_in_gen = null;
    for (Person lastgen : explored) {
      if (first_in_gen == null || lastgen.dateOfBirth().isBefore(first_in_gen.dateOfBirth()))
        first_in_gen = lastgen;
    }

    StringBuilder answer = new StringBuilder(relation);
    answer.append(" of ").append(first_in_gen.name());
    if (generation >= 2) {
      answer.insert(0, "grand");
      for (int i = 2; i < generation; i++)
        answer.insert(0, "great-");
    }

    String teknonymy = answer.toString();
    return teknonymy;
  }
  
  /**
   * Method to verify if a given person has children.
   * 
   * @param Person person
   * @return boolean true if the person has children; false if otherwise. 
   */
  private boolean hasChildren(Person person) {
    return person.children() != null && person.children().length > 0;
  }
}