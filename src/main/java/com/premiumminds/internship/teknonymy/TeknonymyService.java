package com.premiumminds.internship.teknonymy;

import com.premiumminds.internship.teknonymy.exceptions.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.List;

class TeknonymyService implements ITeknonymyService {

  /**
   * Method to get a Person Teknonymy Name
   * 
   * @param person to which define the Teknonymy Name.
   * @return String which is the Teknonymy Name, 
   * or error message if an exception occurs.
   */
  public String getTeknonymy(Person person) {
    try {
      if (person == null) return null;
      if (!hasChildren(person)) return "";
      assertTeknonymySuffix(person);
      return getDescendant(person);
    } catch (UnknownTeknonymySuffixException e){
      return e.errorMessage();
    } catch (ParentChildAgeException e) {
      return e.errorMessage();
    }
  }

  /**
   * Method to obtain a person's oldest direct descendant of the youngest generation. 
   * 
   * An iterative BFS-like algorithm is used to obtain all first direct descendants of 
   * each enqueued parent from the current generation. Once the parents' queue becomes 
   * empty, enqueue all the obtained children (the next parents) for the next iteration. 
   * If the parents' queue empties and no children were listed, the last generation and 
   * the number of generations between were reached.
   * 
   * The descendant is the oldest person among the children of the last generation.
   * 
   * @param person to which define the Teknonymy Name.
   * @throws ParentChildAgeException if a child older than their parent is found.
   * @return String which is the Teknonymy Name.
  */
  private String getDescendant(Person person) throws ParentChildAgeException {
    Queue<Person> parents = new LinkedList<>();
    for (Person child : person.children()) {
      assertParentChildAge(person, child);
      parents.add(child);
    }
    List<Person> next = new ArrayList<>();
    List<Person> explored = new ArrayList<>();
    String relation = (person.sex() == 'M') ? "father" : "mother";
    int generation = 1;

    while (true) {
      Person parent = parents.poll();
      if (hasChildren(parent)) {
        for (Person child : parent.children()) {
          assertParentChildAge(parent, child);
          next.add(child);
        }
      }
      explored.add(parent);
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
    return teknonymyBuilder(first_in_gen, relation, generation);
  }

  /**
   * Method to build the Teknonymy Name for the person.
   * 
   * @param child oldest descendant of the youngest generation.
   * @param relation suffix according to person's sex to build the Teknonymy Name.
   * @param generation number of generations between the descendant and the person.
   * @return String which is the Teknonymy Name.
  */
  private String teknonymyBuilder(Person child, String relation, int generation) {
    StringBuilder answer = new StringBuilder(relation);
    answer.append(" of ").append(child.name());
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
   * @param person to check.
   * @return boolean true if the person has children; false if otherwise. 
   */
  private boolean hasChildren(Person person) {
    return person.children() != null && person.children().length > 0;
  }

  /** Checks if it's possible to obtain a Teknonymy suffix from the person's sex.
   * 
   * @param person to which define the Teknonymy Name.
   * @throws UnknownTeknonymySuffixException if suffix can't be determined.
  */
  private void assertTeknonymySuffix(Person person) throws UnknownTeknonymySuffixException {
    if (person.sex() == null || (person.sex() != 'M' && person.sex() != 'F'))
      throw new UnknownTeknonymySuffixException(person.sex(), person.name());
  }

  /** Checks if the given child is older than their parent.
   * 
   * @param person the parent.
   * @param child the descendant.
   * @throws ParentChildAgeException if descendant is older than the parent.
  */
  private void assertParentChildAge(Person parent, Person child) throws ParentChildAgeException {
    if (child.dateOfBirth().isBefore(parent.dateOfBirth())) 
      throw new ParentChildAgeException(child.name(), parent.name());
  }
}