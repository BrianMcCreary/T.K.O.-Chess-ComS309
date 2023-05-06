package com.example.tko_chess;

import org.junit.After;
import org.junit.Before;
import org.junit.FixMethodOrder;
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

import android.util.Log;

import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
@RunWith(AndroidJUnit4ClassRunner.class)
public class EditProfileActivityTest {

    private static final int SIMULATED_DELAY_MS = 500;

    @Rule
    public ActivityScenarioRule<LogInActivity> activityRule = new ActivityScenarioRule<>(LogInActivity.class);
    @Rule
    public ActivityScenarioRule<EditProfileActivity> activityRule2 = new ActivityScenarioRule<>(EditProfileActivity.class);

    @Before
    public void before(){
        Intents.init();
    }
    @After
    public void after(){
        Intents.release();
    }

    @Test
    public void Test1(){ // change username
        String username = "tester8";
        String password = "password";

        onView(withId(R.id.UsernameText)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.LoginButton)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.MenuToProfileBtn)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.editprofilebtn)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        String currentUsername = "tester8";
        String newUsername = "testUsername";

        onView(withId(R.id.CurrUsernameEditText)).perform(typeText(currentUsername), closeSoftKeyboard());
        onView(withId(R.id.CurrPasswordEditText)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.NewUsernameEditText)). perform(typeText(newUsername), closeSoftKeyboard());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.UpdateUsernameBtn)).perform(click());
        onView(withId(R.id.ChangeSuccessfulText)).check(matches(withText("Username successfully changed.")));

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
    }

    @Test
    public void Test2(){ //change Username Back
        String currentUsername = "testUsername";
        String newUsername = "tester8";
        String password = "password";

        String username = "testUsername";

        onView(withId(R.id.UsernameText)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.LoginButton)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.MenuToProfileBtn)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.editprofilebtn)).perform(click());

        onView(withId(R.id.CurrUsernameEditText)).perform(typeText(currentUsername), closeSoftKeyboard());
        onView(withId(R.id.CurrPasswordEditText)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.NewUsernameEditText)). perform(typeText(newUsername), closeSoftKeyboard());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.UpdateUsernameBtn)).perform(click());
        onView(withId(R.id.ChangeSuccessfulText)).check(matches(withText("Username successfully changed.")));

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
    }

    @Test
    public void Test3(){ //change Password
        String username = "tester8";
        String password = "password";

        onView(withId(R.id.UsernameText)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText(password), closeSoftKeyboard());

        onView(withId(R.id.LoginButton)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.MenuToProfileBtn)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.editprofilebtn)).perform(click());

        String currentUsername = "tester8";
        String newPassword = "password1";

        onView(withId(R.id.CurrUsernameEditText)).perform(typeText(currentUsername), closeSoftKeyboard());
        onView(withId(R.id.CurrPasswordEditText)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordEditText)). perform(typeText(newPassword), closeSoftKeyboard());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.UpdatePasswordBtn)).perform(click());
        onView(withId(R.id.ChangeSuccessfulText)).check(matches(withText("Password successfully changed.")));

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
    }

    @Test
    public void Test4(){ // change Password Back
        String username = "tester8";
        String password = "password1";

        onView(withId(R.id.UsernameText)).perform(typeText(username), closeSoftKeyboard());
        onView(withId(R.id.PasswordText)).perform(typeText(password), closeSoftKeyboard());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.LoginButton)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.MenuToProfileBtn)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.editprofilebtn)).perform(click());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        String currentUsername = "tester8";
        String newPassword = "password";

        onView(withId(R.id.CurrUsernameEditText)).perform(typeText(currentUsername), closeSoftKeyboard());
        onView(withId(R.id.CurrPasswordEditText)).perform(typeText(password), closeSoftKeyboard());
        onView(withId(R.id.NewPasswordEditText)). perform(typeText(newPassword), closeSoftKeyboard());

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }

        onView(withId(R.id.UpdatePasswordBtn)).perform(click());
        onView(withId(R.id.ChangeSuccessfulText)).check(matches(withText("Password successfully changed.")));

        try{
            Thread.sleep(SIMULATED_DELAY_MS);
        }catch (InterruptedException e){
        }
    }
}
