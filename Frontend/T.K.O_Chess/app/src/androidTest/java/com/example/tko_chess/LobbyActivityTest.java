package com.example.tko_chess;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;


import androidx.lifecycle.Lifecycle;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.filters.LargeTest;
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.StringEndsWith.endsWith;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import android.util.Log;

import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner.class)
public class LobbyActivityTest {

    private static final int SIMULATED_DELAY_MS = 500;
    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule = new ActivityScenarioRule<>(LogInActivity.class);

    @Before
    public void before(){
        Intents.init();
    }
    @After
    public void after(){
        Intents.release();
    }

    @Test
    public void LobbyTest(){
        String username = "tester8";
        String password = "password";

        onView(withId(R.id.UsernameText)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.LoginButton)).perform(click());
        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.MenuToChessBtn)).perform(click());
        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.HostGameBtn)).perform(click());
        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
        onView(withId(R.id.Player1Btn)).perform(click());

        onView(withId(R.id.ReadyBtn)).perform(click());
        onView(withId(R.id.NotReadyBtn)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.Player2Btn)).perform(click());
        onView(withId(R.id.ReadyBtn)).perform(click());
        onView(withId(R.id.NotReadyBtn)).perform(click());
        onView(withId(R.id.SpectatorBtn)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.LobbyToHostJoinBtn)).perform(click());
        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
        onView(withId(R.id.HorJGametoMenuBtn)).perform(click());
        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
        onView(withId(R.id.MenuToProfileBtn)).perform(click());

        intended(hasComponent(ProfileActivity.class.getName()));


        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
    }
}
