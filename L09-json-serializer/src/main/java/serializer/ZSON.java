package serializer;

import org.glassfish.json.JsonParserImpl;

import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.Serializable;
import java.io.StringReader;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

public class ZSON {
    private JsonObjectBuilder jsonObject;

    public String stringify(Serializable o){
        jsonObject = Json.createObjectBuilder();

        Field[] fArr = getFieldList(o);
        for(Field f:fArr){
            try{
                Object val = f.get(o);
                String key = f.getName();
//                System.out.println(val.getClass().getCanonicalName());
//                System.out.println(res);
                addValue(key,val,jsonObject);
                           }
            catch (Exception e){
                e.printStackTrace();
            }

        }

        JsonObject jsonO = jsonObject.build();
        return jsonO.toString();
    }

    public Object parse(String s){
        JsonReader jsonReader = Json.createReader(new StringReader(s));
        JsonObject object = jsonReader.readObject();

        for(Map.Entry e : object.entrySet()){
            System.out.println(e.getKey());
            System.out.println(e.getValue());
            System.out.println(e.getValue().getClass());
        }

        return new Object();
    }

    private void addValue(String key, Object val, JsonObjectBuilder b) throws Exception{
        addValueProcessor(key,val,b);
    }

    private void addValue(Object val, JsonArrayBuilder b) throws Exception{
        addValueProcessor(val,b);
    }

    private void addValueProcessor(Object ... params) throws Exception{
        boolean isObject;
        String key = null;
        Object val = null;
        JsonArrayBuilder arrayBuilder = null;
        JsonObjectBuilder objectBuilder = null;

        //Checking what kind of value came
        if(params.length == 3){
            isObject = true;
            key = (String)params[0];
            val = params[1];
            objectBuilder = (JsonObjectBuilder)params[2];
        }
        else if(params.length == 2){
            isObject = false;
            val = params[0];
            arrayBuilder = (JsonArrayBuilder)params[1];
        }
        else{
            throw new Exception("Internal Value Processor Error");
        }

        if(val.getClass().equals(String.class)){
            if(isObject){
                objectBuilder.add(key,(String)val);
            }
            else{
                arrayBuilder.add((String)val);
            }
        }

        else if(val.getClass().equals(Integer.class)){
            if(isObject){
                objectBuilder.add(key,(Integer)val);
            }
            else{
                arrayBuilder.add((Integer)val);
            }
        }

        else if(val.getClass().equals(Double.class)){
            if(isObject){
                objectBuilder.add(key,(Double)val);
            }
            else{
                arrayBuilder.add((Double) val);
            }
        }

        else if(List.class.isAssignableFrom(val.getClass())){
            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            try{
                for(Object v:(List)val){
                    addValue(v,arrBuilder);
                }
            }
            catch (Throwable e){
                e.printStackTrace();
            }
            finally {
                if(isObject){
                    objectBuilder.add(key,arrBuilder.build());
                }
                else{
                    arrayBuilder.add(arrBuilder.build());
                }
            }
        }

        else if(val.getClass().isArray()){
            JsonArrayBuilder arrBuilder = Json.createArrayBuilder();
            try{
                for(Object v:(Object[]) val){
                    addValue(v,arrBuilder);
                }
            }
            catch (Throwable e){
                e.printStackTrace();
            }
            finally {
                if(isObject){
                    objectBuilder.add(key,arrBuilder.build());
                }
                else{
                    arrayBuilder.add(arrBuilder.build());
                }
            }
        }


        else if(Map.class.isAssignableFrom(val.getClass())){
            JsonObjectBuilder objBuilder = Json.createObjectBuilder();
            try{
                Set<Map.Entry> set = ((Map)val).entrySet();
                for(Map.Entry<String,Object> e:set){
                    addValue(e.getKey(),e.getValue(),objBuilder);
                }
            }
            catch (Throwable e){
                e.printStackTrace();
            }
            finally {
                if(isObject){
                    objectBuilder.add(key,objBuilder.build());
                }
                else{
                    arrayBuilder.add(objBuilder.build());
                }
            }
        }


        else{
            throw new Exception("Unsupported data type: " + val.getClass().getCanonicalName());
        }
    }


    private Field[] getFieldList(Serializable o){
        try{
            return o.getClass().getDeclaredFields();
        }
        catch (Throwable e) {
            System.out.println("FAIL!");
            e.printStackTrace();
            return new Field[0];
        }
    }
}
