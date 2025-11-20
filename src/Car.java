public class Car
{
    private String make;
    private String model;
    private int year;
    private String condition;
    private int price;
    private String picture_path;
    private String VIN;
    private int Mileage;

    Car()
    {
        make = "";
        model = "";
        year = 0;
        condition = "";
        price = 0;
        picture_path = "";
        VIN = "";
        Mileage = 0;
    }
    Car(String make, String model, int year, String condition, int price, String picture_path, String VIN, int Mileage)
    {
        this.make = make;
        this.model = model;
        this.year = year;
        this.condition = condition;
        this.price = price;
        this.picture_path = picture_path;
        this.VIN = VIN;
        this.Mileage = Mileage;
    }

    public String getMake()
    {
        return make;
    }

    public void setMake(String make)
    {
        this.make = make;
    }

    public String getModel() {return model;}

    public void setModel(String model)
    {
        this.model = model;
    }

    public int getYear() {return year;}

    public void setYear(int year)
    {
        this.year = year;
    }

    public String getCondition() {return condition;}

    public void setCondition(String condition)
    {
        this.condition = condition;
    }

    public int getPrice() { return price;}

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getPicture_path()
    {
        return picture_path;
    }

    public void setPicture_path(String picture_path)
    {
        this.picture_path = picture_path;
    }

    public String getVIN()
    {
        return VIN;
    }

    public void setVIN(String VIN)
    {
        this.VIN = VIN;
    }

    public int getMileage() {return Mileage;}

    public void setMileage(int Mileage) {this.Mileage = Mileage;}

    @Override
    public String toString()
    {
        return super.toString();
    }
}
