package com.delectable.mobile.api.models;

import com.delectable.mobile.api.Actions;

import org.json.JSONObject;

import android.util.SparseArray;

import java.util.ArrayList;


public class BaseWine extends Resource implements Actions.BaseWineActions {

    private static final String sBaseUri = API_VER + "/base_wines";

    private static final SparseArray<String> sActionUris = new SparseArray<String>();

    private static final SparseArray<String[]> sActionPayloadFields = new SparseArray<String[]>();

    static {
        sActionUris.append(A_CONTEXT, sBaseUri + "/context");

        sActionPayloadFields.append(A_CONTEXT, new String[]{
                "id",
        });
    }

    String id;

    RatingsSummaryHash ratings_summary;

    String producer_name;

    String name;

    String region_id;

    ArrayList<RegionPath> region_path;

    ArrayList<VarietalsHash> varietal_composition;

    ArrayList<WineProfile> wine_profiles;

    String default_wine_profile_id;

    PhotoHash photo;

    String description;

    String context;

    String e_tag;

    @Override
    public String[] getPayloadFieldsForAction(int action) {
        return sActionPayloadFields.get(action);
    }

    @Override
    public String getResourceUrlForAction(int action) {
        return sActionUris.get(action);
    }

    @Override
    public BaseWine parsePayloadForAction(JSONObject jsonResult, int action) {
        JSONObject payloadObj = jsonResult.optJSONObject("payload");
        BaseWine newResource = null;
        if (payloadObj != null && payloadObj.optJSONObject("base_wine") != null) {
            newResource = buildFromJson(payloadObj.optJSONObject("base_wine"),
                    this.getClass());
        }

        return newResource;
    }

    public String getResourceContextForAction(int action) {
        return "profile";
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public RatingsSummaryHash getRatingsSummary() {
        return ratings_summary;
    }

    public void setRatingsSummary(RatingsSummaryHash ratings_summary) {
        this.ratings_summary = ratings_summary;
    }

    public String getProducerName() {
        return producer_name;
    }

    public void setProducerName(String producer_name) {
        this.producer_name = producer_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegionId() {
        return region_id;
    }

    public void setRegionId(String region_id) {
        this.region_id = region_id;
    }

    public ArrayList<RegionPath> getRegionPath() {
        return region_path;
    }

    public void setRegionPath(ArrayList<RegionPath> region_path) {
        this.region_path = region_path;
    }

    public ArrayList<VarietalsHash> getVarietalComposition() {
        return varietal_composition;
    }

    public void setVarietalComposition(ArrayList<VarietalsHash> varietal_composition) {
        this.varietal_composition = varietal_composition;
    }

    public ArrayList<WineProfile> getWineProfiles() {
        return wine_profiles;
    }

    public void setWineProfiles(ArrayList<WineProfile> wine_profiles) {
        this.wine_profiles = wine_profiles;
    }

    public String getDefaultWineProfileId() {
        return default_wine_profile_id;
    }

    public void setDefaultWineProfileId(String default_wine_profile_id) {
        this.default_wine_profile_id = default_wine_profile_id;
    }

    public PhotoHash getPhoto() {
        return photo;
    }

    public void setPhoto(PhotoHash photo) {
        this.photo = photo;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getETag() {
        return e_tag;
    }

    public void setETag(String e_tag) {
        this.e_tag = e_tag;
    }

    @Override
    public String toString() {
        return "BaseWine{" +
                "id='" + id + '\'' +
                ", ratings_summary=" + ratings_summary +
                ", producer_name='" + producer_name + '\'' +
                ", name='" + name + '\'' +
                ", region_id='" + region_id + '\'' +
                ", region_path=" + region_path +
                ", varietal_composition=" + varietal_composition +
                ", wine_profiles=" + wine_profiles +
                ", default_wine_profile_id='" + default_wine_profile_id + '\'' +
                ", photo=" + photo +
                ", description='" + description + '\'' +
                ", context='" + context + '\'' +
                ", e_tag='" + e_tag + '\'' +
                '}';
    }

    public class RegionPath {

        String id;

        String name;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "RegionPath{" +
                    "id='" + id + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
