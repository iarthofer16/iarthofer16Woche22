package net.htlgrieskirchen.pos3.iarthofer16woche22;

public class Car implements Comparable<Car>{

    private String firstName;
    private String lastName;
    private String producer;
    private String modell;

    public Car(String firstName, String lastName, String producer, String modell) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.producer = producer;
        this.modell = modell;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getModell() {
        return modell;
    }

    public void setModell(String modell) {
        this.modell = modell;
    }

    @Override
    public int compareTo(Car o) {
        return producer.compareTo(o.producer);
    }
}
