package com.delectable.mobile.tests;

import com.delectable.mobile.api.models.WineProfile;
import com.delectable.mobile.api.models.WineProfileMinimal;
import com.delectable.mobile.api.models.WineProfileSubProfile;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;

public class WineProfileTest extends BaseInstrumentationTestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testParseWineProfileSubprofileContext() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_wine_profiles_subprofile_ctx);
        JSONObject payload = json.optJSONObject("payload");
        JSONObject wineProfile = payload.optJSONObject("wine_profile");
        WineProfileSubProfile actualWine = mGson.fromJson(wineProfile.toString(), WineProfileSubProfile.class);

        assertEquals("50e86605a6d027d09d00025a", actualWine.getId());
        assertEquals(1, actualWine.getRatingsSummary().getAllCount());
        assertEquals(30.0, actualWine.getRatingsSummary().getAllAvg());
        assertEquals(0, actualWine.getRatingsSummary().getProCount());
        assertEquals(-1.0, actualWine.getRatingsSummary().getProAvg());
        assertEquals("2009", actualWine.getVintage());
        assertEquals("5305ba538953f6d73900543d", actualWine.getBaseWineId());
        assertEquals(21.82, actualWine.getPrice());
        assertEquals("confirmed", actualWine.getPriceStatus());
        assertEquals("subprofile", actualWine.getContext());
        assertEquals("XQr5adP35129_Q", actualWine.getETag());
        assertEquals("", actualWine.getDescription());
        assertEquals("Buy it - $21", actualWine.getPriceText());
    }

    public void testParseWineProfileMinimalContext() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_wine_profiles_minimal_ctx);
        JSONObject payload = json.optJSONObject("payload");
        JSONObject wineProfile = payload.optJSONObject("wine_profile");
        WineProfileMinimal actualWine = mGson.fromJson(wineProfile.toString(), WineProfileMinimal.class);

        assertEquals("50e86605a6d027d09d00025a", actualWine.getId());
        assertEquals("2009", actualWine.getVintage());
        assertEquals("Napa Ridge", actualWine.getProducerName());
        assertEquals("Napa Valley Pinot Noir", actualWine.getName());
        assertEquals("5305ba538953f6d73900543d", actualWine.getBaseWineId());
        assertEquals(21.82, actualWine.getPrice());
        assertEquals("confirmed", actualWine.getPriceStatus());

        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f.jpg",
                actualWine.getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_nano.jpg",
                actualWine.getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_micro.jpg",
                actualWine.getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_thumb.jpg",
                actualWine.getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_250x250.jpg",
                actualWine.getPhoto().get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_medium.jpg",
                actualWine.getPhoto().getMediumUrl());
        assertEquals("minimal", actualWine.getContext());
        assertEquals("SF1craz1xak8Uw", actualWine.getETag());
        assertEquals("", actualWine.getDescription());
        assertEquals("Buy it - $21", actualWine.getPriceText());
    }

    public void testWineProfileParcelable() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_wine_profiles_subprofile_ctx);
        JSONObject payload = json.optJSONObject("payload");
        JSONObject wineProfile = payload.optJSONObject("wine_profile");
        WineProfileSubProfile expectedWine = mGson.fromJson(wineProfile.toString(), WineProfileSubProfile.class);

        Parcel testParcel = Parcel.obtain();

        int originalPos = testParcel.dataPosition();
        expectedWine.writeToParcel(testParcel, 0);

        // Must reset Data position!!
        testParcel.setDataPosition(originalPos);

        WineProfileSubProfile actualWine = WineProfileSubProfile.CREATOR.createFromParcel(testParcel);

        assertEquals("50e86605a6d027d09d00025a", actualWine.getId());
        assertEquals(1, actualWine.getRatingsSummary().getAllCount());
        assertEquals(30.0, actualWine.getRatingsSummary().getAllAvg());
        assertEquals(0, actualWine.getRatingsSummary().getProCount());
        assertEquals(-1.0, actualWine.getRatingsSummary().getProAvg());
        assertEquals("2009", actualWine.getVintage());
        assertEquals("5305ba538953f6d73900543d", actualWine.getBaseWineId());
        assertEquals(21.82, actualWine.getPrice());
        assertEquals("confirmed", actualWine.getPriceStatus());
        assertEquals("subprofile", actualWine.getContext());
        assertEquals("XQr5adP35129_Q", actualWine.getETag());
        assertEquals("", actualWine.getDescription());
        assertEquals("Buy it - $21", actualWine.getPriceText());
    }

    public void testWinePhotoHash() throws JSONException {
        JSONObject json = loadJsonObjectFromResource(R.raw.test_wine_profiles_minimal_ctx);
        JSONObject payload = json.optJSONObject("payload");
        JSONObject wineProfile = payload.optJSONObject("wine_profile");
        WineProfileMinimal expectedWine = mGson.fromJson(wineProfile.toString(), WineProfileMinimal.class);

        Parcel testParcel = Parcel.obtain();

        int originalPos = testParcel.dataPosition();
        expectedWine.writeToParcel(testParcel, 0);

        // Must reset Data position!!
        testParcel.setDataPosition(originalPos);

        WineProfileMinimal actualWine = WineProfileMinimal.CREATOR.createFromParcel(testParcel);

        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f.jpg",
                actualWine.getPhoto().getUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_nano.jpg",
                actualWine.getPhoto().getNanoUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_micro.jpg",
                actualWine.getPhoto().getMicroUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_thumb.jpg",
                actualWine.getPhoto().getThumbUrl());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_250x250.jpg",
                actualWine.getPhoto().get250Url());
        assertEquals(
                "https://s3.amazonaws.com/delectableCapturedPhotos/tim-park-1357406485-f70bd657b34f_medium.jpg",
                actualWine.getPhoto().getMediumUrl());
    }
}
