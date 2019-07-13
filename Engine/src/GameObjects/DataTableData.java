package GameObjects;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TextField;

import java.awt.*;

public class DataTableData {

    private final SimpleStringProperty type;
    private final SimpleIntegerProperty FP;
    private TextField amount;
    private TextField price;

    public DataTableData(String type, Integer fp, String famount, String fprice) {
        this.type = new SimpleStringProperty(type);
        this.FP = new SimpleIntegerProperty(fp);
        this.amount=new TextField(famount);
        this.price=new TextField(fprice);
    }



    public String getType(){
        return  this.type.get();
    }
    public void setType(String fType) {
        type.set(fType);
    }

    public Integer getFP(){
        return this.FP.get();
    }
    public void setFP(Integer fFP) {
        FP.set(fFP);
    }
    public void setAmount(TextField amount)
    {
        this.amount =amount;
    }
    public TextField getAmount()
    {
        return  amount;
    }
    public void setPrice(TextField price)
    {
        this.price =price;
    }
    public TextField getPrice()
    {
        return  price;
    }


}
