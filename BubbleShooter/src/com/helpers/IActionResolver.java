package com.helpers;

/**
 * Created with IntelliJ IDEA.
 * User: Lars Davidsen
 * Date: 12-03-13
 * Time: 19:49
 * To change this template use File | Settings | File Templates.
 */
public interface IActionResolver {
    public void bootstrap();
    public void showScoreloop();
    public void submitScore(int mode, int score);
    public void refreshScores();
}
