package storm.starter;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.testing.FeederSpout;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;
import backtype.storm.utils.Utils;
import storm.starter.bolt.JoinBolt;
import storm.starter.bolt.FilterBolt;
import storm.starter.bolt.*;

import java.util.Random;

public class JoinAndFilterTopology
{
	private static String geo_location = "";
    public static void main(String[] args)
	{
    	
        FeederSpout facebookSpout = new FeederSpout(new Fields("id", "likes", "geo_location"));
        FeederSpout twitterSpout = new FeederSpout(new Fields("id", "retweets"));
        
        // Your topology goes here.
        
        TopologyBuilder builder = new TopologyBuilder();
        builder.setSpout("facebook", facebookSpout);
        builder.setSpout("twitter", twitterSpout);
        // you need to extend this topology in order to Join and Filter the two streams
        
        builder.setBolt("join", new JoinBolt(new Fields("id", "retweets", "likes", "geo_location")))
                .fieldsGrouping("facebook", new Fields("id"))
                .fieldsGrouping("twitter", new Fields("id"));
        // Add a filter bolt to your topology to filter out any message that has retweets less than 4 or likes less than 8.
	
		builder.setBolt("filter", new FilterBolt()).shuffleGrouping("join");
		
        
        // Add another bolt to keep count of total likes and retweets per message
        
		builder.setBolt("counter", new CounterBolt()).fieldsGrouping("filter", new Fields("geo_location"));
        
        // PrinterBolt finally prints out the end result to System.out
        // This assumes that the bolt that feeds into it was named "filter",
        // change as per your topology.
        
		
        //builder.setBolt("print", new PrinterBolt()).shuffleGrouping("filter");
		
		builder.setBolt("print", new PrinterBolt()).shuffleGrouping("counter");
        
        
        
        Config conf = new Config();
        conf.setDebug(true);
        
        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology("join-and-filter-example", conf, builder.createTopology());
        
        
        Random generator = new Random();
        
        int region;
		int num_runs = 100; //was 10000000; 10000
        for(int i=0; i<num_runs; i++) {
            twitterSpout.feed(new Values(i, generator.nextInt(10 * ((i%3)+1))));
            if(i % 3 == 0) {
                geo_location = "Asia/Pacific";
            } else if(i % 3 == 1) {
                geo_location = "Europe";
            }
            else
            {
            	geo_location = "US";
            }
            facebookSpout.feed(new Values(i, generator.nextInt(25 * ((i%3)+1)), geo_location));
        }
                
        Utils.sleep(25000);
        cluster.shutdown();
    }
}