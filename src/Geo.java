import com.google.gson.annotations.SerializedName;

public class Geo {

    @SerializedName("lat")
    private double lat;

    @SerializedName("lng")
    private double lng;

    public Double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public Geo(Builder builder) {
        this.lat = builder.lat;
        this.lng = builder.lng;
    }

    @Override
    public String toString() {
        return "Geo{" +
                "lat=" + lat +
                ", lng=" + lng +
                '}';
    }

    public static class Builder{
        private double lat;
        private double lng;

        public Builder lat(double lat){
            this.lat = lat;
            return this;
        }

        public Builder lng(double lng){
            this.lng = lng;
            return this;
        }

        public Geo build() {
            return new Geo(this);
        }

    }
}
