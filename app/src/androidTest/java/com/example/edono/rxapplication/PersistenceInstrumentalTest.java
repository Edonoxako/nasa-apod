package com.example.edono.rxapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.example.edono.rxapplication.data.AndroidSQLiteApodRepository;
import com.example.edono.rxapplication.data.ApodDataConverter;
import com.example.edono.rxapplication.data.DBHelper;
import com.example.edono.rxapplication.domain.model.ApodData;
import com.example.edono.rxapplication.domain.model.ApodMediaType;
import com.example.edono.rxapplication.util.Constants;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import io.reactivex.subscribers.TestSubscriber;

import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_DATE;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_EXPLANATION;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_HD_URL;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_MEDIA_TYPE;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_TITLE;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.COLUMN_NAME_URL;
import static com.example.edono.rxapplication.data.ApodDataContract.SingleApod.TABLE_NAME;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class PersistenceInstrumentalTest {

    private DBHelper dbHelper;
    private AndroidSQLiteApodRepository repository;
    private SimpleDateFormat format = new SimpleDateFormat(Constants.DATE_FORMAT, Locale.US);

    @Before
    public void setUp() throws Exception {
        InstrumentationRegistry.getTargetContext().deleteDatabase(DBHelper.DATABASE_NAME);
        dbHelper = new DBHelper(InstrumentationRegistry.getTargetContext());
        repository = new AndroidSQLiteApodRepository(dbHelper, new ApodDataConverter());
    }

    @After
    public void tearDown() throws Exception {
        dbHelper.close();
    }

    @Test
    public void testSaveApod() throws Exception {
        ApodData apodData = createTestApodData();
        repository.saveApod(apodData);
        assertThatSaved(apodData);
    }

    @Test
    public void testFindApodByDate() throws Exception {
        TestSubscriber<ApodData> subscriber = new TestSubscriber<>();
        ApodData apodData = createTestApodData();
        repository.saveApod(apodData);

        repository.findApodByDate(apodData.getDate())
                .subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertValue(apodData);
    }

    @Test
    public void testFindApodByDate_NoApodWithSuchDate() throws Exception {
        TestSubscriber<ApodData> subscriber = new TestSubscriber<>();
        ApodData apodData = createTestApodData();
        repository.saveApod(apodData);

        Calendar date = Calendar.getInstance();
        date.setTime(format.parse("2017-01-11"));
        apodData.setDate(date);

        repository.findApodByDate(apodData.getDate())
                .subscribe(subscriber);

        subscriber.assertNoErrors();
        subscriber.assertNoValues();
    }

    public ApodData createTestApodData() throws MalformedURLException, ParseException {
        Calendar date = Calendar.getInstance();
        date.setTime(format.parse("2017-03-11"));
        URL testUrl = new URL("http://test-url");
        String explanation = "test-explanation";
        String title = "test-title";
        String mediaType = "test-media-type";
        return new ApodData(
                date,
                explanation,
                testUrl,
                ApodMediaType.fromString(mediaType),
                title,
                testUrl
        );
    }

    public void assertThatSaved(ApodData apodData) throws MalformedURLException {
        String[] projection = {
                COLUMN_NAME_TITLE,
                COLUMN_NAME_EXPLANATION,
                COLUMN_NAME_DATE,
                COLUMN_NAME_HD_URL,
                COLUMN_NAME_URL,
                COLUMN_NAME_MEDIA_TYPE
        };

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query(
                TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );

        cursor.moveToFirst();
        String savedTitle = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
        String savedExplanation = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_EXPLANATION));
        String savedDate = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_DATE));
        String savedHdUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_HD_URL));
        String savedUrl = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_URL));
        String savedMediaType = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_MEDIA_TYPE));
        cursor.close();

        assertThat(apodData.getTitle(), is(savedTitle));
        assertThat(apodData.getExplanation(), is(savedExplanation));
        assertThat(apodData.getMediaType(), is(ApodMediaType.fromString(savedMediaType)));
        assertThat(apodData.getHdUrl(), is(new URL(savedHdUrl)));
        assertThat(apodData.getUrl(), is(new URL(savedUrl)));
        assertThat(format.format(apodData.getDate().getTime()), is(savedDate));
    }
}
