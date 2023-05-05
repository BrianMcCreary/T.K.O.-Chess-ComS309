package com.example.tko_chess;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
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

@RunWith(AndroidJUnit4ClassRunner.class)
public class LogInActivityTest {

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
    public void login(){
        String username = "tester8";
        String password = "password";

        onView(withId(R.id.UsernameText)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.LoginButton)).perform(click());

        intended(hasComponent(MainMenuActivity.class.getName()));


        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
    }

    @Test
    public void login2(){
        String username = "tester10";
        String password = "hello";

        onView(withId(R.id.UsernameText)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.LoginButton)).perform(click());

        onView(withId(R.id.LoginErrorText)).check(matches(withText("false")));

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
    }
}
