package com.example.weatherapidemo;

import java.util.List;

public class WeatherResponse {
    public Records records;//這次的Api資料，records後面是{}，也就是class類別，所以要建立一個records的class

    public class Records{//接著看到records底下的location，他後面是先[]才是{}，意思是location裡面的資料是用list包起來，再更裡面的資料是class類別
        public List<Location> location;//這裡代表在List裡面放入Location的物件，而Location是class，所以就是用List去包class，就可以完成上述的順序

    }//下面的資料都是以相同的架構去寫
    public class Location{
        public String locationName;
        public List<WeatherElement> weatherElement;

        @Override
        public String toString() {
            return "Location{" +
                    "weatherElement=" + weatherElement +
                    '}';
        }
    }
    public class WeatherElement{
        public String elementName;
        public List<Time> time;
    }
    public class Time{
        public Parameter parameter;
    }
    public class Parameter{
        public String parameterName;
        public String parameterUnit;
    }
    public String getDataByTime(Integer index,Integer day){
        return records.location.get(0).weatherElement.get(index).time.get(day).parameter.parameterName;
    }
    public String getUnitByTime(Integer index){
        return records.location.get(0).weatherElement.get(0).time.get(index).parameter.parameterUnit;
    }
    public Integer getElementSize(){
        return records.location.get(0).weatherElement.size();
    }
}
