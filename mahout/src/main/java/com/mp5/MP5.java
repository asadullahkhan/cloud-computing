package com.mp5;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.ArrayList;
import java.lang.Double;

import org.apache.commons.cli2.OptionException; 
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.recommender.CachingRecommender;
import org.apache.mahout.cf.taste.impl.recommender.slopeone.SlopeOneRecommender;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.impl.common.LongPrimitiveIterator;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.clustering.canopy.CanopyClusterer;
import org.apache.mahout.clustering.canopy.Canopy;
import org.apache.mahout.common.distance.*;
import org.apache.mahout.math.DenseVector;
import org.apache.mahout.math.NamedVector;
import org.apache.mahout.math.Vector;

public class MP5{
    
    public static void main(String... args) throws FileNotFoundException, TasteException, IOException, OptionException {
        
        // create data source (model) - from the csv file            
        File ratingsFile = new File("datasets/ml/ratings.csv");                        
        DataModel model = new FileDataModel(ratingsFile);
        
        // create a simple recommender on our data
        CachingRecommender cachingRecommender = new CachingRecommender(new SlopeOneRecommender(model));

        PrintWriter writer = new PrintWriter("mid-output.txt", "UTF-8");
        HashMap<Double, Double> map = new HashMap<Double, Double>();
        List<Vector> data = new ArrayList<Vector>();

       // int count=0;
        // for all users
        for (LongPrimitiveIterator it = model.getUserIDs(); it.hasNext();){
            long userId = it.nextLong();
            //count++;
            //for 25k users
            //if(count<25000) {


            // get the recommendations for the user
            List<RecommendedItem> recommendations = cachingRecommender.recommend(userId, 5);
            
            //write recommendation data to file
            //writer.print("User ");

            //double[] recommendLine = new double[]

            writer.print(userId);
            
            // if empty write something
            if (recommendations.size() == 0){
                //writer.print("User ");
                writer.print(", 0");
            }
            else {
                // add the list of recommendations for each 
                for (RecommendedItem recommendedItem : recommendations) {
                    double num;
                    if(map.containsKey((double)recommendedItem.getItemID())) {
                        num = (map.get((double)recommendedItem.getItemID()))+1.0;
                        map.remove(recommendedItem.getItemID());
                    }
                    else
                        num = 1;
                    map.put((double)recommendedItem.getItemID(), num);
                    writer.print(", "+recommendedItem.getItemID()+", "+recommendedItem.getValue());
                }
            }
            // }
            writer.println();          
            
        }
        writer.close();

        writer = new PrintWriter("mid-output2.txt", "UTF-8");

        for(double key : map.keySet()) {
            //double[] x = {0,map.get(key)};
            //key.toString();
            data.add(new NamedVector(new DenseVector( new double[]{map.get(key)}), (""+key)));
            writer.println(key+" "+map.get(key));
        }
        writer.close();
        writer = new PrintWriter("MP5-output.txt", "UTF-8");
        DistanceMeasure measure = new ManhattanDistanceMeasure();
        //CanopyClusterer cc = new CanopyClusterer(measure, 100, 100);
        List<Canopy> canopies = CanopyClusterer.createCanopies(data, measure, 50.0, 50.0);
        for(Canopy c : canopies)
            writer.println(c.asFormatString());

        writer.close();

    }
}

