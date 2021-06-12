package com.example.aveia;

import java.util.ArrayList;

/**
 * Class that produces a random number between two values - min and max.
 * It also allows the option for a random number to not be repeated within specified range.
 */
public class RandomNumberGenerator {

    private final int AVOID_REPETITION_SET = 10;
    private int preferredSize = AVOID_REPETITION_SET;
    private int result;
    private ArrayList<Integer> pastResultsSet = new ArrayList<>();
    private int listPointer = 0;

    /**
     * Produces a random number whilst not allowing repeated values for AVOID_REPETITION_SET consecutive times.
     *
     * @param min the minimum value of the range required.
     * @param max the maximum value of the range required.
     * @return Integer number randomly generated between two values, min..max
     */
    public int getRandomNumAvoidRepetition(int min, int max) {

        while (true) {//loops until it gets a correct result
            result = generateRandomNumber(min, max);
            if (!isResultRepeated(result)) {
                break;
            }
        }
        return result;
    }

    /**
     * Produces a random number without comparing to a set of values.
     *
     * @param min min
     * @param max max
     * @return
     */
    public int getRandomNum(int min, int max) {
        result = generateRandomNumber(min, max);
        return result;
    }

    /**
     * Checks if the result is already contained within a list of previously generated results.
     *
     * @param result the randomly generated result
     * @return returns true if the result is contained within the list and false if the value is not
     * in the list.
     */
    private boolean isResultRepeated(int result) {
        boolean isRepeated = false;
        if (!pastResultsSet.isEmpty()) {
            for (int i = 0; i < pastResultsSet.size(); i++) {
                if (pastResultsSet.get(i) == result) {
                    isRepeated = true;
                    break;
                } else {//case when the element has not been repeated
                    addResult(result);
                }
            }
        } else {//the first element to be added to the array list
            addResult(result);
        }
        return isRepeated;
    }

    /**
     * Adds a result to the list of result sets.
     *
     * @param result
     */
    private void addResult(int result) {
        pastResultsSet.add(listPointer, result);
        addToPointer();
    }

    /**
     * Adds value to a pointer, whilst ensuring that it loops over a certain value,
     * keeping the array list of already generated numbers within a desired size.
     * The pointer is used to override a currentButtonPosition within the list.
     */
    private void addToPointer() {
        listPointer++;
        if (listPointer == preferredSize)
            listPointer = 0;//resets the pointer to 0 if the size reaches AVOID_REPETITION_SET
    }

    /**
     * Allows to clear the result set, in case they choose to change value combinations.
     */
    public void resetResultSet() {
        if(!pastResultsSet.isEmpty()) {
            pastResultsSet.clear();
        }
    }

    /**
     * Allows to set the size of the pastResultsSet.
     *
     * @param size
     */
    public void setPreferredSize(int size) {
        if (size <= 0) {
            size = 1;
        }
        preferredSize = size;
    }


    /**
     * Generates a random number between two values.
     * @param min the minimum value.
     * @param max the maximum value.
     * @return result of number generation.
     */
    private int generateRandomNumber(int min, int max) {
        return max - ((int) Math.round((Math.random()) * (max - min)));
    }
}