package org.dicio.skill.output;

import android.view.View;

import androidx.annotation.NonNull;

import org.dicio.skill.util.CleanableUp;

/**
 * An interface that has to be implemented by classes that wish to display the output generated by
 * skills
 */
public interface GraphicalOutputDevice extends CleanableUp {

    /**
     * Displays graphical output to the user
     * @param graphicalOutput a view to show, usually encapsulated inside an output container
     */
    void display(@NonNull View graphicalOutput);

    /**
     * Displays temporary graphical output to the user. After either {@link #display(View)} or
     * this function are called again the temporary output will be removed and replaced with the
     * newly provided one. This should be used, for example, while loading things, so that the
     * loading state can then be removed and replaced by the loaded content.
     * @param graphicalOutput a view to show, usually encapsulated inside an output container, that
     *                        will be removed whenever a new view is requested to be displayed
     */
    void displayTemporary(@NonNull View graphicalOutput);

    /**
     * Removes the temporary view previously added with {@link #displayTemporary(View)}, if it is
     * present, otherwise does nothing
     */
    void removeTemporary();

    /**
     * Adds a divider the output to separate between different things. Does not do anything if
     * called before any {@link #display(View)}, since it makes no sense to add a divider as the
     * first view.
     */
    void addDivider();
}
