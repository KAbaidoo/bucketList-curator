package com.example.bucketlistcurator;

public class Event {
    private String id;
    private String title;
    private String description;
    private String location;
    private String venue;
    private float rating;
    private String curator;
    private String imageResource;
    private float price;
    private String category;

    public Event() {
    }

    public Event(String id, String title, String description, String location, String venue, float rating, String curator, String imageResource, float price, String category) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.location = location;
        this.venue = venue;
        this.rating = rating;
        this.curator = curator;
        this.imageResource = imageResource;
        this.price = price;
        this.category = category;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public String getCurator() {
        return curator;
    }

    public void setCurator(String curator) {
        this.curator = curator;
    }

    public String getImageResource() {
        return imageResource;
    }

    public void setImageResource(String imageResource) {
        this.imageResource = imageResource;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
