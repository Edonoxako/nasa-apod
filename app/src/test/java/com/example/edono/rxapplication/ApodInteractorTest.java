package com.example.edono.rxapplication;

import com.example.edono.rxapplication.domain.ApodApiFactory;
import com.example.edono.rxapplication.domain.ApodInteractor;
import com.example.edono.rxapplication.domain.ApodRepository;
import com.example.edono.rxapplication.domain.RestCacheApodInteractor;
import com.example.edono.rxapplication.domain.model.ApodData;
import com.example.edono.rxapplication.domain.model.ApodMediaType;
import com.example.edono.rxapplication.util.Constants;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import com.google.gson.Gson;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Observable;

import io.reactivex.Flowable;
import io.reactivex.functions.Predicate;
import io.reactivex.subscribers.TestSubscriber;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.getRequestedFor;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;
import static com.github.tomakehurst.wiremock.client.WireMock.verify;
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(MockitoJUnitRunner.class)
public class ApodInteractorTest {

    @Rule
    public WireMockRule wireMockRule =
            new WireMockRule(
                    wireMockConfig()
                            .dynamicPort()
            );

    private ApodInteractor apodInteractor;

    @Mock
    private ApodRepository repository;

    @Before
    public void setUp() throws Exception {
        ApodApiFactory factory = ApodApiFactory.create("http://localhost:" + wireMockRule.port());
        apodInteractor = new RestCacheApodInteractor(factory.getApi(), repository);

        stubFor(get(urlPathEqualTo("/planetary/apod"))
                .withQueryParam("date", equalTo("2017-01-26"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withStatus(200)
                        .withBody(getStubJsonResponseBody())));
    }

    @Test
    public void testGetByDate_fromLocalRepository() throws Exception {
        when(repository.findApodByDate(any(Calendar.class)))
                .thenReturn(Flowable.just(getStubApodData()));

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 0, 26);
        TestSubscriber<ApodData> subscriber = new TestSubscriber<>();

        apodInteractor.getApodByDate(calendar)
                .subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(getStubApodData());
        Mockito.verify(repository).findApodByDate(any(Calendar.class));
        verify(0, getRequestedFor(urlPathEqualTo("/planetary/apod")));
    }

    @Test
    public void testGetByDate_fromRemoteBackendWhenLocalRepositoryEmpty() throws Exception {
        when(repository.findApodByDate(any(Calendar.class)))
                .thenReturn(Flowable.<ApodData>empty());

        Calendar calendar = Calendar.getInstance();
        calendar.set(2017, 0, 26);
        TestSubscriber<ApodData> subscriber = new TestSubscriber<>();

        apodInteractor.getApodByDate(calendar)
                .subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(getStubApodData());
        Mockito.verify(repository).findApodByDate(any(Calendar.class));
        Mockito.verify(repository).saveApod(any(ApodData.class));
        verify(getRequestedFor(urlPathEqualTo("/planetary/apod"))
                .withQueryParam("api_key", equalTo(Constants.API_KEY))
                .withQueryParam("date", equalTo("2017-01-26")));
    }

    @Test
    public void testApodData_dateFromJson() throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyy-MM-dd", Locale.US);

        ApodData apodData = getStubApodData();

        assertEquals("2017-01-26", format.format(apodData.getDate().getTime()));
    }

    @Test
    public void testApodData_urlFromJson() throws Exception {
        URL testHdUrl = new URL("https://apod.nasa.gov/apod/image/1701/ab_moon_from_geo_orbit_med_res_jan_15_2017.jpg");
        URL testUrl = new URL("https://apod.nasa.gov/apod/image/1701/ab_moon_from_geo_orbit_med_res_jan_15_2017_1024.jpg");

        ApodData apodData = getStubApodData();

        assertEquals(testHdUrl, apodData.getHdUrl());
        assertEquals(testUrl, apodData.getUrl());
    }

    @Test
    public void testApodData_mediaTypeFromJson() throws Exception {
        ApodData apodData = getStubApodData();

        assertEquals(ApodMediaType.IMAGE, apodData.getMediaType());
    }

    private ApodData getStubApodData() {
        return new Gson().fromJson(getStubJsonResponseBody(), ApodData.class);
    }

    private String getStubJsonResponseBody() {
        return "{\n" +
                "  \"date\": \"2017-01-26\",\n" +
                "  \"explanation\": " +
                        "\"Launched last November 19 from Cape Canaveral Air Force Station, " +
                        "the satellite now known as GOES-16 can now observe planet Earth from a geostationary orbit " +
                        "22,300 miles above the equator. Its Advanced Baseline Imager captured this contrasting view of " +
                        "Earth and a gibbous Moon on January 15. The stark and airless Moon is not really the focus of GOES-16, " +
                        "though. Capable of providing a high resolution full disk image of Earth every 15 minutes in 16 spectral " +
                        "channels, the new generation satellite's instrumentation is geared to provide sharper, more detailed views " +
                        "of Earth's dynamic weather systems and enable more accurate weather forecasting. Like previous GOES weather " +
                        "satellites, GOES-16 will use the moon over our fair planet as a calibration mTarget.  Participate: " +
                        "Take an Aesthetics & Astronomy Survey\",\n" +
                "  \"hdurl\": \"http://apod.nasa.gov/apod/image/1701/ab_moon_from_geo_orbit_med_res_jan_15_2017.jpg\",\n" +
                "  \"media_type\": \"image\",\n" +
                "  \"service_version\": \"v1\",\n" +
                "  \"title\": \"GOES-16: Moon over Planet Earth\",\n" +
                "  \"url\": \"http://apod.nasa.gov/apod/image/1701/ab_moon_from_geo_orbit_med_res_jan_15_2017_1024.jpg\"\n" +
                "}";
    }
}