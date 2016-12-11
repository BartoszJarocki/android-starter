package com.starter.ui.contributors;

import android.support.test.rule.ActivityTestRule;
import com.starter.R;
import com.starter.util.DeviceAnimationTestRule;
import io.appflate.restmock.RESTMockServer;
import io.appflate.restmock.RequestsVerifier;
import okhttp3.mockwebserver.MockResponse;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static io.appflate.restmock.utils.RequestMatchers.pathContains;
import static org.hamcrest.CoreMatchers.not;

public class ContributorsActivityTest {
    private static final String CONTRIBUTORS_JSON = "contributors.json";
    private static final String EMPTY_JSON = "empty_array.json";
    private static final String CONTRIBUTORS_ENDPOINT = "contributors";

    @ClassRule static public DeviceAnimationTestRule
        deviceAnimationTestRule = new DeviceAnimationTestRule();

    @Rule public ActivityTestRule<ContributorsActivity> rule =
        new ActivityTestRule<>(ContributorsActivity.class, true, false);

    @Before
    public void setUp() {
        RESTMockServer.reset();
    }

    @Test
    public void testContributorsOk() {
        RESTMockServer.whenGET(pathContains(CONTRIBUTORS_ENDPOINT))
            .thenReturnFile(CONTRIBUTORS_JSON);
        rule.launchActivity(null);

        onView(withId(R.id.error_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.empty_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.swipe_to_refresh_view)).check(matches(isDisplayed()));

        RequestsVerifier.verifyRequest(pathContains(CONTRIBUTORS_ENDPOINT)).invoked();
    }

    @Test
    public void testContributorsEmpty() {
        RESTMockServer.whenGET(pathContains(CONTRIBUTORS_ENDPOINT)).thenReturnFile(EMPTY_JSON);
        rule.launchActivity(null);

        onView(withId(R.id.error_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.swipe_to_refresh_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.empty_view)).check(matches(isDisplayed()));

        RequestsVerifier.verifyRequest(pathContains(CONTRIBUTORS_ENDPOINT)).invoked();
    }

    @Test
    public void testContributorsServerError() {
        MockResponse errorResponse = new MockResponse();
        errorResponse.setResponseCode(500);
        errorResponse.setBody("Internal server error.");

        RESTMockServer.whenGET(pathContains(CONTRIBUTORS_ENDPOINT)).thenReturn(errorResponse);
        rule.launchActivity(null);

        onView(withId(R.id.swipe_to_refresh_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.empty_view)).check(matches(not(isDisplayed())));
        onView(withId(R.id.error_view)).check(matches(isDisplayed()));

        RequestsVerifier.verifyRequest(pathContains(CONTRIBUTORS_ENDPOINT)).invoked();
    }
}
