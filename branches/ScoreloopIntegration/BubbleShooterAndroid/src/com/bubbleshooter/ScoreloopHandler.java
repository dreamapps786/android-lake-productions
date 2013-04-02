package com.bubbleshooter;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import com.scoreloop.client.android.core.controller.*;
import com.scoreloop.client.android.core.model.*;
import com.scoreloop.client.android.ui.ScoreloopManagerSingleton;

import java.util.List;


/**
 * Created with IntelliJ IDEA.
 * User: Lars Davidsen
 * Date: 12-03-13
 * Time: 19:56
 * To change this template use File | Settings | File Templates.
 */
public class ScoreloopHandler {
    private Activity1 mContext; //Your class here which extends AndroidApplication

    public ScoreloopHandler(Activity1 context) {
        mContext = context;
    }

    public void submitScore(int scoreValue) {
        Log.d("Scoreloop", "submitting score");
        final Score score = new Score((double) scoreValue, null);
        final ScoreController myScoreController = new ScoreController(new ScoreSubmitObserver());
        myScoreController.submitScore(score);
    }

    public void getRankingForScore(int scoreValue) {
        Score score = new Score((double) scoreValue, null);
        RankingController controller = new RankingController(new RankingRequestObserver());
        controller.loadRankingForScore(score);
    }

    public void getGlobalHighscores() {
        ScoresController myScoresController = new ScoresController(new GlobalRankObserver());
        myScoresController.setSearchList(SearchList.getGlobalScoreSearchList());
        myScoresController.setRangeLength(15);
        myScoresController.loadRangeForUser(Session.getCurrentSession().getUser());
    }

    public void getTodayHighscores() {
        ScoresController myScoresController = new ScoresController(new DailyRankObserver());
        myScoresController.setSearchList(SearchList.getTwentyFourHourScoreSearchList());
        myScoresController.setRangeLength(15);
        myScoresController.loadRangeForUser(Session.getCurrentSession().getUser());
    }

    private class RankingRequestObserver implements RequestControllerObserver {

        @Override
        public void requestControllerDidFail(RequestController arg0,
                                             Exception arg1) {

        }

        @Override
        public void requestControllerDidReceiveResponse(RequestController controller) {
            Ranking ranking = ((RankingController) controller).getRanking();
            final int rank = ranking.getRank();
            mContext.postRunnable(new Runnable() {
                @Override
                public void run() {
                    //This code runs on the "libgdx" thread.
                    //Use a local variable to store the rank in the MainProject and set it here
                }
            });
        }
    }

    private class ScoreSubmitObserver implements RequestControllerObserver {

        @Override
        public void requestControllerDidFail(final RequestController requestController,
                                             final Exception exception) {
            Log.d("Scoreloop", "score submitted exception " + exception.getMessage());
        }

        @Override
        public void requestControllerDidReceiveResponse(final RequestController requestController) {
            Log.d("Scoreloop", "score submitted successsfully");

        }
    }

    private class DailyRankObserver implements RequestControllerObserver {

        @Override
        public void requestControllerDidFail(RequestController arg0,
                                             Exception arg1) {

        }

        @Override
        public void requestControllerDidReceiveResponse(RequestController controller) {
            final List<Score> retrievedScores = ((ScoresController) controller).getScores();
            mContext.postRunnable(new Runnable() {
                @Override
                public void run() {
                    //This code runs on the "libgdx" thread.
                    //Use a local variable to store the daily ranks in the MainProject and set it here
                }
            });
        }
    }

    private class GlobalRankObserver implements RequestControllerObserver {

        @Override
        public void requestControllerDidFail(RequestController arg0,
                                             Exception arg1) {

        }

        @Override
        public void requestControllerDidReceiveResponse(RequestController controller) {
            final List<Score> retrievedScores = ((ScoresController) controller).getScores();
            mContext.postRunnable(new Runnable() {
                @Override
                public void run() {
                    //This code runs on the "libgdx" thread.
                    //Use a local variable to store the global ranks in the MainProject and set it here
                }
            });
        }

    }

}
