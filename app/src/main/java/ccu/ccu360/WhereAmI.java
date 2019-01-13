package ccu.ccu360;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WhereAmI {
    private double x = 23.5604680;
    private double y = 120.4726590;
    public String myplace = "";
    public WhereAmI(){

    }

    public String whereami(double pointx, double pointy){
        x = pointx;
        y = pointy;
        myplace = getmyplace();
        
        return myplace;
    }

    public boolean IsPointInRect(BuildingData.Point point)
    {

        double Ax = point.Ax;
        double Ay = point.Ay;
        double Bx = point.Bx;
        double By = point.By;
        double Cx = point.Cx;
        double Cy = point.Cy;
        double Dx = point.Dx;
        double Dy = point.Dy;

        double a = (Bx - Ax) * (y - Ay) - (By - Ay) * (x - Ax);
        double b = (Cx - Bx) * (y - By) - (Cy - By) * (x - Bx);
        double c = (Dx - Cx) * (y - Cy) - (Dy - Cy) * (x - Cx);
        double d = (Ax - Dx) * (y - Dy) - (Ay - Dy) * (x - Dx);

        if((a>=0 && b>=0 && c>=0 && d>=0) || (a<=0 && b<=0 && c<=0 && d<=0))
            return true;
        return false;
    }
    public String getmyplace(){
        List<Map<String, BuildingData.Point>> listmpas = new ArrayList<>();

        boolean inrect = false;
        BuildingData buildingdata = new BuildingData();
        BuildingData.Point minpoint = buildingdata.new Point();
        try {
            //judge whether in school
            BuildingData.Point schoolpoint =buildingdata.new Point();
            schoolpoint.Ax = 23.5684030;
            schoolpoint.Ay = 120.4713940;
            schoolpoint.Bx = 23.5611340;
            schoolpoint.By = 120.4661020;
            schoolpoint.Cx = 23.5571020;
            schoolpoint.Cy = 120.4761770;
            schoolpoint.Dx = 23.5605485;
            schoolpoint.Dy = 120.4822353;
            if(IsPointInRect(schoolpoint)==false)
                return "我不在中正校園內。";

            listmpas = buildingdata.getdata();

            //find nearest point

            BuildingData.Point tmppoint;
            double min = 100000000;
            double tmp;
            for(int i=0;i<listmpas.size();i++)
            {
                tmppoint = listmpas.get(i).get("point");
               // System.out.println(tmppoint.name);
                tmp = (tmppoint.Ax - x) * (tmppoint.Ax - x) + (tmppoint.Ay - y) * (tmppoint.Ay - y);

                if (tmp<min)
                {
                    min = tmp;
                    minpoint = tmppoint;
                }
                tmp = (tmppoint.Bx - x) * (tmppoint.Bx - x) + (tmppoint.By - y) * (tmppoint.By - y);

                if (tmp<min)
                {
                    min = tmp;
                    minpoint = tmppoint;
                }
                tmp = (tmppoint.Cx - x) * (tmppoint.Cx - x) + (tmppoint.Cy - y) * (tmppoint.Cy - y);

                if (tmp<min)
                {
                    min = tmp;
                    minpoint = tmppoint;
                }
                tmp = (tmppoint.Dx - x) * (tmppoint.Dx - x) + (tmppoint.Dy - y) * (tmppoint.Dy - y);

                if (tmp<min)
                {
                    min = tmp;
                    minpoint = tmppoint;
                }
                //System.out.println("minpoint"+minpoint.name);
            }

            inrect = IsPointInRect(minpoint);

        }
        catch (IOException e){
            e.printStackTrace();
        }
        return (inrect==true)? "我在"+ minpoint.name +"內部。" : "我在"+ minpoint.name+"附近。";
    }
}
